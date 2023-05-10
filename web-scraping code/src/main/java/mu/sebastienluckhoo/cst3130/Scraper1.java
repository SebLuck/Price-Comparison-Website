package mu.sebastienluckhoo.cst3130;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Scraping data from ebay
 */

public class Scraper1 extends Thread{
    //Interval between HTTP requests
    long crawlDelay = 5;
    // Arraylist to check for duplication with the title
    ArrayList<String> titleArray = new ArrayList<>();
    String title;
    String priceText;
    String url;
    String imageUrl;
    String descriptionText;
    String brand;

    /**
     * Run method for the thread
     */

    @Override
    public void run(){
        Document doc;
        int numberPage = 10;
        // Iterate through multiple pages
        for(int i = 0; i < numberPage; i++) {
            String link = "https://www.ebay.com/sch/i.html?_dcat=177&_fsrp=1&rt=nc&_from=R40&LH_ItemCondition=1000&" +
                    "Brand=ASUS%7CChuwi%7CGIGABYTE%7CAcer&Type=Notebook%252FLaptop&Most%2520Suitable%2520For=Gaming%" +
                    "7CGraphic%2520Design&_nkw=laptop&_sacat=0&LH_BIN=1&_pgn=" + (i+1);
            try {
                doc = Jsoup.connect(link).get();
                sleep(1000 * crawlDelay);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Scraper 1 thread is scraping data from Ebay page " +(i+1) + "...");


            Elements productElements = doc.getElementsByClass("srp-results srp-list clearfix");

            for (Element productElement : productElements) {
                //Get the product link
                Elements productLink = productElement.getElementsByClass("s-item__link");

                //Get the product title
                Elements description = productElement.getElementsByClass("s-item__image-img");

                //Get the product price
                Elements price = productElement.getElementsByClass("s-item__price");

                //Get the image
                Elements image = productElement.getElementsByClass("s-item__image-img");


                for(int j = 0; j < price.size(); j++){
                    title = description.get(j).attr("alt");
                    priceText = price.get(j).text();
                    url = productLink.get(j).attr("href");
                    imageUrl = image.get(j).attr("src");
                    // Split the title to check if this product has already been added to the database.
                    String[] arrayTitle1 = title.split(" ");
                    if(arrayTitle1.length <= 3){
                        continue;
                    }
                    boolean isDuplicate = false;
                    int titleSize = titleArray.size();
                    // Check for duplication
                    for(int k = 0; k < titleSize; k++){
                        String[] arrayTitle2 = titleArray.get(k).split(" ");
                        if(Objects.equals(arrayTitle1[0], arrayTitle2[0]) &&
                                Objects.equals(arrayTitle1[1], arrayTitle2[1]) &&
                                Objects.equals(arrayTitle1[2], arrayTitle2[2]) &&
                                Objects.equals(arrayTitle1[3], arrayTitle2[3])){
                            // set isDuplicate to true if there is duplication.
                            isDuplicate = true;
                            break;
                        }
                    }
                    // If there is duplication, do not add the data to the database.
                    if(isDuplicate){
                        continue;
                    }

                    titleArray.add(title);

                    // split title by spaces
                    String[] splitWord = title.split(" ");
                    // Get the brand from the title
                    if(title.contains("Acer")){
                        brand = "Acer";
                    }else if(title.contains("CHUWI")){
                        brand = "CHUWI";
                    }else if(title.contains("GIGABYTE")){
                        brand = "GIGABYTE";
                    }else if(title.contains("Asus")){
                        brand = "Asus";
                    }else if(title.contains("ASUS")){
                        brand = "ASUS";
                    }else if(title.contains("asus")){
                        brand = "ASUS";
                    }else{
                        brand = splitWord[0];
                    }
                    // Get the description
                    // remove the brand from the title
                    if(title.contains(" " + brand)){
                        descriptionText = title.replace(" " + brand, "");
                    }
                    if(title.contains(brand + " ")){
                        descriptionText = title.replace(brand + " ", "");
                    }

                    //Add the laptop to the database using Hibernate with Spring
                    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                    HibernateLaptop hibernate = (HibernateLaptop) context.getBean("hibernate");
                    //Add data to database
                    hibernate.addLaptop(descriptionText, priceText, imageUrl, url, brand);
                    //Shut down Hibernate
                    hibernate.shutDown();

                }
            }
        }
        System.out.println("Scraper 1 thread has scraped all the required data from Ebay.");
        System.out.println();

    }

}
