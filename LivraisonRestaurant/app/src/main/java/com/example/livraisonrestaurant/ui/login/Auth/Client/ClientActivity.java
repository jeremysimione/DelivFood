package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.livraisonrestaurant.R;
import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ClientActivity extends AppCompatActivity {
    private  SearchActivity cat;
    private client cli;
    private AccountActivity a;
    private OrdersActivity o;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client2);
        cat = new SearchActivity();
        cli = new client();
        a = new AccountActivity();
        o=new OrdersActivity();
        BottomNavigationView bnm = findViewById(R.id.bottom_nav);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, cli, "NewFragmentTag");
        ft.commit();
        bnm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_frame, cli, "NewFragmentTag");
                        ft.commit();
                        break;
                    case R.id.searchpage:
                        FragmentTransaction f1 = getSupportFragmentManager().beginTransaction();
                        f1.replace(R.id.main_frame, cat, "NewFragmentTag");
                        f1.commit();
                        break;
                    case R.id.orderspage:
                        FragmentTransaction f2 = getSupportFragmentManager().beginTransaction();
                        f2.replace(R.id.main_frame, o, "NewFragmentTag");
                        f2.commit();
                        break;

                    case R.id.accountpage:
                        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
                        f.replace(R.id.main_frame, a, "NewFragmentTag");
                        f.commit();
                        break;
                }
                return true;
            }

        });
    }

}