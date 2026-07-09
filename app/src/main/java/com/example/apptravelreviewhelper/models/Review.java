package com.example.apptravelreviewhelper.models;

public class Review {
    private String userEmail;
    private float rating;
    private String comment;
    private long timestamp;

    public Review() {
    }

    public Review(String userEmail, float rating, String comment, long timestamp) {
        this.userEmail = userEmail;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    // CÁC HÀM GETTER ĐỂ LẤY DỮ LIỆU
    public String getUserEmail() {
        return userEmail;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public long getTimestamp() {
        return timestamp;
    }
}