package com.john.entity;

import java.util.List;

public class Order {
    private long id;
    private Customer customer;
    private List<Beer> beers;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();

        result.append("Order:\nId: " + id + "\n");
        result.append("-- Customer: \n");
        result.append(customer.toSourceString());
        result.append("-- Beers:\n");

        for (Beer beer : beers) {
            result.append(beer.toSourceString());
            result.append("-- *********\n");
        }

        return result.toString();
    }
}

