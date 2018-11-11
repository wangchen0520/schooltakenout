package com.wy.schooltakenout.Data;

public class Goods {
    private int goodsID;
    private int sellerID;
    private String name;
    private double price;
    private int num;

    public Goods(int goodsID, int sellerID, String name, double price, int num) {
        this.goodsID = goodsID;
        this.sellerID = sellerID;
        this.name = name;
        this.price = price;
        this.num = num;
    }

    public Goods(int goodsID, int sellerID, String name, double price) {
        this.goodsID = goodsID;
        this.sellerID = sellerID;
        this.name = name;
        this.price = price;
    }

    public int getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(int goodsID) {
        this.goodsID = goodsID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
