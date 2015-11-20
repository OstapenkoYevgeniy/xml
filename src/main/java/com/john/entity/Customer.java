package com.john.entity;

public class Customer {
    private long id;
    private String name;
    private String lastname;
    private Address address;


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
