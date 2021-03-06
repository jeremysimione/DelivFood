package com.example.livraisonrestaurant.ui.login.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class restaurant {
    private String uid;
    private String adress_uid;
    private String name;
    private Date created_at;
    private String admin_Uid;
    private ArrayList<String>  categorie;
    private String numTEl;

    public restaurant() {
    }



    public restaurant(String uid, String adress_uid, String name, String admin_Uid, ArrayList<String> c,String numTel) {
        this.uid = uid;
        this.adress_uid = adress_uid;
        this.name = name;
        this.categorie = c;
        this.numTEl=numTel;
        this.admin_Uid = admin_Uid;
    }

    public String getNumTEl() {
        return numTEl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdress_uid() {
        return adress_uid;
    }

    public void setAdress_uid(String adress_uid) {
        this.adress_uid = adress_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @ServerTimestamp
    public Date getCreated_at() {
        return created_at;
    }

    public String getAdmin_Uid() {
        return admin_Uid;
    }

    public void setAdmin_Uid(String admin_Uid) {
        this.admin_Uid = admin_Uid;
    }
    public ArrayList<String>  getCategorie() { return categorie; }

    public void setCategorie(ArrayList<String> categorie) { this.categorie = categorie; }


}
