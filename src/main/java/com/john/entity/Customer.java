package com.john.entity;

import com.john.annotation.XmlElement;
import com.john.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
public class Customer {
    private long id;
    private String name;
    private String lastname;
    private String streetAddress;
    private String city;
    private String zip;

    @XmlElement(name = "customerId")
    public void setId(long id) {
        this.id = id;
    }

    @XmlElement(name = "customerName")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @XmlElement(name = "streetAddress")
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @XmlElement(name = "city")
    public void setCity(String city) {
        this.city = city;
    }

    @XmlElement(name = "zip")
    public void setZip(String zip) {
        this.zip = zip;
    }
}
