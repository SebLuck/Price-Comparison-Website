package mu.sebastienluckhoo.cst3130;

import java.util.List;

/**
 * Class that will start all the threads
 */
public class ScraperThread {
    private static List<Thread> threadList;
    /** Constructor */
    ScraperThread(){
    }
    /**
     * get the thread
     */
    public static List<Thread> getThreadList() {
        return threadList;
    }
    /**
     * set the thread
     */
    public static void setThreadList(List<Thread> threadList) {
        ScraperThread.threadList = threadList;
    }
    /**
     * Run all the threads
     */
    public void startThread(){
        for(Thread laptopScraper : threadList){
            laptopScraper.start();
        }
    }
    /**
     * Wait for all threads to finish
     */
    public void joinThread(){
        for(Thread laptopScraper : threadList){
            try {
                laptopScraper.join();
            }catch (InterruptedException ex){
                System.err.println(ex.getMessage());
            }
        }
    }
}
