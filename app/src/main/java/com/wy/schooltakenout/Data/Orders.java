package com.wy.schooltakenout.Data;

import java.sql.Date;

public class Orders {
    private int orderID;
    private int sellerID;
    private int userID;
    private double totalPrice;
    private Date time;
    private int goodsID;
    private int goodsNum;

    public Orders(int orderID, int sellerID, int userID, double totalPrice, Date time, int goodsID, int goodsNum) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(int goodsID) {
        this.goodsID = goodsID;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }
}
