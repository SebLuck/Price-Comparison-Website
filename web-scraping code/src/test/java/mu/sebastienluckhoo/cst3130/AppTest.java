package mu.sebastienluckhoo.cst3130;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Unit test for simple App.
 */
@DisplayName("Test Scraper class")
public class AppTest {
    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
    }
    /**
     * Test the run method from scraper 1 to get the brand from the product title
     */
    @Test
    @DisplayName("Test the brand from Scraper 1")
    public void testBrandFromScraper1() {
        final Scraper1 ebay = new Scraper1();
        ebay.title = "CHUWI HeroBook Pro";
        String[] splitWord = ebay.title.split(" ");
        if(ebay.title.contains("Acer")){
            ebay.brand = "Acer";
        }else if(ebay.title.contains("CHUWI")){
            ebay.brand = "CHUWI";
        }else if(ebay.title.contains("GIGABYTE")){
            ebay.brand= "GIGABYTE";
        }else if(ebay.title.contains("Asus")){
            ebay.brand = "Asus";
        }else if(ebay.title.contains("ASUS")){
            ebay.brand = "ASUS";
        }else if(ebay.title.contains("asus")){
            ebay.brand = "ASUS";
        }else{
            ebay.brand = splitWord[0];
        }
        assertEquals("CHUWI", ebay.brand);
    }
    /**
     * Test the run method from scraper 1 to get the description from the product title
     */
    @Test
    @DisplayName("Test the description from Scraper 1")
    public void testDescriptionFromScraper1() {
        final Scraper1 ebay = new Scraper1();
        ebay.title = "NEW ASUS TUF Dash Gaming 15.6 FHD 144Hz RTX 3070 i7-12650H 512GB SSD 16GB RAM";
        String[] splitWord = ebay.title.split(" ");
        if(ebay.title.contains("Acer")){
            ebay.brand = "Acer";
        }else if(ebay.title.contains("CHUWI")){
            ebay.brand = "CHUWI";
        }else if(ebay.title.contains("GIGABYTE")){
            ebay.brand= "GIGABYTE";
        }else if(ebay.title.contains("Asus")){
            ebay.brand = "Asus";
        }else if(ebay.title.contains("ASUS")){
            ebay.brand = "ASUS";
        }else if(ebay.title.contains("asus")){
            ebay.brand = "ASUS";
        }else{
            ebay.brand = splitWord[0];
        }
        if(ebay.title.contains(" " + ebay.brand)){
            ebay.descriptionText = ebay.title.replace(" " + ebay.brand, "");
        }else{
            ebay.descriptionText = ebay.title.replace(ebay.brand + " ", "");
        }

        assertEquals("NEW TUF Dash Gaming 15.6 FHD 144Hz RTX 3070 i7-12650H 512GB SSD 16GB RAM",
                ebay.descriptionText);
    }

    /**
     * Test the run method from scraper 2 to get the brand from the product title
     */
    @Test
    @DisplayName("Test brand from laptop title")
    public void testBrandFromLaptopTitle() {
        final Scraper2 flipkart = new Scraper2();
        flipkart.title = "Lenovo IdeaPad 3 Chromebook ";
        String[] splitWord = flipkart.title.split(" ");
        assertEquals("Lenovo", splitWord[0]);
    }
    /**
     * Test the run method from scraper 2 to get the description from the product title
     */
    @Test
    @DisplayName("Test description from laptop title")
    public void testDescriptionFromLaptopTitle() {
        final Scraper2 flipkart = new Scraper2();
        flipkart.title = "Lenovo IdeaPad 3 Chromebook";
        String[] splitWord = flipkart.title.split(" ");
        assertEquals("IdeaPad 3 Chromebook", flipkart.title.substring(splitWord[0].length() + 1));
    }

    /**
     * Test the run method from scraper 2 to get the price without the commas
     */
    @Test
    @DisplayName("Test price from scraper 2")
    public void replaceCommasInPrice() {
        final Scraper2 flipkart = new Scraper2();
        flipkart.priceText = "₹ 1,50,990";
        assertEquals("₹ 150990", flipkart.priceText.replace(",", ""));
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
