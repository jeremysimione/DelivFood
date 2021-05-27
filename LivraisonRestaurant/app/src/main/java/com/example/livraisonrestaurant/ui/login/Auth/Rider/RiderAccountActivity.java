package com.example.livraisonrestaurant.ui.login.Auth.Rider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.MainActivity;
import com.example.livraisonrestaurant.ui.login.RiderCustomerAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RiderAccountActivity extends AppCompatActivity {
    ListView myListView;
    ListView myListView1;

    ArrayList<RowItem> myRowItems;
    ArrayList<RowItem> myRowItems1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_account);
        myRowItems = new ArrayList<RowItem>();
        myRowItems1 = new ArrayList<RowItem>();
        myListView = (ListView) findViewById(R.id.lv2);

        myListView1 = (ListView) findViewById(R.id.lv1);

        RowItem row_one = new RowItem();
        row_one.setSubHeading("Déconnexion");


        myRowItems.add(row_one);

        fillArrayList();
        CustomAdapter myAdapter1 = new CustomAdapter(getApplicationContext(), myRowItems1);
        myListView1.setAdapter(myAdapter1);
        CustomAdapter myAdapter = new CustomAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter(myAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void fillArrayList() {
        RowItem r2 = new RowItem();
        r2.setHeading("Documents");
        r2.setSmallImageName(R.drawable.baseline_description_black_24dp);
        myRowItems1.add(r2);

        RowItem r3 = new RowItem();
        r3.setHeading("Paiement");
        r3.setSmallImageName(R.drawable.baseline_credit_card_black_24dp);
        myRowItems1.add(r3);

        RowItem r4 = new RowItem();
        r4.setHeading("Informations fiscales");
        r4.setSmallImageName(R.drawable.baseline_calculate_black_24dp);
        myRowItems1.add(r4);


        RowItem r5 = new RowItem();
        r5.setHeading("Modifier les informations");
        r5.setSmallImageName(R.drawable.baseline_info_black_24dp);
        myRowItems1.add(r5);


        RowItem r6 = new RowItem();
        r6.setHeading("A propos");
        r6.setSmallImageName(R.drawable.baseline_info_black_24dp);
        myRowItems1.add(r6);


        RowItem r7 = new RowItem();
        r7.setHeading("Assurance");
        r7.setSmallImageName(R.drawable.baseline_beach_access_black_24dp);
        myRowItems1.add(r7);

        RowItem r8 = new RowItem();
        r8.setHeading("Paramètres de l'application");
        r8.setSmallImageName(R.drawable.baseline_settings_black_24dp);
        myRowItems1.add(r8);

    }



}