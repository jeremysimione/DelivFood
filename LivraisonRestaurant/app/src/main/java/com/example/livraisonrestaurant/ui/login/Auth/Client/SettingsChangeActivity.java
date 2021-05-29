package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class SettingsChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change);
        EditText username = findViewById(R.id.editTextTextPersonName);
        EditText num = findViewById(R.id.editTextPhone);
        EditText Adres = findViewById(R.id.editTextTextEmailAddress);
        Button b = findViewById(R.id.button7);
        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.toObject(user.class).getUsername());
                num.setText(documentSnapshot.toObject(user.class).getPhoneNumber());

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userHelper.updateUsername(FirebaseAuth.getInstance().getUid(),username.getText().toString());
                userHelper.updatePhoneNumbre(num.getText().toString(),FirebaseAuth.getInstance().getUid());
                Toast.makeText(getApplicationContext(), "MAJ prise en compte", Toast.LENGTH_SHORT).show();

            }
        });


    }
}