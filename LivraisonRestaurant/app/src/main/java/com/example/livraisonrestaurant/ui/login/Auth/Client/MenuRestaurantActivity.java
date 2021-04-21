package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuRestaurantActivity extends AppCompatActivity {
    ArrayList<products> produits = new ArrayList<products>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Context context = this;
        String uid = i.getStringExtra("id_resto");
        setContentView(R.layout.activity_menu_restaurant);
        LinearLayout myScrollView = findViewById(R.id.linScrollView6);

        productHelper.getProductsCollection().whereEqualTo("restaurant_id",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("test", document.getId() + " => " + document.getData());
                        products produit = document.toObject(products.class);
                        produits.add(produit);
                    }
                }
                for (products p : produits){
                    LinearLayout parent = new LinearLayout(context);
                    MaterialCardView m = new MaterialCardView(context);
                    m.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    m.setClickable(true);
                    m.setFocusable(true);
                    m.setCheckable(true);

                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.VERTICAL);


                    LinearLayout layout2 = new LinearLayout(context);
                    layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout2.setOrientation(LinearLayout.VERTICAL);
                    parent.addView(layout2);
                    TextView tv1 = new TextView(context);
                    tv1.setText(p.getName()+"           "+p.getPrice()+"$");
                    TextView tv2 = new TextView(context);
                    layout2.addView(tv1);
                    layout2.addView(tv2);
                    m.addView(parent);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(0, 20, 0, 0);
                    myScrollView.addView(m, layoutParams);
                }
            }
        });
    }
}