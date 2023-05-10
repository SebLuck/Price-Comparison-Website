package mu.sebastienluckhoo.cst3130;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main method that starts hibernate and run the 5 threads
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //Create and wire beans using annotations.
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //Get web scraper manager beans
        ScraperThread thread = (ScraperThread) context.getBean("scraperThread");
        // Start threads
        thread.startThread();
        //Wait for threads to finish
        thread.joinThread();

        System.out.println("Web scraping completed");

    }




}
