package com.example.livraisonrestaurant.ui.login.models;

import com.google.firebase.firestore.ServerTimestamp;

import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class  user {

    private String uid;
    private String username;
    @Nullable

    private Date dateCreated;
    private Boolean isMentor;
    @Nullable
    public String adressCode;
    @Nullable
    public String phoneNumber;
    public Boolean isRest;
    public Boolean isRider;


    public user() { }

    public user(String uid, String username) {
        this.uid = uid;
        this.username = username;
        this.isRest=false;
        this.isRider=false;
        this.isMentor = false;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }

    public Boolean getIsMentor() { return isMentor; }
    public Boolean getIsRest() { return isRest; }
    public Boolean getIsRider() { return isRider; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setIsMentor(Boolean mentor) { isMentor = mentor; }

}