package com.wy.schooltakenout.Data;

import java.util.List;

//主界面商店的类，含商店名、商店图片与标签
public class Seller {
	private int sellerID;
	private String name;
	private String address;
	private double account;

	public Seller(int sellerID, String name, String address, double account) {
		this.sellerID = sellerID;
		this.name = name;
		this.address = address;
		this.account = account;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}
}
