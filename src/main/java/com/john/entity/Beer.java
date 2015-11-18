package com.john.entity;


import com.john.annotation.XmlElement;
import com.john.annotation.XmlElementWrapper;
import com.john.annotation.XmlRootElement;


import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "order")
public class Beer {
    private String name;
    private String beerType;
    private Alcohol alcoholType;

    private String manufacturer;
    private String filtered;
    private String material;
    private double volume;

   // @XmlElementWrapper(name = "characteristics")
    private List<Characteristic> characteristics;

//    private List<String> ingredients;


    public Beer() {
//        ingredients = new ArrayList<>();
        characteristics = new ArrayList<>();
    }

   // @XmlElement()
    public void setName(String name) {
        this.name = name;
    }

    //@XmlElement(name = "beerType")
    public void setBeerType(String type) {
        this.beerType = type;
    }

   // @XmlElement(name = "alcoholType")
    public void setAlcoholType(Alcohol alcohol) {
        this.alcoholType = alcohol;
    }

    //@XmlElement(name = "manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

  //  @XmlElement(name = "filtered")
    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    //
   // @XmlElement(name = "material")
    public void setMaterial(String material) {
        this.material = material;
    }

   // @XmlElement(name = "volume")
    public void setVolume(double volume) {
        this.volume = volume;
    }

   // @XmlElement(name = "characteristic")
    public void addCharacteristics(Characteristic characteristic) {
        characteristics.add(characteristic);
    }
//
//    @SetterField(name = "ingredients")
//    public void setIngredients(List<String> ingredients) {
//        this.ingredients = ingredients;
//    }

    public String toSourceString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name + " " + beerType + " " + filtered + " " + alcoholType + ". " + manufacturer + "\n");
        // builder.append(name + " " + type + " " + filtered + " " +  manufacturer + "\n");


        builder.append("Ингридиенты: ");
//        for (String ingredient : ingredients) {
//            builder.append(ingredient + "; ");
//        }

        builder.append("\n" + material + " " + volume + " л.");
        return builder.toString();
    }

    public enum Alcohol {
        ALCOHOL, NO_ALCOHOL;
    }
}
