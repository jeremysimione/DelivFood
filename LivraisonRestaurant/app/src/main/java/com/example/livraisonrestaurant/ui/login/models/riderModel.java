package com.example.livraisonrestaurant.ui.login.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class riderModel {
    private String uid;
    private Date created_at;
    private Boolean enLigne;

    public riderModel() {
    }



    public riderModel(String uid) {
        this.uid = uid;
        this.enLigne=false;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getEnLigne() {
        return enLigne;
    }

    public void setEnLigne(Boolean enLigne) {
        this.enLigne = enLigne;
    }

    @ServerTimestamp
    public Date getCreated_at() {
        return created_at;
    }



}