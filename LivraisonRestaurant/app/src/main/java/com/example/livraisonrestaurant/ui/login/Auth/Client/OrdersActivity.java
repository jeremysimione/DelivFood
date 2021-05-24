package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.LinkAddress;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<orders> orders = new ArrayList<orders>();
    ArrayList<RowItem> myRowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        myRowItems = new ArrayList<RowItem>();

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
                    row_one.setHeading(o.getRestaurant_id());
                    row_one.setSubHeading(o.getNbDeProduits() + " - " + o.getPrice());
                    row_one.setTheFooter((String) DateFormat.format("dd", o.getCreated_at()) + " " + (String) DateFormat.format("MMM", o.getCreated_at()) + " - " + o.getStatus());
                    myRowItems.add(row_one);
                }
                OrdersAdapter myAdapter = new OrdersAdapter(getApplicationContext(), myRowItems);
                myListView.setAdapter(myAdapter);
            }

        });


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView <?> parent,final View view, int position, long id){
                RowItem list_row = myRowItems.get(position);
                if (list_row.getHeading() != null) {

                    Toast.makeText(getApplicationContext(), "salut", Toast.LENGTH_LONG);
                }
            }

        });

    }


}
