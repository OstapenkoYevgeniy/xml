package com.john.entity;

public class Address {
    private String street;
    private String city;
    private String zip;

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String toSourceString() {
        String result = "street: " + street +"\n";
        result += "city: " + city +"\n";
        result += "zip: " + zip +"\n";
        return result;
    }
}
