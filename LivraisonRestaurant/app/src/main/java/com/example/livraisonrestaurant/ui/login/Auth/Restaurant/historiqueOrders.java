package com.example.livraisonrestaurant.ui.login.Auth.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class historiqueOrders extends BaseActivity {
    ArrayList<orders> commande = new ArrayList<orders>();
    user u=null;
    products p12 =null;
    String s;
    int iden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_orders);
        LinearLayout myScrollView = findViewById(R.id.linScrollView1);
        Context context = this;
        String uid = getCurrentUser().getUid();
        orderHelper.getOrdersCollection().whereEqualTo("restaurant_id",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("test", document.getId() + " => " + document.getData());
                        orders order = document.toObject(orders.class);
                        commande.add(order);
                    }
                }
                for (orders p : commande){
                    s="";
                    LinearLayout parent = new LinearLayout(context);
                    MaterialCardView m = new MaterialCardView(context);
                    m.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    m.setClickable(true);

                    m.setFocusable(true);
                    m.setCheckable(true);
                  


                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.setOrientation(LinearLayout.VERTICAL);
                    userHelper.getUser(p.getClient_Uid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                           u = documentSnapshot.toObject(user.class);
                            LinearLayout layout2 = new LinearLayout(context);
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout2.setOrientation(LinearLayout.VERTICAL);
                            parent.addView(layout2);
                            TextView tv1 = new TextView(context);
                            tv1.setText(u.getUsername()+"           "+p.getPrice()+"$");
                            TextView tv2 = new TextView(context);
                            layout2.addView(tv1);
                            layout2.addView(tv2);
                            m.addView(parent);
                            m.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle(u.getUsername()+"   "+p.getPrice()+"$");
                                    s ="";
                                   iden=0;
                                    for (String c : p.getListProducts()
                                            ) {

                                        productHelper.getProduct(c).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            iden++;
                                            p12 = documentSnapshot.toObject(products.class);
                                            s += p12.getName()+"\n";
                                            if( iden == p.getListProducts().size()){
                                                builder.setMessage(s);
                                                builder.show();
                                            }



                                        }
                                    });
                                        
                                    }




                                    builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(), "retour", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            });
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(0, 20, 0, 0);
                            myScrollView.addView(m, layoutParams);
                        }
                    });


                }
            }
        });


    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_historique_orders;
    }
}