package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AdressActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng position = new LatLng(0,0);
    private Location mCurrentLocation;
    private String adress;
private Marker f;
    GoogleMap g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);
        Button confirmer = findViewById(R.id.outlinedButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userHelper.updateAdress(FirebaseAuth.getInstance().getUid(),adress);
                Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
        TextInputEditText adressET = findViewById(R.id.adress);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println("+++++"+location);
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            position = new LatLng(location.getLatitude(), location.getLongitude());
                            mCurrentLocation = location;
                            System.out.println("++++"+position);
                            System.out.println(mCurrentLocation);
                            try {
                                adress = getAdressFromLocation();
                                adressET.setText(adress);
                            } catch (IOException e) {
                                e.printStackTrace();
                            };
                            f.setPosition(position);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(position) // Position que l'on veut atteindre
                                    .zoom(15)             // Niveau de zoom
                                    .bearing(180) 	      // Orientation de la caméra, ici au sud
                                    .tilt(60)    	      // Inclinaison de la caméra
                                    .build();
                            g.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                    }
                });



    }
    public String getAdressFromLocation() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0);

        return address;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
      f = googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Marker"));
      g=googleMap;
           }
}
