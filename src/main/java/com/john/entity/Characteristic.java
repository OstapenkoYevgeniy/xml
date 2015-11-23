package com.john.entity;

public class Characteristic {
    private String description;
    private String value;

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }


    public String toSourceString() {
        String result = "Характеристики: \n";
        result += description + "\n";
        result += value + "\n";
        return result;
    }
}
