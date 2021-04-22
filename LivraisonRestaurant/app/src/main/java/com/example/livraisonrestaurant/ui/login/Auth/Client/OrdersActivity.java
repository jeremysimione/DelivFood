package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.LinkAddress;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class OrdersActivity extends AppCompatActivity {
    private BottomSheetDialog mBottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mBottomSheetDialog = new BottomSheetDialog(OrdersActivity.this);

        View bottomSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialog, (LinearLayout)findViewById(R.id.bottomSheetContainer));
                            TextView test = findViewById(R.id.tv_title);
                            (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBottomSheetDialog.dismiss();
                                }
                            });
                            (bottomSheetLayout.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "Ok button clicked", Toast.LENGTH_SHORT).show();
                                }
                            });


                            mBottomSheetDialog.setContentView(bottomSheetLayout);
                            mBottomSheetDialog.show();

                    }




}
