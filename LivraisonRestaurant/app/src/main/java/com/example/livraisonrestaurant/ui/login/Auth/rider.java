package com.example.livraisonrestaurant.ui.login.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.RiderCustomerAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class rider extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {
    private View locationButton;
    private BottomSheetDialog mBottomSheetDialog;
    private GoogleMap mMap;
    private restaurant r;
    ListView myListView;
    ListView myListView1;
    ArrayList<RowItem> myRowItems;
    ArrayList<RowItem> myRowItems1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        FloatingActionButton fab1 = findViewById(R.id.floatingActionButton9);
        ProgressBar pb = findViewById(R.id.progressBar);
        myRowItems = new ArrayList<RowItem>();
        myRowItems1 = new ArrayList<RowItem>();

        myListView = (ListView) findViewById(R.id.listviewrider);

        myListView1 = (ListView) findViewById(R.id.listviewprofile);

        fillArrayList();
        fillProfile();

        CustomAdapter myAdapter1 = new CustomAdapter(getApplicationContext(), myRowItems1);
        myListView1.setAdapter(myAdapter1);
        RiderCustomerAdapter myAdapter = new RiderCustomerAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter( myAdapter );
        fab1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.uber_online);
                mp.start();
                getorderslistner();
                pb.setIndeterminate(true);

            }
        });

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        NavigationView nv = findViewById(R.id.navigationView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(nv);
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setPeekHeight(220);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // do stuff when the drawer is expanded
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // do stuff when the drawer is collapsed
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // do stuff during the actual drag event for example
                // animating a background color change based on the offset


            }
        });


        //ConstraintLayout cl = findViewById(R.id.myconstraintlayout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton14);

        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        NavigationView nv2 = findViewById(R.id.mynv);
        getCheckedItem(nv2);
    }

    private void fillProfile() {
        RowItem r2 = new RowItem();
        r2.setHeading("Jérémy");
        r2.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems1.add(r2);

    }


    private int getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);

            if (item.isChecked()) {
                item.setChecked(true);
            }
        }

        return -1;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mymap));

            if (!success) {
                System.out.println("Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            System.out.println("Can't find style. Error: " + e);
        }
        // get your maps fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Extract My Location View from maps fragment
        locationButton = mapFragment.getView().findViewById(0x2);
        // Change the visibility of my location button
        if (locationButton != null)
            locationButton.setVisibility(View.GONE);
        FloatingActionButton fa = findViewById(R.id.floatingActionButton12);
        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    if (locationButton != null)
                        locationButton.callOnClick();

                }
            }
        });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void getorderslistner() {
       // String uid = getCurrentUser().getUid();
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.uberdriver);

        orderHelper.getOrdersCollection().whereEqualTo("rider_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).whereEqualTo("status", 0).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            mBottomSheetDialog = new BottomSheetDialog(rider.this);
                            View bottomSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialog, (LinearLayout)findViewById(R.id.bottomSheetContainer));

                            TextView test = bottomSheetLayout.findViewById(R.id.tv_title);
                            restHelper.getRestaurant((String) dc.getDocument().getData().get("restaurant_id")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        r = documentSnapshot.toObject(restaurant.class);
                                        test.setText(r.getName());
                                    (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mBottomSheetDialog.dismiss();
                                        }
                                    });
                                    (bottomSheetLayout.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "Ok button clicked", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    mBottomSheetDialog.setContentView(bottomSheetLayout);
                                    if(!isFinishing()) {
                                        mBottomSheetDialog.show();
                                    }
                                }

                        });
                            break;
                    }
                }
            }
        });
    }
    private void fillArrayList() {

        RowItem row_one = new RowItem( );
        row_one.setHeading("Compte");



        myRowItems.add( row_one );

        RowItem row_two = new RowItem();
        row_two.setHeading("Boîte de réception");
        myRowItems.add(row_two);

       RowItem r = new RowItem();
       r.setHeading("Bonus");
        myRowItems.add(r);


        RowItem r1 = new RowItem();
        r1.setHeading("Revenus");
        myRowItems.add(r1);


        RowItem r2 = new RowItem();
        r2.setHeading("Wallet");
        myRowItems.add(r2);


        RowItem r3 = new RowItem();
        r3.setHeading("Compte");
        myRowItems.add(r3);
    }
}

