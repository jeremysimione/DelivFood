package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.Restaurant.restaurantActivity;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.google.firebase.auth.FirebaseAuth;

public class devenirRest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devenir_rest);
        EditText nom = findViewById(R.id.nom);
        EditText num = findViewById(R.id.num);
        EditText adress = findViewById(R.id.adress);
        EditText catg = findViewById(R.id.catg);
        Button valider = findViewById(R.id.outlinedButton);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restHelper.createRestaurant(FirebaseAuth.getInstance().getUid(),String.valueOf(adress.getText()),String.valueOf(nom.getText()),FirebaseAuth.getInstance().getUid(),String.valueOf(catg.getText()),String.valueOf(num.getText()));
                userHelper.updateIsRest(FirebaseAuth.getInstance().getUid(),true);
                setContentView(R.layout.activity_main);
                new Handler().postDelayed(null,6000);
                Intent intent = new Intent(getApplicationContext(), restaurantActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}