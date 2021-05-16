package com.example.livraisonrestaurant.ui.login.api;

import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.riderModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class riderHelper {
    private static final String COLLECTION_NAME = "rider";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRiderCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createRider(String uid) {
        riderModel restaurantToCreate = new riderModel(uid);


        return riderHelper.getRiderCollection().document(uid).set(restaurantToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRider(String uid){
        return  riderHelper.getRiderCollection().document(uid).get();
    }


    // --- UPDATE ---

    public static Task<Void> updateEnligne(Boolean c, String uid) {
        return  riderHelper.getRiderCollection().document(uid).update("enLigne", c);
    }



    // --- DELETE ---

    public static Task<Void> deleteRider(String uid) {
        return  riderHelper.getRiderCollection().document(uid).delete();
    }

}
