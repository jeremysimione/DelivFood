package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.MainActivity;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<RowItem> myRowItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myRowItems = new ArrayList<RowItem>();
        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ((TextView)findViewById(R.id.textView4)).setText(documentSnapshot.toObject(user.class).getUsername());
            }
        });
        Button deco = findViewById(R.id.button3);
        deco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });

        Button modify = findViewById(R.id.button4);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), SettingsChangeActivity.class);
                startActivity(intent3);
            }
        });
        myListView = (ListView) findViewById(R.id.listviewhome);

        fillArrayList();

        CustomAdapter myAdapter = new CustomAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter( myAdapter );
    }

    private void fillArrayList() {

        RowItem row_one = new RowItem( );
        row_one.setHeading("Domicile \n\nAjouter une adresse de domicile");

        row_one.setSmallImageName( R.drawable.maison);

        myRowItems.add( row_one );

        RowItem row_two = new RowItem( );
        row_two.setHeading("Travail");
        row_two.setSmallImageName( R.drawable.mallette);
        myRowItems.add( row_two);

    }
}