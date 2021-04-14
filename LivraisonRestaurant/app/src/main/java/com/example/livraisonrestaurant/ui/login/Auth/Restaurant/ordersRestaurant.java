package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class ordersRestaurant extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_restaurant);
        LinearLayout myScrollView = findViewById(R.id.linScrollView1);
        Button bt1 = (Button) findViewById(R.id.button2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String uid = getCurrentUser().getUid();
        productHelper.getProductsCollection().whereEqualTo("restaurant_id",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });

    }

    @Override
    public int getFragmentLayout() {
        return 0;
    }
}