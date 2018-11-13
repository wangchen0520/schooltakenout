package com.wy.schooltakenout.Data;

public class OrderItem {
    private int userID;
    private String time;

    public OrderItem(int userID, String time) {
        this.userID = userID;
        this.time = time;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
