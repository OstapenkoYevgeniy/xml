package com.john.entity;

import java.util.ArrayList;
import java.util.List;

public class Beer {
    private long id;
    private String name;
    private String type;
    private String alcohol;
    private String manufacturer;
    private List<Characteristic> characteristics = new ArrayList<>();

    private List<String> ingredients = new ArrayList<>();

    private String filtered;


    private String material;
    private double volume;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String toSourceString() {
        String result = "id: " + id + "\n";
        result += "this: " + this + "\n";
        result += "name: " + name + "\n";
        result += "type: " + type + "\n";
        result += "alcohol: " + alcohol + "\n";
        result += "manufacturer: " + manufacturer + "\n";

        result += ingredients.size() + "ingridients:\n";
        for (String ingredient : ingredients) {
            result += ingredient + "\n";
        }

        result += characteristics.size() + "characteristic: \n";
        for (Characteristic characteristic : characteristics) {
            result+= characteristic.toSourceString() + "\n";
        }
        return result;
    }

}

