package com.wy.schooltakenout.Data;

public class Orders {
    private int orderID;
    private int sellerID;
    private int userID;
    private double totalPrice;
    private String time;
    private String goodsID;
    private String goodsNum;

    public Orders(int orderID, int sellerID, int userID, double totalPrice, String time, String goodsID, String goodsNum) {
        this.orderID = orderID;
        this.sellerID = sellerID;
        this.userID = userID;
        this.totalPrice = totalPrice;
        this.time = time;
        this.goodsID = goodsID;
        this.goodsNum = goodsNum;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }
}
