package com.john.entity;

public class Customer {
    private long id;
    private String name;
    private String lastname;
    private Address address;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();

        result.append("-- Id: " + id + "\n");
        result.append("-- Name: " + name + "\n");
        result.append("-- Last name: " + lastname + "\n");
        result.append("---- Address:\n");
        result.append(address.toSourceString());

        return result.toString();
    }
}
