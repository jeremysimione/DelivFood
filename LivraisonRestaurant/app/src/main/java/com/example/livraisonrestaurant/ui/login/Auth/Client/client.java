package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.MainActivity;
import com.example.livraisonrestaurant.ui.login.MenuAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.productHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.products;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.opencensus.resource.Resource;


public class client extends Fragment {
    ArrayList<restaurant> restaurants = new ArrayList<restaurant>();
    ArrayList<String> imgrestau = new ArrayList<String>();
    user U;
    ArrayList<RowItem> myRowItems;
    CounterFab cart;
    View v;

    @Override
    public void onResume() {
        super.onResume();

        userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                U = documentSnapshot.toObject(user.class);
                cart.setCount(U.getOrder().getListProducts().size());
                if (U.getOrder().getListProducts().size() != 0) {
                    restHelper.getRestaurant(U.getOrder().getRestaurant_id()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ((TextView)v.findViewById(R.id.textView14)).setText(documentSnapshot.toObject(restaurant.class).getName());
                        }
                    });


                }
            }
        });
    }
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        orderHelper.getOrdersCollection().whereEqualTo("client_Uid",FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "listen:error", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case MODIFIED:

                           if(dc.getDocument().getData().get("status").toString().equals("1")){
                               notif("Votre commande à été prise en charge par le restaurant");}
                            if(dc.getDocument().getData().get("status").toString().equals("2")){
                               notif("Votre commande a été prise en charge par le livreur");}
                            if(dc.getDocument().getData().get("status").toString().equals("8")){
                               notif("Votre commande a été annulée ");

                           }
                    }
                }
            }
        });
        View view = inflater.inflate(R.layout.activity_client, container, false);
        v =view;
        Context context = getActivity();
        imgrestau.add("burgee");
        imgrestau.add("pizza");
        imgrestau.add("sushi");
        imgrestau.add("mcdonalds");
        imgrestau.add("doner");
        NavigationView nav = view.findViewById(R.id.navigationView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(nav);
        bottomSheetBehavior.setDraggable(true);
        myRowItems = new ArrayList<RowItem>();
        bottomSheetBehavior.setPeekHeight(0);
        ListView lv = view.findViewById(R.id.lvcart);
        lv.setNestedScrollingEnabled(true);
        cart = view.findViewById(R.id.floating_action_button);
        Button btnOrder = view.findViewById(R.id.buttonOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),PaymentActivity.class);
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
                                ((TextView) view.findViewById(R.id.textView14)).setText(documentSnapshot.toObject(restaurant.class).getName());
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
                                myRowItems.add(row_one);

                                MenuAdapter myAdapter = new MenuAdapter(getActivity(), myRowItems);
                                lv.setAdapter(myAdapter);

                            }
                        });
                    }
                }else{
                    Toast.makeText(getActivity(), "Votre panier est vide", Toast.LENGTH_SHORT).show();
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
                            ((TextView) view.findViewById(R.id.textView14)).setText(documentSnapshot.toObject(restaurant.class).getName());
                        }
                    });


                }
            }});
        LinearLayout myScrollView = view.findViewById(R.id.linScrollView);
        AppBarLayout mappBar = view.findViewById(R.id.appbar);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
            Resources resource=getResources();
            String[] Adress= resource.getStringArray(R.array.Adress);




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Adress, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(getActivity(), AdressActivity.class);
                startActivity(i);
                return true;
            }
        });
        Button b = view.findViewById(R.id.filter_search);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SearchActivity.class);
                startActivity(i);
            }
        });



        mappBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0)
                {
                    MaterialToolbar mtb = view.findViewById(R.id.toolbar);
                    mtb.setVisibility(View.GONE);

                }
                else
                {
                    MaterialToolbar mtb = view.findViewById(R.id.toolbar);
                    mtb.setVisibility(View.VISIBLE);


                }

            }

        });

        restHelper.getRestaurantCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        restaurants = new ArrayList<restaurant>();
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
                                    Intent intent = new Intent(getActivity(),MenuRestaurantActivity.class);
                                    intent.putExtra("id_resto",r.getUid());
                                    userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            U = documentSnapshot.toObject(user.class);
                                            System.out.println(U.getOrder());
                                            if(U.getOrder().getListProducts().size() == 0 || U.getOrder().getRestaurant_id().equals(r.getUid()) ){

                                                userHelper.updateorders(FirebaseAuth.getInstance().getUid(),r.getUid());
                                                startActivity(intent);
                                            }else{
                                               AlertDialog.Builder d = new AlertDialog.Builder(context);
                                               d.setTitle("Vous avez dêja un panier sur un autres restaurant voulez vous le supprimer ?? ");
                                               d.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                               d.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                   @TargetApi(11)
                                                   public void onClick(DialogInterface dialog, int id) {
                                                       orders ord = new orders(null,r.getUid(),FirebaseAuth.getInstance().getUid(),0,new ArrayList<String>());
                                                       userHelper.updateorders2(FirebaseAuth.getInstance().getUid(),ord);
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

                            iv.getLayoutParams().height=420;

                            int randomNum = ThreadLocalRandom.current().nextInt(0, 5 );

                            int id = getActivity().getResources().getIdentifier(imgrestau.get(randomNum), "drawable", getActivity().getPackageName());
                            iv.setImageResource(id);
                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                            iv.setPadding(40,40,40,40);
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            LinearLayout layout2 = new LinearLayout(context);
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout2.setOrientation(LinearLayout.VERTICAL);
                            parent.addView(iv);
                            parent.addView(layout2);
                            TextView tv1 = new TextView(context);
                            tv1.setText(r.getName());
                            tv1.setTextSize(20);
                            tv1.setPadding(40,10,0,0);
                            tv1.setTextColor(Color.BLACK);
                            TextView tv2 = new TextView(context);
                            tv2.setText("Frais de livraisons : 0.49$ - 15-25mn" );
                            tv2.setTextSize(14);
                            tv2.setPadding(40,10,0,0);
                            tv2.setTextColor(Color.GRAY);

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
    return view;
    }

    public void notif(String message){
        Intent intent = new Intent(getActivity(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Mise à jours commande");
        inboxStyle.addLine(message);
        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getActivity(), channelId)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Mise à jours commande")
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify("firebase", 007, notificationBuilder.build());
    }

}