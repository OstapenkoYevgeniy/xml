package com.john.touristvoucherentity;

public class Voucher {
    private long id;
    private String typeoftour;
    private String country;
    private String dayandnightquantity;
    private String transport;
    private Hotel hotel;
    private Cost cost;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();
        result.append("Id: " + id + "\n");
        result.append("TypeOfTour: " + typeoftour + "\n");
        result.append("Country: " + country + "\n");
        result.append("DayAndNightQuantity: " + dayandnightquantity + "\n");
        result.append("Transport: " + transport + "\n");
        result.append("Hotel: " + hotel.toSourceString());
        result.append("Cost: " + cost.toSourceString());
        return result.toString();
    }
}
