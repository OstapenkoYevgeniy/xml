package com.john.entity;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    private int id;
    private List<String> lists = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public void addLists(String string) {
        lists.add(string);
    }
}
