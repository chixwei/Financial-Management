package com.example.financialmanagement;

public class DataModel {

    //Model Class
    String name;
    String imageUrl;

    //Constructors
    public DataModel() {

    }

    public DataModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
