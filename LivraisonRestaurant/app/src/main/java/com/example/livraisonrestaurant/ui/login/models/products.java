package com.example.livraisonrestaurant.ui.login.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class products {
    private String uid;
    private String name;
    private String restaurant_id;
    private int price;
    private boolean status;
    private Date created_at;

    public products() {
    }

    public products(String uid, String name, String restaurant_id, int price, boolean status) {
        this.uid = uid;
        this.name = name;
        this.restaurant_id = restaurant_id;
        this.price = price;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public int getPrice() {
        return price;
    }

    public boolean isStatus() {
        return status;
    }
    @ServerTimestamp
    public Date getCreated_at() {
        return created_at;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
