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
import com.example.livraisonrestaurant.ui.login.MainActivity;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
        TextView nomRest = findViewById(R.id.textView25);
        TextView sTotal = findViewById(R.id.textView29);
        TextView total = findViewById(R.id.textView32);
        TextInputEditText adress = findViewById(R.id.adress);
        TextInputEditText num = findViewById(R.id.num);
        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(user.class);
                sTotal.setText(u.getOrder().getPrice()+"$");
                Double prix = u.getOrder().getPrice()+0.49+1;
                total.setText(prix + "$");
                if(u.getAdress()!=null){
                    adress.setText(u.getAdress());
                }
                if(u.getPhoneNumber() !=null){
                    num.setText(u.getPhoneNumber());
                }
                restHelper.getRestaurant(u.getOrder().getRestaurant_id()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        nomRest.setText(documentSnapshot.toObject(restaurant.class).getName());
                    }
                });

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
                        userHelper.updateAdress(FirebaseAuth.getInstance().getUid(),adress.getText().toString());
                        userHelper.updatePhoneNumbre(num.getText().toString(),FirebaseAuth.getInstance().getUid());

                        orderHelper.createOrders1(u.getOrder());
                        userHelper.updateorders2(FirebaseAuth.getInstance().getUid(),new orders(null,null,FirebaseAuth.getInstance().getUid(),0,new ArrayList<String>()));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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