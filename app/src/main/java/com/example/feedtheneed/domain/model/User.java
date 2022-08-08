package com.example.feedtheneed.domain.model;


import com.google.android.gms.maps.model.LatLng;

public class User {
    private String userId;
    private String userFullName;
    private String userEmail;
    private boolean isRestaurant;
    private String userLat;
    private String userLong;

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLong() {
        return userLong;
    }

    public void setUserLong(String userLong) {
        this.userLong = userLong;
    }

    public boolean isRestaurant() {
        return isRestaurant;
    }

    public void setRestaurant(boolean restaurant) {
        isRestaurant = restaurant;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public User(){}
    public User(String userFullName, String userEmail) {
        this.userFullName = userFullName;
        this.userEmail = userEmail;
    }

    public User(String userId, String userFullName, String userEmail, boolean isRestaurant) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.isRestaurant = isRestaurant;
    }

    public User(String userId, String userFullName, String userEmail, boolean isRestaurant, String userLat, String userLong) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.isRestaurant = isRestaurant;
        this.userLat = userLat;
        this.userLong = userLong;
    }
}
