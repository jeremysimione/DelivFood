package com.example.livraisonrestaurant.ui.login.Auth.Rider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;

import java.util.ArrayList;

public class MailBoxActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<RowItem> myRowItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);
        myRowItems = new ArrayList<RowItem>();

        myListView = (ListView) findViewById(R.id.listviewemail);
        fillArrayList();

        CustomAdapter myAdapter = new CustomAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter(myAdapter);
    }

    private void fillArrayList() {

        RowItem row_one = new RowItem();
        row_one.setHeading("Problèmes avec vos comptes");
        row_one.setSubHeading("Il y a 10 minutes");
        myRowItems.add(row_one);

        RowItem row_two = new RowItem();
        row_two.setHeading("Temps d'attente rallongé en restaurant suite a un probleme");
        row_one.setSubHeading("Il y a 4 jours");
        myRowItems.add(row_two);

        RowItem r = new RowItem();
        r.setHeading("Vos bonus du weekend");
        r.setSubHeading("Il y a 4 jours");
        myRowItems.add(r);


        RowItem r1 = new RowItem();
        r1.setHeading("Revenus");
        myRowItems.add(r1);


        RowItem r2 = new RowItem();
        r2.setHeading("Wallet");
        myRowItems.add(r2);


        RowItem r3 = new RowItem();
        r3.setHeading("Compte");
        myRowItems.add(r3);
    }
}