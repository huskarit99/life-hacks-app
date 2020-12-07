package com.example.lifehack.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private String category_name;
    private String category_image;
    private int number_of_hacks;
    private ArrayList<String> hacks;

    public Category() {
        this.category_name = "";
        this.category_image = "";
        this.number_of_hacks = 0;
        hacks = new ArrayList<String>();
    }

    public Category(String category_name, String category_image, ArrayList<String> hacks) {
        this.category_name = category_name;
        this.category_image = category_image;
        this.hacks = new ArrayList<>(hacks);
        this.number_of_hacks = this.hacks.size();
    }

    public int getNumber_of_hacks() {
        return number_of_hacks;
    }

    public String getCategory_image() {
        return category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public ArrayList<String> getHacks() {
        return hacks;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setHacks(ArrayList<String> hacks) {
        this.hacks = new ArrayList<>(hacks);
        this.number_of_hacks = this.hacks.size();
    }
}
