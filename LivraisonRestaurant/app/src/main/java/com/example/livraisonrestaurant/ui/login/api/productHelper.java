package com.example.livraisonrestaurant.ui.login.api;

import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class productHelper {
    private static final String COLLECTION_NAME = "products";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getProductsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createProduct(String uid, String name, String restaurant_id, int price, boolean status) {
        products p = new products(uid,name,restaurant_id,price,status);


        return productHelper.getProductsCollection().document(uid).set(p);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getProduct(String uid){
        return  productHelper.getProductsCollection().document(uid).get();
    }


    // --- UPDATE ---





    // --- DELETE ---

    public static Task<Void> deleteProduct(String uid) {
        return  productHelper.getProductsCollection().document(uid).delete();
    }

}
