package mu.sebastienluckhoo.cst3130;

import javax.persistence.*;

import java.io.Serializable;

/**
 * class for laptop table using annotation
 */
@Entity
@Table(name="laptop")
public class LaptopAnnotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;


    /**
     * constructor
     */
    public LaptopAnnotation(){
    }

    /**
     * Getters and setters
     */
    public int getId() {return id;}
    public String getBrand() {return brand;}
    public String getDescription(){return description;}
    public String getImage(){return image;}

    public void setId(int id) {this.id = id;}
    public void setBrand(String brand) {this.brand = brand;}
    public void setDescription(String description){this.description = description;}
    public void setImage(String image){this.image = image;}



}

