package com.example.apptravelreviewhelper;

public class Bill {
    private String location;
    private String checkIn;
    private int nights;
    private int adults;
    private String roomType;
    private int roomCount;
    private String paymentMethod;
    private String totalPrice;
    private long timestamp; // thời điểm đặt, dùng để sắp xếp mới nhất lên đầu

    public Bill() {
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

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCheckIn() { return checkIn; }
    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }

    public int getNights() { return nights; }
    public void setNights(int nights) { this.nights = nights; }

    public int getAdults() { return adults; }
    public void setAdults(int adults) { this.adults = adults; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getRoomCount() { return roomCount; }
    public void setRoomCount(int roomCount) { this.roomCount = roomCount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}