package com.john.entity;

public class Address {
    private String street;
    private String city;
    private String zip;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();

        result.append("---- Street: " + street +"\n");
        result.append("---- City: " + city +"\n");
        result.append("---- Zip: " + zip +"\n");

        return result.toString();
    }
}
