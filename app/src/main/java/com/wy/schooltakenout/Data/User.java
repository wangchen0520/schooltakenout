package com.wy.schooltakenout.Data;

public class User {
    private int userID;
    private String name;
    private String phone;
    private String passWord;

    public User(int userID, String name, String phone, String passWord) {
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.passWord = passWord;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
