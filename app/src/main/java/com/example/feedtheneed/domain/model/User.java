package com.example.feedtheneed.domain.model;


import com.google.android.gms.maps.model.LatLng;

public class User {
    private String userId;
    private String userFullName;
    private String userEmail;
    private boolean isRestaurant;
    private LatLng userLatLng;

    public LatLng getUserLatLng() {
        return userLatLng;
    }

    public void setUserLatLng(LatLng userLatLng) {
        this.userLatLng = userLatLng;
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

    public User(String userId, String userFullName, String userEmail, boolean isRestaurant, LatLng userLatLng) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.isRestaurant = isRestaurant;
        this.userLatLng = userLatLng;
    }
}
