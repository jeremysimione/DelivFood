package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.MenuAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuRestaurantActivity extends AppCompatActivity {
    ArrayList<products> produits = new ArrayList<products>();
    ListView myListView;
    ArrayList<RowItem> myRowItems1;
    ArrayList<RowItem> myRowItems;
    user U;
    CounterFab cart;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Context context = this;
        String uid = i.getStringExtra("id_resto");
        setContentView(R.layout.activity_menu_restaurant);
        myRowItems = new ArrayList<RowItem>();
        myListView = (ListView) findViewById(R.id.myrestaulv);
        myListView.setNestedScrollingEnabled(true);
        NavigationView nav = findViewById(R.id.navigationView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(nav);
        bottomSheetBehavior.setDraggable(true);
        myRowItems1 = new ArrayList<RowItem>();
        bottomSheetBehavior.setPeekHeight(0);
        ListView lv = findViewById(R.id.lvcart);
        lv.setNestedScrollingEnabled(true);
        cart = findViewById(R.id.floating_action_button);
        Button btnOrder = findViewById(R.id.buttonOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PaymentActivity.class);
                startActivity(i);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (U.getOrder().getListProducts().size() != 0) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    if (U.getOrder().getListProducts().size() != 0) {
                        restHelper.getRestaurant(U.getOrder().getRestaurant_id()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ((TextView) findViewById(R.id.textView14)).setText(documentSnapshot.toObject(restaurant.class).getName());
                            }
                        });


                    }
                    for (String c : U.getOrder().getListProducts()) {
                        productHelper.getProduct(c).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                products p = documentSnapshot.toObject(products.class);

                                RowItem row_one = new RowItem();
                                row_one.setSubHeading("lorem ipsumf ffjoizhgoirzhvohzrvuoih");
                                row_one.setTheFooter(String.valueOf(p.getPrice()) + "€");
                                row_one.setHeading(p.getName());
                                myRowItems1.add(row_one);

                                MenuAdapter myAdapter = new MenuAdapter(getApplicationContext(), myRowItems1);
                                lv.setAdapter(myAdapter);

                            }
                        });
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Votre panier est vide", Toast.LENGTH_SHORT).show();
                }
            }
        });


        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                U = documentSnapshot.toObject(user.class);
                cart.setCount(U.getOrder().getListProducts().size());
                if (U.getOrder().getListProducts().size()!=0){
                    restHelper.getRestaurant(U.getOrder().getRestaurant_id()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ((TextView) findViewById(R.id.textView14)).setText(documentSnapshot.toObject(restaurant.class).getName());
                        }
                    });


                }
            }});

        productHelper.getProductsCollection().whereEqualTo("restaurant_id", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("test", document.getId() + " => " + document.getData());
                        products produit = document.toObject(products.class);
                        produits.add(produit);
                    }
                }
                for (products p : produits) {
                    RowItem row_one = new RowItem();
                    row_one.setSubHeading("Déscription du produit");
                    row_one.setTheFooter(String.valueOf(p.getPrice()) + "€");
                    row_one.setHeading(p.getName());
                    row_one.setSmallImageName(R.drawable.mcdonalds2);
                    myRowItems.add(row_one);
                }
                MenuAdapter myAdapter = new MenuAdapter(getApplicationContext(), myRowItems);
                myListView.setAdapter(myAdapter);


                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            RowItem list_row = myRowItems.get(position);
                            System.out.println(list_row + "laa");
                            for (products p : produits) {
                                if (list_row.getHeading().equals(p.getName())) {
                                    userHelper.addProducts(FirebaseAuth.getInstance().getUid(), p.getUid(),p.getPrice());
                                    Toast.makeText(getApplicationContext(), "Produit ajoutée dans le panier !", Toast.LENGTH_SHORT).show();
                                    userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            U = documentSnapshot.toObject(user.class);
                                            cart.setCount(U.getOrder().getListProducts().size());
                                            if (U.getOrder().getListProducts().size()!=0){
                                                restHelper.getRestaurant(U.getOrder().getRestaurant_id()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        ((TextView) findViewById(R.id.textView14)).setText(documentSnapshot.toObject(restaurant.class).getName());
                                                    }
                                                });


                                            }
                                        }});
                                }
                            }
                        }

                    });

            }
        });
    }
}