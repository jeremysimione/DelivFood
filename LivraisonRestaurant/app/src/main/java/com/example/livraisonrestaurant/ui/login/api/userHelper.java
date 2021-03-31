package com.example.livraisonrestaurant.ui.login.api;

import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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
    public static Task<Void> updateIsRider(String uid, Boolean c) {
        return userHelper.getUsersCollection().document(uid).update("isRider", c);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return userHelper.getUsersCollection().document(uid).delete();
    }

}