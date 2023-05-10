package mu.sebastienluckhoo.cst3130;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


/** This class will create new sessions to interact with the database*/
public class HibernateLaptop {
    SessionFactory sessionFactory;

    /** Empty constructor */
    HibernateLaptop() {
    }

    /** Closes Hibernate down and stops its threads from running*/

    public void shutDown(){
        sessionFactory.close();
    }
    /** This function will add all the data from the websites to the database.
     * @param description the description of the laptop
     * @param price the price of the laptop
     * @param image the image of the laptop
     * @param url the url of the laptop
     * @param brand the brand of the laptop
     */
    public void addLaptop(String description, String price, String image, String url, String brand){
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();
        //Create an instance of a laptop class and comparison class
        LaptopAnnotation laptop = new LaptopAnnotation();
        ComparisonLaptopAnnotation comparison = new ComparisonLaptopAnnotation();
        //Set the values of the laptop class and comparison class that we want to add.
        laptop.setDescription(description);
        laptop.setBrand(brand);
        laptop.setImage(image);
        //Add data to database
        session.save(laptop);

        comparison.setLaptopId(laptop.getId());
        comparison.setPrice(price);
        comparison.setUrl(url);
        //Add data to database
        session.save(comparison);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Laptop added to database with ID: " + laptop.getId());

    }
    /** get the session*/
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    /** set the session*/
    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

}
