package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;

public class ajoute_products extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_products);
        String uid = getCurrentUser().getUid();
    }

    @Override
    public int getFragmentLayout() {
        return 0;
    }
}