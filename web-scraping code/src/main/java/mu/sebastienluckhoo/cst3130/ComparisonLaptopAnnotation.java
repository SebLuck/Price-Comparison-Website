package mu.sebastienluckhoo.cst3130;


import javax.persistence.*;
import java.io.Serializable;
/**
 * class for laptop_comparison table using annotation
 */
@Entity
@Table(name="laptop_comparison")
public class ComparisonLaptopAnnotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "laptop_id")
    private int laptopId;

    @Column(name = "price")
    private String price;
    @Column(name = "url")
    private String url;
    /**
     * constructor
     */
    public ComparisonLaptopAnnotation(){
    }
    /**
     * Getters and setters
     */
    public int getId() {return id;}
    public int getLaptopId(){return laptopId;}
    public String getPrice(){return price;}
    public String getUrl(){return url;}

    public void setId(int id) {this.id = id;}
    public void setLaptopId(int laptopId){this.laptopId = laptopId;}
    public void setPrice(String price){this.price = price;}
    public void setUrl(String url){this.url = url;}

}
