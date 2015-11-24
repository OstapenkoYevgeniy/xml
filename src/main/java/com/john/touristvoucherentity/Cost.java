package com.john.touristvoucherentity;

public class Cost {
    private int price;
    private String currency;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();
        result.append("Price: " + price + "\n");
        result.append("Currency: " + currency + "\n");
        return result.toString();
    }
}
