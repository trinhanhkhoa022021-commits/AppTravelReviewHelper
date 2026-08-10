package com.example.apptravelreviewhelper;

import com.google.firebase.firestore.PropertyName;

public class Bill {
    private String location;
    private String checkIn;
    private int nights;
    private int adults;
    private String roomType;
    private int roomCount;
    private String paymentMethod;
    private String totalPrice;
    private long timestamp;

    public Bill() {
        // Required empty constructor for Firestore
    }

    public Bill(String location, String checkIn, int nights, int adults,
                String roomType, int roomCount, String paymentMethod,
                String totalPrice, long timestamp) {
        this.location = location;
        this.checkIn = checkIn;
        this.nights = nights;
        this.adults = adults;
        this.roomType = roomType;
        this.roomCount = roomCount;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    @PropertyName("location")
    public String getLocation() { return location; }
    @PropertyName("location")
    public void setLocation(String location) { this.location = location; }

    @PropertyName("checkIn")
    public String getCheckIn() { return checkIn; }
    @PropertyName("checkIn")
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }

    @PropertyName("nights")
    public int getNights() { return nights; }
    @PropertyName("nights")
    public void setNights(int nights) { this.nights = nights; }

    @PropertyName("adults")
    public int getAdults() { return adults; }
    @PropertyName("adults")
    public void setAdults(int adults) { this.adults = adults; }

    @PropertyName("roomType")
    public String getRoomType() { return roomType; }
    @PropertyName("roomType")
    public void setRoomType(String roomType) { this.roomType = roomType; }

    @PropertyName("roomCount")
    public int getRoomCount() { return roomCount; }
    @PropertyName("roomCount")
    public void setRoomCount(int roomCount) { this.roomCount = roomCount; }

    @PropertyName("paymentMethod")
    public String getPaymentMethod() { return paymentMethod; }
    @PropertyName("paymentMethod")
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    @PropertyName("totalPrice")
    public String getTotalPrice() { return totalPrice; }
    @PropertyName("totalPrice")
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    @PropertyName("timestamp")
    public long getTimestamp() { return timestamp; }
    @PropertyName("timestamp")
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
