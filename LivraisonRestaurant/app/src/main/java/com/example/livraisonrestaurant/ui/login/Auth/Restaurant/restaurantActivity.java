package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class restaurantActivity extends BaseActivity {
    restaurant r;
    NavigationView nv2;
    NavigationView n;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        n = findViewById(R.id.navigation_rail);
         nv2 =findViewById(R.id.mynv1);
        n.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout1);
                mDrawerLayout.openDrawer(Gravity.START);
                return false;
            }
        });
        String uid = getCurrentUser().getUid();
        orderHelper.createOrders("test",uid,uid,200,new ArrayList<String>());
        r=GetR(uid);
        GetC(uid);


    }

    private void GetC(String uid) {
        n.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent1 = new Intent(getApplicationContext(), historiqueOrders.class);
                startActivity(intent1);
                return true;
            }
        });

    }

    public int getFragmentLayout() {
        return R.layout.activity_restaurant;
    }



    public restaurant GetR(String uid){
        System.out.println("++++++++++++++++++f");
        restHelper.getRestaurant(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                System.out.println("++++++++++++++++++f2");
               restaurant re = documentSnapshot.toObject(restaurant.class);
               nv2.getMenu().getItem(0).setTitle(re.getName());
               nv2.getMenu().getItem(1).setTitle("Produits");
               nv2.getMenu().getItem(2).setTitle("");
               nv2.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       Intent intent = new Intent(getApplicationContext(), ordersRestaurant.class);
                       startActivity(intent);

                       return true;
                   }
               });


            }
        });
        return r;
    }
}