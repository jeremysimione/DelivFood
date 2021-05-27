package com.example.livraisonrestaurant.ui.login.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import com.example.livraisonrestaurant.ui.login.Auth.Client.client;
import com.example.livraisonrestaurant.ui.login.Auth.Rider.MailBoxActivity;
import com.example.livraisonrestaurant.ui.login.Auth.Rider.RiderAccountActivity;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.PickupAdapter;
import com.example.livraisonrestaurant.ui.login.RiderCustomerAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.restHelper;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.example.livraisonrestaurant.ui.login.models.restaurant;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class rider extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {
    private View locationButton;
    private boolean isInShift =false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker markerPerth;
    private LatLng latlng = null;
    DirectionsResult result = null;
    DirectionsResult result_client = null;
    private LatLng position = null;
    orders ord;
    user c;
    private boolean pickedUpOrder = false;
    private BottomSheetDialog mBottomSheetDialog;
    private GoogleMap mMap;
    private restaurant r;
    ArrayList<Marker> markers= new ArrayList<Marker>();
    String str;
    Polyline myItinierary;
    String adr_client;
    private Location mCurrentLocation;
    ListView myListView;
    ListView myListView1;
    ListView myListView3;

    ArrayList<RowItem> myRowItems;
    ArrayList<RowItem> myRowItems1;

    ArrayList<RowItem> myRowItems2;
    LocationRequest locationRequest = LocationRequest.create();
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
        myRowItems2 = new ArrayList<RowItem>();

        myListView = (ListView) findViewById(R.id.listviewrider);
        myListView3 = (ListView) findViewById(R.id.lvrider);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            position = new LatLng(location.getLatitude(), location.getLongitude());
                            mCurrentLocation = location;

                        }
                    }
                });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    onMyLocationClick(location);

                    if(isInShift){
                        if(!pickedUpOrder) {
                            try {
                                result = DirectionsApi.newRequest(getGeoContext())
                                        .mode(TravelMode.BICYCLING).origin(getAdressFromLocation()).destination(str).departureTime(Instant.now()).await();
                            } catch (ApiException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            myItinierary.remove();
                            addPolyline(result, mMap);
                            if (result.routes[0].legs[0].distance.inMeters < 50) {
                                Toast.makeText(getApplicationContext(), "Vous etes arrivés au restau", Toast.LENGTH_LONG).show();
                                pickedUpOrder =true;
                            }
                        } else {
                            try {
                                result = DirectionsApi.newRequest(getGeoContext())
                                        .mode(TravelMode.BICYCLING).origin(getAdressFromLocation()).destination(adr_client).departureTime(Instant.now()).await();
                            } catch (ApiException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0;i< markers.size();i++) {
                                markers.get(i).remove();
                            }
                            addMarkersToMap(result,mMap);
                            myItinierary.remove();
                            addPolyline(result, mMap);


                        }
                    }
                }
            }
        };
        myListView1 = (ListView) findViewById(R.id.listviewprofile);
        TextView con = (TextView) findViewById(R.id.textView7);


        fillArrayList();
        fillProfile();
        pb.setVisibility(View.GONE);
        PickupAdapter mypickupadapter = new PickupAdapter(getApplicationContext(),myRowItems2);
        CustomAdapter myAdapter1 = new CustomAdapter(getApplicationContext(), myRowItems1);
        myListView1.setAdapter(myAdapter1);
        RiderCustomerAdapter myAdapter = new RiderCustomerAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter(myAdapter);
        myListView3.setAdapter(mypickupadapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                RowItem selectedItem = (RowItem) parent.getItemAtPosition(position);
                String selection = selectedItem.getHeading();
                System.out.println("iciciici" + selection);
                switch (selection) {
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
                mp.start();
                getorderslistner();
                TextView tv2 = findViewById(R.id.textView2);
                tv2.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                pb.setIndeterminate(true);
                con.setText("Vous êtes en ligne");
                riderHelper.updateEnligne(FirebaseAuth.getInstance().getUid(),true);
                fab1.setVisibility(View.GONE);

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
                    if (con.getText().equals("Vous êtes en ligne")) {
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
                fab1.setVisibility(View.VISIBLE);
                TextView tv2 = findViewById(R.id.textView2);
                tv2.setVisibility(View.VISIBLE);
                pb.setIndeterminate(false);
                riderHelper.updateEnligne(FirebaseAuth.getInstance().getUid(),false);
                pb.setVisibility(View.GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            ;
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

        RowItem r3 = new RowItem();
        r3.setHeading("Jérémy S.");
        r3.setSubHeading("#AC56458");
        myRowItems2.add(r3);
        RowItem r4 = new RowItem();
        r4.setHeading("Jérémy S.");
        r4.setSubHeading("#AC5648");
        myRowItems2.add(r4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (true) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
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
        mCurrentLocation = location;
        position = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        float zoomLevel = 15.0f; //This goes up to 21
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()), zoomLevel));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        float zoomLevel = 15.0f; //This goes up to 21
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()), zoomLevel));
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
                            ord=dc.getDocument().toObject(orders.class);
                            mBottomSheetDialog = new BottomSheetDialog(rider.this);
                            // View bottomSheetLayout1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialog, (ConstraintLayout) findViewById(R.id.constraint));
                            View bottomSheetLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_dialog,(ConstraintLayout)findViewById(R.id.bottomSheetContainer));

                            TextView test = bottomSheetLayout.findViewById(R.id.tv_title);

                            userHelper.getUser((String) dc.getDocument().getData().get("client_Uid")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    c = documentSnapshot.toObject(user.class);
                                    adr_client = c.getAdress();
                                    restHelper.getRestaurant((String) dc.getDocument().getData().get("restaurant_id")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            r = documentSnapshot.toObject(restaurant.class);
                                            test.setText(r.getName());
                                            try {
                                                System.out.println(r.getAdress_uid());
                                                str = r.getAdress_uid();
                                                latlng = getLocationFromAddress(r.getAdress_uid());
                                                try {
                                                    result = DirectionsApi.newRequest(getGeoContext())
                                                            .mode(TravelMode.BICYCLING).origin(getAdressFromLocation()).destination(str).departureTime(Instant.now()).await();
                                                    result_client = DirectionsApi.newRequest(getGeoContext())
                                                            .mode(TravelMode.BICYCLING).origin(getAdressFromLocation()).destination(adr_client).departureTime(Instant.now()).await();
                                                } catch (ApiException e) {
                                                    e.printStackTrace();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                //mMap.moveCamera(CameraUpdateFactory.zoomOut());
                                                float zoomLevel = 12.0f; //This goes up to 21
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()), zoomLevel));

                                               // mMap.moveCamera(CameraUpdateFactory.zoomOut());
                                                addMarkersToMap(result_client, mMap);
                                                addPolyline(result_client, mMap);
                                                int distance = (int) (result_client.routes[0].legs[0].distance.inMeters);
                                                double distance2 = (double) distance / 1000;
                                                double tps = 5 * distance2;
                                                int tps1 = (int) tps;
                                                TextView tarifs = bottomSheetLayout.findViewById(R.id.textView9);
                                                DecimalFormat df = new DecimalFormat("0.00");
                                                tarifs.setText(df.format(calculTarifLivraison(result_client, 1)) + "€" + "\n" + distance2 + "km -" + tps1 + "mn");
                                                TextView tv10 = bottomSheetLayout.findViewById(R.id.textView11);
                                                tv10.setText(c.getAdress());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            (bottomSheetLayout.findViewById(R.id.chip4)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    mBottomSheetDialog.dismiss();
                                                    markers.get(0).remove();
                                                    mp.stop();
                                                    myItinierary.remove();
                                                }
                                            });
                                            (bottomSheetLayout.findViewById(R.id.bottomSheetContainer)).setOnClickListener(new View.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onClick(View v) {
                                                    mp.stop();
                                                    isInShift = true;
                                                    markers.get(0).remove();
                                                    addMarkersToMap(result, mMap);
                                                    myItinierary.remove();
                                                    addPolyline(result, mMap);
                                                    mBottomSheetDialog.dismiss();
                                                }
                                            });
                                            mBottomSheetDialog.setContentView(bottomSheetLayout);
                                            if (!isFinishing()) {
                                                mBottomSheetDialog.show();
                                            }
                                        }

                                    });

                                }
                            });
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

    public String getAdressFromLocation() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0);

        return address;
    }
    private GeoApiContext getGeoContext() {

        return new GeoApiContext.Builder().apiKey("AIzaSyDgvoOUdBPjTtemYGC7WSWHFY21jd9wZ4M")
                .build();
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress)));


    }

    private double calculTarifLivraison(DirectionsResult result,int nbOrders){
        double prixTotal = 0;
        double tarifPriseEnChargeNet = 1.9;
        double tarifDepotClientNet = 0.95;
        double tarifParKm = 0.76;
        int distance = (int)(result.routes[0].legs[0].distance.inMeters);
        double distance2 = (double) distance/1000;
        System.out.println("distance : " + distance2 + " " + distance2 + tarifParKm);
        prixTotal+= tarifPriseEnChargeNet + (tarifParKm *  distance2) + tarifDepotClientNet;
        return prixTotal;
    }
    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        myItinierary = mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }
}

