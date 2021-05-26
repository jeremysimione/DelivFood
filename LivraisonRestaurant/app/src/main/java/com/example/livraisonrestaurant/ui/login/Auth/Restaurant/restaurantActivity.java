package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.Client.MenuRestaurantActivity;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.MainActivity;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
    String s;
    NavigationView nv2;
    Context context = this;
    LinearLayout myScrollView;
    NavigationView n;
    products p12;
    int i;
    LinearLayout parent;
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
        nv2.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent3);
                finish();
                return false;
            }
        });
        String uid = getCurrentUser().getUid();
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

        orderHelper.getOrdersCollection().whereEqualTo("restaurant_id",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "listen:error", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            mp.start();
                            if(dc.getDocument().getData().get("status").toString().equals("0")) {
                                final boolean[] bo = {true};
                                mp.start();
                                parent = new LinearLayout(context);
                                MaterialCardView m = new MaterialCardView(context);
                                m.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                m.setClickable(true);
                                m.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View myScrollView1 = inflater.inflate(R.layout.scrool_text, null, false);
                                        LinearLayout l = myScrollView1.findViewById(R.id.linlay);
                                        s = "";
                                        i = 0;
                                        for (String c : (ArrayList<String>) dc.getDocument().getData().get("listProducts")) {
                                            productHelper.getProduct(c).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    i++;
                                                    p12 = documentSnapshot.toObject(products.class);
                                                    LinearLayout parent1 = new LinearLayout(context);
                                                    MaterialCardView m1 = new MaterialCardView(context);
                                                    m1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                    m1.setFocusable(true);
                                                    m1.setCheckable(true);
                                                    parent1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                    parent1.setOrientation(LinearLayout.VERTICAL);
                                                    LinearLayout layout3 = new LinearLayout(context);
                                                    layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                    layout3.setOrientation(LinearLayout.VERTICAL);

                                                    parent1.addView(layout3);
                                                    TextView tv12 = new TextView(context);
                                                    s += "       " + p12.getName() + "         " + p12.getPrice() + "$" + "\n";
                                                    if (i == ((ArrayList<String>) dc.getDocument().getData().get("listProducts")).size()) {
                                                        tv12.setText(s);
                                                    }

                                                    TextView tv22 = new TextView(context);
                                                    layout3.addView(tv12);
                                                    layout3.addView(tv22);
                                                    m1.addView(parent1);
                                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                    layoutParams.setMargins(0, 20, 0, 0);
                                                    l.addView(m1, layoutParams);
                                                }
                                            });

                                        }
                                        LinearLayout parent1 = new LinearLayout(context);
                                        MaterialCardView m1 = new MaterialCardView(context);
                                        m1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        m1.setFocusable(true);
                                        m1.setCheckable(true);
                                        parent1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        parent1.setOrientation(LinearLayout.VERTICAL);
                                        LinearLayout layout3 = new LinearLayout(context);
                                        layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout3.setOrientation(LinearLayout.VERTICAL);

                                        parent1.addView(layout3);
                                        TextView tv12 = new TextView(context);

                                        tv12.setText("Total TTC :" + dc.getDocument().getData().get("price") + "$");
                                        TextView tv22 = new TextView(context);
                                        layout3.addView(tv12);
                                        layout3.addView(tv22);
                                        m1.addView(parent1);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                        layoutParams.setMargins(0, 20, 0, 0);
                                        l.addView(m1, layoutParams);


                                        userHelper.getUser((String) dc.getDocument().getData().get("client_Uid")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                u = documentSnapshot.toObject(user.class);
                                                AlertDialog.Builder d = new AlertDialog.Builder(restaurantActivity.this).setView(myScrollView1);
                                                d.setTitle(u.getUsername());
                                                if (bo[0]) {
                                                    d.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            orderHelper.updateStatus((String) dc.getDocument().getData().get("uid"), 8);

                                                            parent.setVisibility(RelativeLayout.GONE);

                                                        }
                                                    });

                                                    d.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                                                        @TargetApi(11)
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            riderHelper.getRiderCollection().whereEqualTo("enLigne", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    ;
                                                                    System.out.println("+++++++++++++++++++++" + (String) queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().getData().get("uid"));
                                                                    orderHelper.orderRider((String) dc.getDocument().getData().get("uid"), (String) queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().getData().get("uid"));
                                                                    orderHelper.updateStatus((String) dc.getDocument().getData().get("uid"), 1);
                                                                    bo[0] = false;

                                                                }
                                                            });

                                                        }

                                                    });
                                                } else {
                                                    d.setNegativeButton("", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }
                                                    );
                                                    d.setPositiveButton("imprimer", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                                }

                                                d.show();


                                            }
                                        });


                                    }
                                });

                                m.setFocusable(true);
                                m.setCheckable(true);
                                parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                parent.setOrientation(LinearLayout.VERTICAL);


                                LinearLayout layout2 = new LinearLayout(context);
                                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                layout2.setOrientation(LinearLayout.VERTICAL);

                                parent.addView(layout2);
                                TextView tv1 = new TextView(context);
                                userHelper.getUser((String) dc.getDocument().getData().get("client_Uid")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        u = documentSnapshot.toObject(user.class);
                                        tv1.setText(u.getUsername());

                                    }
                                });

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
                        case MODIFIED:
                            if(dc.getDocument().getData().get("status").toString().equals("2")){
                                parent.setVisibility(RelativeLayout.GONE);
                               }
                    }
                }
            }
        });
    }
}