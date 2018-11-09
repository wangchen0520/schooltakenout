package com.wy.schooltakenout.Data;

public class User {
    private int userID;
    private String username;
    private String userPhone;
    private String userPassword;

    public User(int userID, String username, String userPhone, String userPassword) {
        this.userID = userID;
        this.username = username;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
