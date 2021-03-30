package com.example.livraisonrestaurant.ui.login.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class client extends BaseActivity {



    @Override
    public int getFragmentLayout() {
        return R.layout.activity_client;
    }
}