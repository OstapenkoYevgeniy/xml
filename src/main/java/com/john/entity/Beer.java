package com.john.entity;

import java.util.List;

public class Beer {
    private long id;
    private String name;
    private String type;
    private String alcohol;
    private String manufacturer;
    private List<Characteristic> characteristics;
    private List<String> ingredients;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();

        result.append("-- Id: " + id + "\n");
        result.append("-- Name: " + name + "\n");
        result.append("-- Type: " + type + "\n");
        result.append("-- Alcohol: " + alcohol + "\n");
        result.append("-- Manufacturer: " + manufacturer + "\n");
        result.append("---- Ingredients:\n");

        for (String ingredient : ingredients) {
            result.append("---- " + ingredient + "\n");
        }

        result.append("---- Characteristics:\n");

        for (Characteristic characteristic : characteristics) {
            result.append(characteristic.toSourceString());
        }

        return result.toString();
    }
}

