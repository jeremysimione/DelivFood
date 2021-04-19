package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.UUID;
import java.util.jar.Attributes;

public class ajoute_products extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajoute_products);
        String uid = getCurrentUser().getUid();
        EditText name =  findViewById(R.id.name);
        EditText price =  findViewById(R.id.Price);
        Button Valid = (Button) findViewById(R.id.Validate);
        String ownerId = UUID.randomUUID().toString();
        Valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = (String) name.getText().toString();
                int P = (int) Integer.parseInt(price.getText().toString());
                productHelper.createProduct(ownerId,n,uid,P,true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), ordersRestaurant.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_ajoute_products;
    }
}