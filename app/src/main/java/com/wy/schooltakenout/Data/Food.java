package com.wy.schooltakenout.Data;

public class Food {
    private String foodName;
    private String storeName;
    private int foodImg;
    private double foodPrice;
    private int foodNum;

    public Food(String foodName, String storeName, int foodImg, double foodPrice, int foodNum) {
        this.foodName = foodName;
        this.storeName = storeName;
        this.foodImg = foodImg;
        this.foodPrice = foodPrice;
        this.foodNum = foodNum;
    }

    public String getFoodName() {
        return foodName;

    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(int foodImg) {
        this.foodImg = foodImg;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(int foodNum) {
        this.foodNum = foodNum;
    }
}
