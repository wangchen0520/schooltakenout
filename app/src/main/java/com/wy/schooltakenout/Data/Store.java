package com.wy.schooltakenout.Data;

import java.util.List;

//主界面商店的类，含商店名、商店图片与标签
public class Store {
	private String storeName;
	private int storeImg;
	private List<String> storeTags;

	public Store(String storeName, int storeImg, List<String> storeTags) {
		this.storeName = storeName;
		this.storeImg = storeImg;
		this.storeTags = storeTags;
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
}
