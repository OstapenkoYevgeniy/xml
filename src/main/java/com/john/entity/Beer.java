package com.john.entity;


import java.util.ArrayList;
import java.util.List;

public class Beer {
    private String name;
    private String type;
    private String alcohol; // TODO как тут хранить значения вложенных классов
    private String manufacturer;
    private String filtered;
    private String material;
    private List<String> ingredients;
    private int nutritionFacts;
    private int transparent;
    private float volume;
    private float abv;

    public Beer() {
        ingredients = new ArrayList<>();
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setNutritionFacts(int nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }

    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    public void setTransparent(int transparent) {
        this.transparent = transparent;
    }

    public void setAbv(float abv) {
        this.abv = abv;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public String toSourceString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name + " " + type + " " + filtered + " " + alcohol + ". " + manufacturer + "\n");
        builder.append("Ингридиенты: ");
        for (String ingredient : ingredients) {
            builder.append(ingredient + "; ");
        }
        builder.append("\nАлкоголь: " + abv + "%");
        builder.append("\nПрозрачность: " + transparent + "%");
        builder.append("\nПищевая ценность: " + nutritionFacts + "ккал.");
        builder.append("\n" + material + " " + volume + " л.");
        return builder.toString();
    }

    public class Type {
        public static final String ALCOHOL = "Алкогольное";
        public static final String ALCOHOL_FREE = "Безалкогольное";
    }
}
