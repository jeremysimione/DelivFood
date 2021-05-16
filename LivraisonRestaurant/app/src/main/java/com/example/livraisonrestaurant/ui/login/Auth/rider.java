package com.example.livraisonrestaurant.ui.login.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.Rider.MailBoxActivity;
import com.example.livraisonrestaurant.ui.login.Auth.Rider.RiderAccountActivity;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.RiderCustomerAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.type.DateTime;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class rider extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {
    private View locationButton;
    private Marker markerPerth;
    private LatLng latlng = null;
    DirectionsResult result= null;
    private LatLng position = null;
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
        FloatingActionButton deconnexion = findViewById(R.id.floatingActionButton);
        ProgressBar pb = findViewById(R.id.progressBar);
        myRowItems = new ArrayList<RowItem>();
        myRowItems1 = new ArrayList<RowItem>();
        myListView = (ListView) findViewById(R.id.listviewrider);

        myListView1 = (ListView) findViewById(R.id.listviewprofile);
        TextView con = (TextView) findViewById(R.id.textView7);
        TextView go = (TextView) findViewById(R.id.textView2);

        fillArrayList();
        fillProfile();
        pb.setVisibility(View.GONE);
        CustomAdapter myAdapter1 = new CustomAdapter(getApplicationContext(), myRowItems1);
        myListView1.setAdapter(myAdapter1);
        RiderCustomerAdapter myAdapter = new RiderCustomerAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                RowItem selectedItem = (RowItem) parent.getItemAtPosition(position);
                String selection = selectedItem.getHeading();
                System.out.println("iciciici" + selection);
                switch (selection){
                    case "Boîte de réception":
                        intent = new Intent(getApplicationContext(), MailBoxActivity.class);
                        startActivity(intent);
                        break;
                    case "Compte":
                        intent = new Intent(getApplicationContext(), RiderAccountActivity.class);
                        startActivity(intent);

                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.uber_online);
                riderHelper.updateEnligne(true,FirebaseAuth.getInstance().getCurrentUser().getUid());
                mp.start();
                getorderslistner();

                pb.setVisibility(View.VISIBLE);
                pb.setIndeterminate(true);
                con.setText("Vous êtes en ligne");
                go.setVisibility(View.GONE);
                fab1.setVisibility (View.GONE);

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
                    // do stuff when the drawer is expanded*
                    pb.setVisibility(View.GONE);
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // do stuff when the drawer is collapsed
                    if(con.getText().equals("Vous êtes en ligne")){
                        pb.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // do stuff during the actual drag event for example
                // animating a background color change based on the offset
                pb.setVisibility(View.GONE);

            }
        });


        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                con.setText("Vous êtes hors ligne");
                go.setVisibility(View.VISIBLE);
                fab1.setVisibility (View.VISIBLE);
                pb.setIndeterminate(false);
                pb.setVisibility(View.GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            };
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
        position = new LatLng(location.getLatitude(), location.getLongitude() );
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

        orderHelper.getOrdersCollection().whereEqualTo("rider_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).whereEqualTo("status", 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                           // View bottomSheetLayout1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialog, (ConstraintLayout) findViewById(R.id.constraint));
                            View bottomSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialog,(ConstraintLayout)findViewById(R.id.bottomSheetContainer));

                            TextView test = bottomSheetLayout.findViewById(R.id.tv_title);
                            restHelper.getRestaurant((String) dc.getDocument().getData().get("restaurant_id")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    r = documentSnapshot.toObject(restaurant.class);
                                    test.setText(r.getName());
                                    try {
                                        System.out.println(r.getAdress_uid());
                                        latlng = getLocationFromAddress(r.getAdress_uid());

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    (bottomSheetLayout.findViewById(R.id.chip4)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mBottomSheetDialog.dismiss();
                                        }
                                    });
                                    (bottomSheetLayout.findViewById(R.id.bottomSheetContainer)).setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                           result = DirectionsApi.newRequest(getGeoContext())
                                                        .mode(TravelMode.BICYCLING).origin("31 rue de la méditerranée").destination("16 rue boussairolles").departureTime(Instant.now()).await();
                                           System.out.println(result);
                                            } catch (ApiException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            position = new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
                                            System.out.println(position);
                                            addMarkersToMap(result,mMap);
                                            addPolyline(result,mMap);

                                            mBottomSheetDialog.dismiss();
                                        }
                                    });
                                    mBottomSheetDialog.setContentView(bottomSheetLayout);
                                    if (!isFinishing()) {
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

        RowItem row_one = new RowItem();
        row_one.setHeading("COVID-19");


        myRowItems.add(row_one);

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

    public LatLng getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            System.out.println(location.getLatitude());

            System.out.println(location.getLongitude());

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

                 } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }
    private GeoApiContext getGeoContext() {

        return new GeoApiContext.Builder().apiKey("" +
                "Your_api_key")
                .build();
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress));
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        riderHelper.updateEnligne(false,FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}

