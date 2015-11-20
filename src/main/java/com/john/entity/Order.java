package com.john.entity;

import java.util.ArrayList;
import java.util.List;

//@XmlRootElement(name = "order")
//@XmlType(propOrder = {"id","customers","beers"})
public class Order {
    private long id;
    private Customer customer;
    private List<Beer> beers;


    public Order() {
        beers = new ArrayList<>();
    }



    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addBeer(Beer beer) {
        beers.add(beer);
    }

    public String toSourceString() {
        String result = "id: " + id + "\n";
        result += "Customer --\n";
        result += customer.toSourceString() + "\n";


        result += "Beers:\n";


        for (Beer beer : beers) {
            result += beer.toSourceString();
        }

        return result;
    }


    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Beer> getBeers() {
        return beers;
    }
}
