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
 * Scraping data from flipkart
 */
public class Scraper2 extends Thread{
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
            String link = "https://www.flipkart.com/search?q=laptop&otracker=search&otracker1=search&marketplace=" +
                    "FLIPKART&as-show=on&as=off&page="+(i+1);

            try {
                doc = Jsoup.connect(link).get();
                sleep(1000 * crawlDelay);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Scraper 2 thread is scraping data from Flipkart page " +(i+1)+ "...");

            Elements productElements = doc.getElementsByClass("_1YokD2 _3Mn1Gg");

            for (Element productElement : productElements) {
                //Get the product link
                Elements productLink = productElement.getElementsByClass("_1fQZEK").select("a");

                //Get the product title
                Elements description = productElement.getElementsByClass("_4rR01T");

                //Get the product price
                Elements priceElement = productElement.getElementsByClass("_30jeq3 _1_WHN1");

                //Get the image
                Elements image = productElement.getElementsByClass("CXW8mj").
                        select("img");


                for(int j = 0; j < priceElement.size(); j++){
                    title = description.get(j).text();
                    priceText = priceElement.get(j).text();
                    imageUrl = image.get(j).attr("src");
                    url = "https://www.flipkart.com" +productLink.get(j).attr("href");
                    // Split the title to check if this product has already been added to the database.
                    String[] arrayTitle1 = title.split(" ");
                    boolean isDuplicate = false;
                    int titleSize = titleArray.size();
                    // Check for duplication
                    for(int k = 0; k < titleSize; k++){
                        String[] arrayTitle2 = titleArray.get(k).split(" ");
                        if(Objects.equals(arrayTitle1[0], arrayTitle2[0]) &&
                                Objects.equals(arrayTitle1[1], arrayTitle2[1]) &&
                                Objects.equals(arrayTitle1[2], arrayTitle2[2]) &&
                                Objects.equals(arrayTitle1[3], arrayTitle2[3])){
                            // set isDuplicate to true if there is duplication
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
                    brand = splitWord[0];
                    // Get the description
                    descriptionText = title.substring(splitWord[0].length() + 1);

                    //Add the laptop to the database using Hibernate with Spring
                    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                    HibernateLaptop hibernate = (HibernateLaptop) context.getBean("hibernate");
                    //Add data to database
                    hibernate.addLaptop(descriptionText, priceText.replace(",", ""),
                            imageUrl, url, brand);
                    //Shut down Hibernate
                    hibernate.shutDown();

                }
            }
        }
        System.out.println("Scraper 2 thread has scraped all the required data from Flipkart.");
        System.out.println();
    }
}
