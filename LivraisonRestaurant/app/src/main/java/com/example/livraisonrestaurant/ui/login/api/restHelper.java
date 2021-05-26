package com.example.livraisonrestaurant.ui.login.api;

import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class restHelper {

        private static final String COLLECTION_NAME = "restaurants";

        // --- COLLECTION REFERENCE ---

        public static CollectionReference getRestaurantCollection(){
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        }

        // --- CREATE ---

        public static Task<Void> createRestaurant(String uid, String adress_uid, String name, String admin_Uid, ArrayList<String> ca, String num) {
            restaurant restaurantToCreate = new restaurant(uid, adress_uid,name,admin_Uid,ca,num);


            return restHelper.getRestaurantCollection().document(uid).set(restaurantToCreate);
        }

        // --- GET ---

        public static Task<DocumentSnapshot> getRestaurant(String uid){
            return  restHelper.getRestaurantCollection().document(uid).get();
        }


        // --- UPDATE ---

        public static Task<Void> updateRestName(String name, String uid) {
            return  restHelper.getRestaurantCollection().document(uid).update("name", name);
        }



        // --- DELETE ---

        public static Task<Void> deleteRestaurant(String uid) {
            return  restHelper.getRestaurantCollection().document(uid).delete();
        }

    }
