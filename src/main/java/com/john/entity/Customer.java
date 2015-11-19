package com.john.entity;

import com.john.annotation.XmlElement;
import com.john.annotation.XmlRootElement;

public class Customer {
    private long id;
    private String name;
    private String lastname;
    private Address address;
//    private String streetAddress;
//    private String city;
//    private String zip;

    public void setId(long id) {
        this.id = id;
    }

        public void setName(String name) {
        this.name = name;
    }
//
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    //    public void setStreetAddress(String streetAddress) {
//        this.streetAddress = streetAddress;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public void setZip(String zip) {
//        this.zip = zip;
//    }
//
    public long getId() {
        return id;
    }

    public String toSourceString() {
        String result = "id: " + id + "\n";
        result += "name: " + name + "\n";
        result += "lastname: " + lastname + "\n";
        result += address.toSourceString();
        return result;
    }
}
