package com.slama.foodfacts.model;

import java.io.Serializable;

/**
 * Created by Dell on 14/11/2017.
 */

public class Product implements Serializable {

    private String code;
    private String name;
    private String picture;
    private String ingredients;
    private String energy;

    public Product(String code, String name, String picture, String ingredients, String energy) {
        this.code = code;
        this.name = name;
        this.picture = picture;
        this.ingredients = ingredients;
        this.energy = energy;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }
}
