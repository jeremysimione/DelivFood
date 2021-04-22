package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;

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