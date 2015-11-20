package com.john.entity;

public class Beer {
    private String name;
    private String type;
    private Alcohol alcoholType;

    private String manufacturer;
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

    public String getType() {
        return type;
    }

    public String toSourceString() {
        String result = "name: " + name + "\n";
//        result += "type: " + type + "\n";
        return result;
    }

    public enum Alcohol {
        ALCOHOL, NO_ALCOHOL;
    }
}

