package com.example.livraisonrestaurant.ui.login.models;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class  user {

    private String uid;
    private String username;
    @Nullable

    private Date dateCreated;
    private Boolean isCustomer;
    @Nullable
    public String adressCode;
    @Nullable
    public String phoneNumber;
    public Boolean isRest;
    public Boolean isRider;
    public orders orders;


    public user() { }

    public user(String uid, String username) {
        this.uid = uid;
        this.username = username;
        //Test activité client
        this.isRest=false;
        this.isRider=false;
        this.isCustomer = false;
        this.orders = new orders(null,null,this.uid,0,new ArrayList<String>());
    }

    public user(user user1) {
        this.uid= user1.getUid();
        this.username = user1.getUsername();
        //Test activité client
        this.isRest=user1.getIsRest();
        this.isRider=user1.getIsRider();
        this.isCustomer = user1.getIsCustomer();
    }


    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }

    public com.example.livraisonrestaurant.ui.login.models.orders getOrder() {
        return orders;
    }

    public void setOrders(com.example.livraisonrestaurant.ui.login.models.orders order) {
        this.orders = order;
    }

    public Boolean getIsCustomer() { return isCustomer; }
    public Boolean getIsRest() { return isRest; }
    public Boolean getIsRider() { return isRider; }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setIsMentor(Boolean customer) { isCustomer = customer; }

    public String getAdress() {
        return adressCode;
    }
}