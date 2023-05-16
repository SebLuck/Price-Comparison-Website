# Price-Comparison-Website-Java-JavaScript
In this project, the website consists of a price comparison website for laptops as a product. The user can compare the same laptop on different websites. There is a search bar for the user to search for different laptops and pagination for the user to navigate through different pages.

There are five websites that have been scraped to get all the data:  
- eBay
- Flipkart
- Microcenter
- TigerDirect
- Officedepot

The data that was gathered from these five websites were the descriptions, brands, URLs, images, and prices. Jsoup has been used as a library to scrape the data from the websites.

Maven was used to add all dependencies, like Junit 5, Jsoup, Spring Framework, Hibernate, and MySQL. This will allow the web application to scrape the data from all websites using Jsoup and add them to the database using Hibernate and MySQL.

MySQL and Hibernate were used to store all the details of each laptop in the database. Hibernate was used to map Java classes to database tables using annotations and to map Java data types to SQL data types.  

The RESTful API and Vue framework were used for the search and pagination functionality.  
The Vue framework was also used to display the products.


