package com.john.entity;

public class Characteristic {
    private String description;
    private String value;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();

        if (value != null) {
            result.append("---- " + description + ": " + value + "\n");
        } else {
            result.append("---- " + description + "\n");
        }

        return result.toString();
    }
}
