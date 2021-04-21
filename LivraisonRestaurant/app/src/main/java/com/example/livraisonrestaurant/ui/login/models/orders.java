package com.example.livraisonrestaurant.ui.login.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class orders {
    private String uid;
    private String rider_id;
    private String restaurant_id;
    private String client_Uid;
    private int price;
    private ArrayList<String> listProducts;
    private int status; // 0 = En attente ;1 = prise en charge par le resto;2 = Prise en charghe par le livreur ; 3 = livrer
    private Date created_at;

    public orders(){

    }

    public orders(String rider_id, String restaurant_id, String client_Uid, int price, ArrayList<String> listProducts) {
        this.rider_id = rider_id;
        this.restaurant_id = restaurant_id;
        this.client_Uid = client_Uid;
        this.price = price;
        this.listProducts = listProducts;
       this.uid= UUID.randomUUID().toString();
       this.status=0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }
    @ServerTimestamp
    public Date getCreated_at() {
        return created_at;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getClient_Uid() {
        return client_Uid;
    }

    public void setClient_Uid(String client_Uid) {
        this.client_Uid = client_Uid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getListProducts() {
        return listProducts;
    }

    public void setListProducts(ArrayList<String> listProducts) {
        this.listProducts = listProducts;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
