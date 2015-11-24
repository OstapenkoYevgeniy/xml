package com.john.touristvoucherentity;

public class Hotel {
    private int hotelstars;
    private String food;
    private int room;
    private String tv;
    private String airconditioner;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();
        result.append("HotelStars: " + hotelstars + "\n");
        result.append("Food: " + food + "\n");
        result.append("Room: " + room + "\n");
        result.append("TV: " + tv + "\n");
        result.append("AirConditioner: " + airconditioner + "\n");
        return result.toString();
    }
}
