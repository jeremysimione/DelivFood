package com.example.livraisonrestaurant.ui.login.api;

import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class orderHelper {
    private static final String COLLECTION_NAME = "orders";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getOrdersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createOrders(String rider_id, String restaurant_id, String client_Uid, int price, ArrayList<String> listProducts) {
        orders p = new orders(rider_id,  restaurant_id,  client_Uid,  price,  listProducts);


        return orderHelper.getOrdersCollection().document(p.getUid()).set(p);
    }
    public static Task<Void> createOrders1(orders p) {



        return orderHelper.getOrdersCollection().document(p.getUid()).set(p);
    }
    // --- GET ---

    public static Task<DocumentSnapshot> getOrders(String uid){
        return  orderHelper.getOrdersCollection().document(uid).get();
    }



    // --- UPDATE ---

    public static Task<Void> orderRider(String uid, String c) {
        return orderHelper.getOrdersCollection().document(uid).update("rider_id", c);
    }
    public static Task<Void> updateStatus(String uid, int c) {
        return orderHelper.getOrdersCollection().document(uid).update("status", c);
    }
    public static Task<Void> Addproduct(String uid, String c) {
        return orderHelper.getOrdersCollection().document(uid).update("listProducts", FieldValue.arrayUnion(c));
    }


    // --- DELETE ---

    public static Task<Void> deleteOrders(String uid) {
        return  orderHelper.getOrdersCollection().document(uid).delete();
    }

}


