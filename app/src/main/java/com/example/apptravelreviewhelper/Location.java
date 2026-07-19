package com.example.apptravelreviewhelper;

public class Location {
    private String id;
    private boolean isSaved;
    private String name;
    private String address;
    private String description;
    private double rating;
    private String imageUrl;

    public Location(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location(String name, String address, String description, double rating, String imageUrl){
        this.name=name;
        this.address=address;
        this.description=description;
        this.rating=rating;
        this.imageUrl=imageUrl;
    }

}