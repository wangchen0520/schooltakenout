package com.wy.schooltakenout.Data;

import java.util.List;

//主界面商店的类，含商店名、商店图片与标签
public class Store {
	private int storeID;
	private String storeName;
	private int storeImg;
	private List<String> storeTags;
	private int storeFoodNum;
	private double storeFee;

	public Store(int storeID, String storeName, int storeImg, List<String> storeTags, int storeFoodNum, double storeFee) {
	    this.storeID = storeID;
		this.storeName = storeName;
		this.storeImg = storeImg;
		this.storeTags = storeTags;
		this.storeFoodNum = storeFoodNum;
		this.storeFee = storeFee;
	}

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getStoreImg() {
		return storeImg;
	}

	public void setStoreImg(int storeImg) {
		this.storeImg = storeImg;
	}

	public List<String> getStoreTags() {
		return storeTags;
	}

	public void setStoreTags(List<String> storeTags) {
		this.storeTags = storeTags;
	}

    public int getStoreFoodNum() {
        return storeFoodNum;
    }

    public void setStoreFoodNum(int storeFoodNum) {
        this.storeFoodNum = storeFoodNum;
    }

    public double getStoreFee() {
        return storeFee;
    }

    public void setStoreFee(double storeFee) {
        this.storeFee = storeFee;
    }
}
