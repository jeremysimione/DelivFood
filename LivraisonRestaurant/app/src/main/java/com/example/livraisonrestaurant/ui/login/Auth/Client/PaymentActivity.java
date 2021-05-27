package com.example.livraisonrestaurant.ui.login.Auth.Client;

import android.content.Intent;
import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.Restaurant.restaurantActivity;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.EventListener;

public class PaymentActivity extends AppCompatActivity {

    private TextView mTextView;
    user u;

    @Override
    protected void onResume() {
        super.onResume();
        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(user.class);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(user.class);
            }
        });

        mTextView = (TextView) findViewById(R.id.text);
        Button Valider = findViewById(R.id.button6);

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        u = documentSnapshot.toObject(user.class);
                        orderHelper.createOrders1(u.getOrder());
                        userHelper.updateorders2(FirebaseAuth.getInstance().getUid(),new orders(null,null,FirebaseAuth.getInstance().getUid(),0,new ArrayList<String>()));
                        Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        // Enables Always-on
        //setAmbientEnabled();
    }
}