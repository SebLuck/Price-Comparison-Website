package mu.sebastienluckhoo.cst3130;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
/**
 * class for the spring configuration
 */
@Configuration
public class AppConfig {
    SessionFactory sessionFactory;

    /**
     * function to add all the 5 scraper to the threadList
     */
    @Bean
    public ScraperThread scraperThread(){
        ScraperThread scraperThread = new ScraperThread();
        //Create list of Threads
        List<Thread> threadList = new ArrayList();
        threadList.add(myScraper1());
        threadList.add(myScraper2());
        threadList.add(myScraper3());
        threadList.add(myScraper4());
        threadList.add(myScraper5());
        ScraperThread.setThreadList(threadList);
        return scraperThread;
    }
    /**
     * Scraper for ebay
     */
    @Bean
    public Scraper1 myScraper1(){
        Scraper1 ebay = new Scraper1();
        return ebay;
    }
    /**
     * Scraper for flipkart
     */
    @Bean
    public Scraper2 myScraper2(){
        Scraper2 flipkart = new Scraper2();
        return flipkart;
    }
    /**
     * Scraper for microCenter
     */
    @Bean
    public Scraper3 myScraper3(){
        Scraper3 microCenter = new Scraper3();
        return microCenter;
    }
    /**
     * Scraper for tigerDirect
     */
    @Bean
    public Scraper4 myScraper4(){
        Scraper4 tigerDirect = new Scraper4();
        return tigerDirect;
    }
    /**
     * Scraper for officeDepot
     */
    @Bean
    public Scraper5 myScraper5(){
        Scraper5 officeDepot = new Scraper5();
        return officeDepot;
    }
    @Bean
    public HibernateLaptop hibernate(){
        HibernateLaptop hibernate = new HibernateLaptop();
        hibernate.setSessionFactory(sessionFactory());
        return hibernate;
    }
    /** Sets up the session factory. */
    @Bean
    public SessionFactory sessionFactory (){
        // Build the sessionFactory only once
        if(sessionFactory == null) {
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file.
                standardServiceRegistryBuilder.configure("resources/hibernate.cfg.xml");

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    //Create the session factory.
                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                } catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory*/
                    System.err.println("Session Factory build failed.");
                    e.printStackTrace();
                    StandardServiceRegistryBuilder.destroy(registry);
                }

                //Output result
                System.out.println("Session factory built.");

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("SessionFactory creation failed." + ex);
            }
        }
        return sessionFactory;
    }
}


