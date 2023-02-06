package com.graduation.mah.weather.model;


public class EGModel {

    String title;

    String description;


    String date;

    String category;
    int image;

    public EGModel(String title, String description, String date, String category, int image) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    EGModel() {
    }

    public EGModel(String title, String description, String date, String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
