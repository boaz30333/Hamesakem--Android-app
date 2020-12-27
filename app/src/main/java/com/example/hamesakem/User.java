package com.example.hamesakem;

import java.io.Serializable;

public class User implements Serializable {
//    private String UserID;
    public String fName;
    public String email;
    public String phone;
//    private String type;

    public User() {
    }

//    public User(String userID, String fName, String email, String phone, String type) {
//        UserID = userID;
//        this.fName = fName;
//        Email = email;
//        phone = phone;
//        this.type = type;
//    }

//    public String getUserID() {
//        return UserID;
//    }
//
//    public void setUserID(String userID) {
//        UserID = userID;
//    }

    public String getName() {
        return fName;
    }

    public void setName(String name) {
        this.fName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}
