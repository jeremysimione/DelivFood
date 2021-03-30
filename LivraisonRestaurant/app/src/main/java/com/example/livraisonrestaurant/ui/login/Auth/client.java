package com.example.livraisonrestaurant.ui.login.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class client extends AppCompatActivity {
    Context context = this;
    ArrayList<restaurant> restaurants = new ArrayList<restaurant>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        LinearLayout myScrollView = findViewById(R.id.linScrollView);
        restHelper.getRestaurantCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("test", document.getId() + " => " + document.getData());
                                restaurant resto = document.toObject(restaurant.class);
                                restaurants.add(resto);
                                System.out.println("Dans le oncomplete  " +
                                        restaurants.get(0).getName() );
                            }
                        } else {
                            Log.d("test", "Error getting documents: ", task.getException());
                        }

                        for(restaurant resto : restaurants) {
                            System.out.println("Dans la liste des restaurants "+ resto.getName());
                            MaterialCardView m = new MaterialCardView(getApplicationContext());
                            m.setClickable(true);
                            m.setFocusable(true);
                            m.setCheckable(true);
                            LinearLayout parent = new LinearLayout(getApplicationContext());

                            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            parent.setOrientation(LinearLayout.VERTICAL);

                            ImageView iv = new ImageView(getApplicationContext());
                            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            iv.setImageResource(R.drawable.home);
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            LinearLayout layout2 = new LinearLayout(getApplicationContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout2.setOrientation(LinearLayout.VERTICAL);
                            parent.addView(iv);
                            parent.addView(layout2);
                            TextView tv1 = new TextView(getApplicationContext());
                            assert resto != null;
                            tv1.setText(resto.getName());
                            TextView tv2 = new TextView(getApplicationContext());
                            layout2.addView(tv1);
                            layout2.addView(tv2);
                            m.addView(parent);
                            myScrollView.addView(m);
                        }
                    }
                });

        System.out.println("reastaurants : "  + restaurants);

    }

        }