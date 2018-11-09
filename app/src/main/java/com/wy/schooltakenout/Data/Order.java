package com.wy.schooltakenout.Data;

public class Order {
    private int orderID;
    private String foodName;
    private int foodImage;
    private String clientName;
    private String clientPhone;

    public Order(int orderID, String foodName, int foodImage, String clientName, String clientPhone) {
        this.orderID = orderID;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(int foodImage) {
        this.foodImage = foodImage;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
}
