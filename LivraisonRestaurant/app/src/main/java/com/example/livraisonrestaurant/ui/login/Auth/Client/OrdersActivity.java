package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.LinkAddress;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.OrdersAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<orders> orders = new ArrayList<orders>();
    ArrayList<RowItem> myRowItems;
    OrdersAdapter myAdapter;
    user U;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        myRowItems = new ArrayList<RowItem>();
        BottomNavigationView bnm = findViewById(R.id.bottom_nav);
        for (int i = 0; i < 4;i++) {
            bnm.getMenu().getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onOptionsItemSelected(item);
                    return true;
                }
            });
        }

        myListView = (ListView) findViewById(R.id.lvorders);
        orderHelper.getOrdersCollection().whereEqualTo("client_Uid", FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        orders o = document.toObject(orders.class);
                        orders.add(o);
                    }
                }
                for (orders o : orders) {
                    System.out.println(o.getCreated_at());
                    RowItem row_one = new RowItem();
                    restHelper.getRestaurant(o.getRestaurant_id()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            row_one.setHeading(documentSnapshot.toObject(restaurant.class).getName());
                            if(o.getStatus()==8){
                                row_one.setSubHeading(o.getNbDeProduits() + " - Annulée");
                            }else{
                                row_one.setSubHeading(o.getNbDeProduits() + " - " + o.getPrice()+"$");
                            }

                            row_one.setTheFooter((String) DateFormat.format("dd", o.getCreated_at()) + " " + (String) DateFormat.format("MMM", o.getCreated_at()) + " - " + o.getStatus());
                            myRowItems.add(row_one);
                            myListView.setAdapter(myAdapter);
                        }
                    });


                }
                myAdapter = new OrdersAdapter(getApplicationContext(), myRowItems);
                myListView.setAdapter(myAdapter);
            }

        });


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView <?> parent,final View view, int position, long id){
                RowItem list_row = myRowItems.get(position);
                Toast.makeText(getApplicationContext(), "Produit ajoutée dans le panier !", Toast.LENGTH_SHORT).show();
                    restHelper.getRestaurantCollection().whereEqualTo("name",list_row.getHeading()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w("TAG", "listen:error", error);
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Intent intent = new Intent(getApplicationContext(),MenuRestaurantActivity.class);
                                        intent.putExtra("id_resto", (String)dc.getDocument().getData().get("restaurant_id"));
                                        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                U = documentSnapshot.toObject(user.class);
                                                System.out.println(U.getOrder());
                                                if(U.getOrder().getListProducts().size() == 0 || U.getOrder().getRestaurant_id().equals((String)dc.getDocument().getData().get("restaurant_id")) ){

                                                    userHelper.updateorders(FirebaseAuth.getInstance().getUid(),(String)dc.getDocument().getData().get("restaurant_id"));
                                                    startActivity(intent);
                                                }else{
                                                    AlertDialog.Builder d = new AlertDialog.Builder(OrdersActivity.this);
                                                    d.setTitle("Vous avez dêja un panier sur un autres restaurant voulez vous le supprimer ?? ");
                                                    d.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                                    d.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                        @TargetApi(11)
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            orders ord = new orders(null,(String)dc.getDocument().getData().get("restaurant_id"),FirebaseAuth.getInstance().getUid(),0,new ArrayList<String>());
                                                            userHelper.updateorders2(FirebaseAuth.getInstance().getUid(),ord);
                                                            startActivity(intent);

                                                        }
                                                    });
                                                    d.show();
                                                }
                                            }
                                        });
                                }
                            }

                        }
                    });


            }

        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.homepage:
                Intent inte = new Intent(getApplicationContext(),client.class);
                startActivity(inte);
                return true;
            case R.id.searchpage:
                Intent in = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(in);
                return true;
            case R.id.orderspage :
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
