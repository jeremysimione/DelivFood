package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResultActivity extends AppCompatActivity {
    ArrayList<restaurant> restaurants =new ArrayList<restaurant>();
    user U;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        Context context = this;
        ArrayList<String> r = intent.getStringArrayListExtra("mot");
        LinearLayout myScrollView = findViewById(R.id.linScrollView);


        restHelper.getRestaurantCollection().whereArrayContainsAny("categorie", r).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    restaurants.add(document.toObject(restaurant.class));
                    restaurant r = document.toObject(restaurant.class);
                    System.out.println("iciiiiiiii dans on complete " + r.getCategorie());


                }

                for (restaurant r : restaurants) {
                    LinearLayout parent = new LinearLayout(context);
                    MaterialCardView m = new MaterialCardView(context);
                    m.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    m.setClickable(true);

                    m.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MenuRestaurantActivity.class);
                            intent.putExtra("id_resto", r.getUid());
                            userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    U = documentSnapshot.toObject(user.class);
                                    System.out.println(U.getOrder());
                                    if (U.getOrder().getListProducts().size() == 0 || U.getOrder().getRestaurant_id().equals(r.getUid())) {

                                        userHelper.updateorders(FirebaseAuth.getInstance().getUid(), r.getUid());
                                        startActivity(intent);
                                    } else {
                                        AlertDialog.Builder d = new AlertDialog.Builder(SearchResultActivity.this);
                                        d.setTitle("Vous avez dêja un panier sur un autres restaurant voulez vous le supprimer ?? ");
                                        d.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        d.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                            @TargetApi(11)
                                            public void onClick(DialogInterface dialog, int id) {
                                                orders ord = new orders(null, r.getUid(), FirebaseAuth.getInstance().getUid(), 0, new ArrayList<String>());
                                                userHelper.updateorders2(FirebaseAuth.getInstance().getUid(), ord);
                                                startActivity(intent);

                                            }
                                        });
                                        d.show();
                                    }
                                }
                            });


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
                    iv.getLayoutParams().height = 420;

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

        System.out.println("restaurants : " + restaurants);


        SearchView simpleSearchView = (SearchView) findViewById(R.id.searchviewResult); // inititate a search view


        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), SearchResultActivity.class);
                ArrayList<String> r = new ArrayList<String>();
                r.add(query);
                i.putExtra("mot", r);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }
        public void onBackPressed() {
            Log.d("CDA", "onBackPressed Called");
            Intent setIntent = new Intent(getApplicationContext(),ClientActivity.class);
            startActivity(setIntent);
        }

}