package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.Client.MenuRestaurantActivity;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class restaurantActivity extends BaseActivity {
    restaurant r;
    user u;
    orders o;
    NavigationView nv2;
    Context context = this;
    LinearLayout myScrollView;
    NavigationView n;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        n = findViewById(R.id.navigation_rail);
         nv2 =findViewById(R.id.mynv1);
       myScrollView = findViewById(R.id.linScrollView);
        getorderslistner();
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
        orderHelper.createOrders("test",uid,"wOzNVfEbXMOmedDKNX1yKmVS2LP2",200,new ArrayList<String>());
        r=GetR(uid);
        GetC(uid);


    }
    public void onResume() {

        super.onResume();

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
                       Intent intent = new Intent(getApplicationContext(), ListProduct.class);
                       startActivity(intent);

                       return true;
                   }
               });


            }
        });
        return r;
    }
    public void getorderslistner(){
        String uid = getCurrentUser().getUid();
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.test2);

        orderHelper.getOrdersCollection().whereEqualTo("restaurant_id",uid).whereEqualTo("status",0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "listen:error", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            System.out.println("+++++++++++++++++++++Nouveau orders:" + dc.getDocument().getData());

                            mp.start();
                            LinearLayout parent = new LinearLayout(context);
                            MaterialCardView m = new MaterialCardView(context);
                            m.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            m.setClickable(true);

                            m.setFocusable(true);
                            m.setCheckable(true);
                            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            parent.setOrientation(LinearLayout.VERTICAL);


                            LinearLayout layout2 = new LinearLayout(context);
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout2.setOrientation(LinearLayout.VERTICAL);

                            parent.addView(layout2);
                            TextView tv1 = new TextView(context);

                            tv1.setText((String)dc.getDocument().getData().get("restaurant_id"));
                            TextView tv2 = new TextView(context);
                            layout2.addView(tv1);
                            layout2.addView(tv2);
                            m.addView(parent);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(0, 20, 0, 0);
                            myScrollView.addView(m, layoutParams);
                            break;
                    }
                }
            }
        });
    }
}