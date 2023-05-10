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
 * Scraping data from tigerdirect
 */
public class Scraper4 extends Thread{
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
        int numberPage = 3;
        // Iterate through multiple pages
        for(int i = 0; i < numberPage; i++) {
            String link = "https://www.tigerdirect.com/applications/category/guidedSearch.asp?CatId=17&sel=Detail" +
                    "%3B175_100_128752_128752&pageSize=30&page="+(i+1);
            
            try {
                doc = Jsoup.connect(link).get();
                sleep(1000 * crawlDelay);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Scraper 4 thread is scraping data from TigerDirect page " +(i+1)+ "...");

            Elements productElements = doc.getElementsByClass("sku-display");
            for (Element productElement : productElements) {

                //Get the product price
                Elements price = productElement.getElementsByClass("d-price");

                productElement.getElementsByClass("priceDecimal aria-hidden=").remove();
                //Get the product url
                Elements productLink = productElement.getElementsByClass("sku-namecategory");

                //Get the product title
                Elements description = productElement.getElementsByClass("sku-namecategory");

                //Get the image
                Elements image = productElement.getElementsByClass("itemImage").
                        select("img");


                for(int j = 0; j < price.size(); j++){
                    title = description.get(j).text();
                    priceText = price.get(j).text();
                    imageUrl = image.get(j).attr("src");
                    url = "https://www.tigerdirect.com/" +productLink.get(j).attr("href");
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
                    hibernate.addLaptop(descriptionText, priceText, imageUrl, url, brand);
                    //Shut down Hibernate
                    hibernate.shutDown();

                }
            }
        }
        System.out.println("Scraper 4 thread has scraped all the required data from TigerDirect.");
        System.out.println();
    }
}
