var app = new Vue({
    el: '#shopping',
    data: {
        laptops: [],
        num_items: 24,
        pageNumber: 0,
        searchLaptop: "",
        numResults: 0,
        laptopDetails: [],
        comparison: [],
        comparisonLaptops: [],
        total_page: 0,
        number_page_created: false,
        next_counter: 0
    },
    methods : {
        searchLaptops: async function (){
            // Set the comparisonLaptops object to empty
            this.comparisonLaptops = [];
            // make it scroll to the top
            document.documentElement.scrollTop = 0;
            /*Remove all the number pages when searching*/
            if(this.number_page_created == true){
                for(i=0;i < this.total_page; i++){
                    document.querySelector(`#numberPage${i+1}`).remove();
                    this.number_page_created = false;

                    this.pageNumber = 0;
                    this.next_counter = 0;
                }
            }
            /*set the main page, comparison page, first and previous buttons to none when the user try to search 
            for a laptop and then display the laptops*/
            document.querySelector(".main_page").style.display = "none";
            document.querySelector(".product_page").style.display = "block";
            document.querySelector(".comparison_page").style.display = "none";
            document.querySelector(".previous").style.display = "none";
            document.querySelector(".first").style.display = "none";

            let page_number_div= document.querySelector("#page_number");
            //set the offset
            let offset = this.pageNumber * this.num_items;
            // This variable will make the get request to the server
            let paginationUrl = "/search/" + this.searchLaptop + "?num_items=" + this.num_items +"&offset=" + offset;
            try{
                 //Get all the laptops based on the search of the user from the web service using Axios.
                let searchResult = await axios.get(paginationUrl);
                //Point laptops in data to return an array of laptops.
                this.laptops = searchResult.data.laptop;
                // total number of laptops obtained from the get request
                this.numResults = searchResult.data.totNumItems;
                // total number of pages
                this.total_page = Math.ceil(this.numResults/this.num_items);
                // Check if all the number pages has been added
                for(i=0;i < this.total_page; i++){
                    let button_page = document.createElement("button_page");
                    // set the first page as active when searching
                    if(i == 0){
                        button_page.innerHTML = `<p class="numberPage active" id="numberPage${i+1}" > ${i+1} </p>`;
                    }else{
                        button_page.innerHTML = `<p class="numberPage" id="numberPage${i+1}" > ${i+1} </p>`;                            
                    }
                    page_number_div.appendChild(button_page);
                    // This will display only the first five numbers of pages
                    if(i <= 4){
                        document.querySelector(`#numberPage${i+1}`).style.display = "block";
                    }
                }
                this.number_page_created = true;
                // set the next button and last button to none if there is only 1 page for the pagination
                if(this.total_page > 1){
                    document.querySelector(".next").style.display = "block";
                    document.querySelector(".last").style.display = "block";
                }else{
                    document.querySelector(".next").style.display = "none";
                    document.querySelector(".last").style.display = "none";
                }

            }
            catch(ex){
                console.error("Error fetching data from server: " + ex);
            }
        },
        /* This function will be called only when the user will click on the next, previous, last and first buttons*/
        searchLaptopsPagination: async function (){
            // Set the comparisonLaptops object to empty
            this.comparisonLaptops = [];
            document.documentElement.scrollTop = 0;
            document.querySelector(".main_page").style.display = "none";
            document.querySelector(".product_page").style.display = "block";
            document.querySelector(".comparison_page").style.display = "none";
            //set the offset
            let offset = this.pageNumber * this.num_items;
            // This variable will make the get request to ther server
            let paginationUrl = "/search/" + this.searchLaptop + "?num_items=" + this.num_items +"&offset=" + offset;
            try{
                 //Get all the laptops based on the search of the user from the web service using Axios.
                let searchResult = await axios.get(paginationUrl);
                //Point laptops in data to return an array of laptops.
                this.laptops = searchResult.data.laptop;
                // total number of laptops obtained from the get request
                this.numResults = searchResult.data.totNumItems;
                // total number of pages
                this.total_page = Math.ceil(this.numResults/this.num_items);

            }
            catch(ex){
                console.error("Error fetching data from server: " + ex);
            }
        },
        // This function will be called when the next button will be clicked
        nextPage: function(){
            /*Display the previous button every time the user clicked on the next button  */
            document.querySelector(".previous").style.display = "block";
            // Display the first button when the user clicks on the next button.
            document.querySelector(".first").style.display = "block";
            this.pageNumber++;
            // When the next button is clicked, the next page number will be set to active and the one that was active will be set to inactive.
            document.getElementById(`numberPage${this.pageNumber}`).className = "numberPage";
            document.getElementById(`numberPage${this.pageNumber+1}`).className = "numberPage active";

            /* The next_counter variable is to count how many times the user has reached the last number of pages from the five that are displayed.
                For example, if the five page numbers are 1, 2, 3, 4, and 5, when page number 5 is reached, page number 6 will be displayed and
                 page number 1 will be hidden.
               */
            if(this.pageNumber >= 4 && this.pageNumber != this.total_page-1){
                document.querySelector(`#numberPage${this.pageNumber-3}`).style.display = "none";
                document.querySelector(`#numberPage${this.pageNumber+2}`).style.display = "block";
                this.next_counter++;
            }

            // If the last page has been reached, the next button will be set to none.
            if(this.pageNumber === this.total_page-1){
                document.querySelector(".next").style.display = "none";
                document.querySelector(".last").style.display = "none";
            }

            this.searchLaptopsPagination();

        },
        previousPage: function(){
            /* 
               Display the next button every time the user clicks on the previous button.
               This will help in case the user has reached the last page and wants to go back to the previous pages.
            */ 
            document.querySelector(".next").style.display = "block";
            /*Display the last button in case the user has clicked on the last button and wants to click on the previous button.
            Normally, when the last page is reached, the last button is set to none.*/
            document.querySelector(".last").style.display = "block";
            this.pageNumber--;
            /* When the prevoius button is clicked, the prevoius page number will be set to active, 
            and the one that was active will be inactive.*/
            document.getElementById(`numberPage${this.pageNumber+2}`).className = "numberPage";
            document.getElementById(`numberPage${this.pageNumber+1}`).className = "numberPage active";
            /*This if statement will check if the first number of pages from five that are displayed has been reached.
             For example, if the five pages are 3, 4, 5, 6, and 7, when page number 3 is reached, page number 7 will
              be hidden and page number 2 will be displayed. */
            if(this.pageNumber == this.next_counter && this.pageNumber != 0){
                this.next_counter--;
                document.querySelector(`#numberPage${this.pageNumber}`).style.display = "block";
                document.querySelector(`#numberPage${this.pageNumber+5}`).style.display = "none";
            }

            // Check if the user is on the first page
            if(this.pageNumber == 0){
                // set the previous button as none
                document.querySelector(".previous").style.display = "none";
                document.querySelector(".first").style.display = "none";
            }

            this.searchLaptopsPagination();
        },
        /*This function will navigate to the last page of the pagination by calling the function nextPage() multiple times.*/
        lastPage: function(){
            let pageDifference= this.total_page-1 - this.pageNumber;
            document.querySelector(".last").style.display = "none";
            for(i=0;i < pageDifference; i++){
                this.nextPage();
            }
        },
        /*This function will navigate to the first page of the pagination by calling the function previousPage() multiple times.*/
        firstPage: function(){
            document.querySelector(".first").style.display = "none";
            let actualPageNumber = this.pageNumber;
            for(i=0;i < actualPageNumber; i++){
                this.previousPage();
            }
        },
        // This function will be called when the user clicks on the title of the website, which will make them go to the main page.
        mainPage: function(){
            document.documentElement.scrollTop = 0;
            /* In case the user wants to go back to the main page, set the product and comparison pages to none.*/
            document.querySelector(".main_page").style.display = "block";
            document.querySelector(".product_page").style.display = "none";
            document.querySelector(".comparison_page").style.display = "none";
        },
        /* This function will be called when the user clicks on a laptop name on the product page.
         It will display the price, description, and image of the laptop that the user is looking for.
          It will show on which websites the laptops are available. The user can click on the website name 
          or description to go to the website that sells the laptop. */
        comparisonPage: async function(id){
            document.documentElement.scrollTop = 0;
            document.querySelector(".main_page").style.display = "none";
            document.querySelector(".product_page").style.display = "none";
            document.querySelector(".comparison_page").style.display = "block";
            // This will store the details of the selected laptop in the object laptopDetails.
            this.laptopDetails = (await axios.get("/laptops/" + id)).data;
            // This will store all the laptops in the database in the object comparison.
            this.comparison = (await axios.get("/laptops")).data;
            // Get the link, brand, and description of the selected laptop.
            let link = this.laptopDetails[0].url;
            let brands = this.laptopDetails[0].brand;
            let descriptions = this.laptopDetails[0].description;
            // Get the title of the selected laptop by concatenating the brand and the description.
            let laptopTitle = brands.concat(" ", descriptions);
            // Split the selected laptop title 
            let laptopTitleArray = laptopTitle.split(" ");
            // Get the website name of the selected laptop.
            let domain = (new URL(link));
            domain = domain.hostname.replace("www.", "").replace(".com", "");
            // The object comparisonLaptops will store all the same laptops which are from different websites.
            this.comparisonLaptops.push(
                {price: this.laptopDetails[0].price, 
                 url: this.laptopDetails[0].url,
                 website_name: domain}
            );
            /* This for loop will loop through all the laptops in the database to compare the title 
            of each one to the selected laptop.*/
            for(i=0;i < this.comparison.totNumItems; i++){
                //Get the title of the laptop that is being compared.
                let descriptionsComparison = this.comparison.laptop[i].description;
                let brandComparison = this.comparison.laptop[i].brand;
                let laptopTitleComparison = brandComparison.concat(" ", descriptionsComparison)
                // Split the title to compare it to the title of the selected laptop.
                let arrayDescription = laptopTitleComparison.split(" ");
                if(arrayDescription[0] == laptopTitleArray[0] && arrayDescription[1] == laptopTitleArray[1] 
                    && arrayDescription[2] == laptopTitleArray[2] && arrayDescription[3] == laptopTitleArray[3]){
                        // Get the website name of the laptop that is being compared to the selected laptop.
                        let link_comparison = this.comparison.laptop[i].url;
                        let domain_comparison = (new URL(link_comparison));
                        domain_comparison = domain_comparison.hostname.replace("www.", "").replace(".com", "");
                        let duplicate_counter = 0;
                        // This for loop will check if the comparisonLaptops object has a duplicate website name.
                        for(j=0; j < Object.keys(this.comparisonLaptops).length; j++){
                            if(domain_comparison == this.comparisonLaptops[j].website_name){
                                duplicate_counter = 1;
                                break;
                            }
                        }
                        // If there is no duplicate website, then add the price, url, and website name to the comparisonLaptops object.
                        if(duplicate_counter == 0){
                            this.comparisonLaptops.push(
                                {price: this.comparison.laptop[i].price, 
                                 url: this.comparison.laptop[i].url,
                                 website_name: domain_comparison}
                            );
                        }    
                }
            }
        }
    }
})


