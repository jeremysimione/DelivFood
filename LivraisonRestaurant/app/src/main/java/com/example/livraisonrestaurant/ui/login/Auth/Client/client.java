package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class client extends AppCompatActivity {
    ArrayList<restaurant> restaurants = new ArrayList<restaurant>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        setContentView(R.layout.activity_client);
        LinearLayout myScrollView = findViewById(R.id.linScrollView);
        BottomNavigationView bnm = findViewById(R.id.bottom_nav);
        AppBarLayout mappBar = findViewById(R.id.appbar);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(i);
                return true;
            }
        });
        Button b = findViewById(R.id.filter_search);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(i);
            }
        });



        mappBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0)
                {
                    MaterialToolbar mtb = findViewById(R.id.toolbar);
                    mtb.setVisibility(View.GONE);
                    System.out.println("ouvert");
                }
                else
                {
                    MaterialToolbar mtb = findViewById(R.id.toolbar);
                    mtb.setVisibility(View.VISIBLE);

                    System.out.println("fermé");
                }

            }

        });
        for (int i = 0; i < 4;i++) {
            bnm.getMenu().getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onOptionsItemSelected(item);
                    return true;
                }
            });
        }
        restHelper.getRestaurantCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("test", document.getId() + " => " + document.getData());
                                restaurant resto = document.toObject(restaurant.class);
                                restaurants.add(resto);

                            }
                        } else {
                            Log.d("test", "Error getting documents: ", task.getException());
                        }
                        for (restaurant r : restaurants) {
                            LinearLayout parent = new LinearLayout(context);
                            MaterialCardView m = new MaterialCardView(context);
                            m.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            m.setClickable(true);

                            m.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(),MenuRestaurantActivity.class);
                                    intent.putExtra("id_resto",r.getUid());
                                    startActivity(intent);
                                }
                            });
                            m.setFocusable(true);
                            m.setCheckable(true);
                            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            parent.setOrientation(LinearLayout.VERTICAL);
                            parent.getLayoutParams().height = 600;
                            ImageView iv = new ImageView(context);
                            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            iv.setImageResource(R.drawable.home);
                            iv.getLayoutParams().height=420;

                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            LinearLayout layout2 = new LinearLayout(context);
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout2.setOrientation(LinearLayout.VERTICAL);
                            parent.addView(iv);
                            parent.addView(layout2);
                            TextView tv1 = new TextView(context);
                            tv1.setText(r.getName());
                            tv1.setTextSize(20);
                            tv1.setTextColor(Color.BLACK);
                            TextView tv2 = new TextView(context);
                            layout2.addView(tv1);
                            layout2.addView(tv2);
                            m.addView(parent);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(0, 20, 0, 0);
                            myScrollView.addView(m, layoutParams);
                        }
                    }
                });

        System.out.println("restaurants : "  + restaurants);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.homepage:
                return true;
            case R.id.searchpage:
                Intent in = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(in);
                return true;
            case R.id.orderspage :
                Intent inte = new Intent(getApplicationContext(),OrdersActivity.class);
                startActivity(inte);
                return true;
            case R.id.accountpage :
                Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}