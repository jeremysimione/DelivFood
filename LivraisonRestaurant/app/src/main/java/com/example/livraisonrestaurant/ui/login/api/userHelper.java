package com.example.livraisonrestaurant.ui.login.api;

import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;


public class userHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username) {
        user userToCreate = new user(uid, username);

        return userHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return userHelper.getUsersCollection().document(uid).get();
    }


    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return userHelper.getUsersCollection().document(uid).update("username", username);
    }
    public static Task<Void> updatePhoneNumbre(String phoneNumber, String uid) {
        return userHelper.getUsersCollection().document(uid).update("phoneNumber", phoneNumber);
    }

    public static Task<Void> updateIsCustomer(String uid, Boolean c) {
        return userHelper.getUsersCollection().document(uid).update("isCustomer", c);
    }
    public static Task<Void> updateIsRest(String uid, Boolean c) {
        return userHelper.getUsersCollection().document(uid).update("isRest", c);
    }
    public static Task<Void> updateAdress(String uid, String c) {
        return userHelper.getUsersCollection().document(uid).update("adressCode", c);
    }
    public static Task<Void> updateIsRider(String uid, Boolean c) {
        return userHelper.getUsersCollection().document(uid).update("isRider", c);
    }
    public static Task<Void> updateorders(String uid, String c) {
        return userHelper.getUsersCollection().document(uid).update("orders.restaurant_id", c);
    }
    public static Task<Void> updateorders2(String uid, orders c) {
        return userHelper.getUsersCollection().document(uid).update("orders", c);
    }
    public static Task<Void> addProducts(String uid, String c,int p) {
        userHelper.getUsersCollection().document(uid).update("orders.price",FieldValue.increment(p));
        userHelper.getUsersCollection().document(uid).update("orders.nbDeProduits",FieldValue.increment(1));
        return userHelper.getUsersCollection().document(uid).update("orders.listProducts", FieldValue.arrayUnion(c));
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return userHelper.getUsersCollection().document(uid).delete();
    }

}