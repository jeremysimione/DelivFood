package com.example.livraisonrestaurant.ui.login.Auth.Client;

import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;

public class PaymentActivity extends AccountActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }
}