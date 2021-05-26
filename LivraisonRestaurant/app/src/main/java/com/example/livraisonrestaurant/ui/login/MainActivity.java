package com.example.livraisonrestaurant.ui.login;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.Client.client;
import com.example.livraisonrestaurant.ui.login.Auth.Restaurant.restaurantActivity;
import com.example.livraisonrestaurant.ui.login.Auth.rider;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

public class MainActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 123;
    private user user1;
    private user user2=null;
    Button button;
    boolean f = false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 2);
        //assuming your layout is in a LinearLayout as its root
        new Handler().postDelayed(null,3000);
        if(this.isCurrentUserLogged()){
            this.createUserInFirestore();

        }else {
            setContentView(R.layout.activity_login);
            TextView tv = (TextView) findViewById(R.id.home_textview);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            tv.setPadding(width - 1040, height - 500, 0, 0);
            ImageView image = findViewById(R.id.home_image);
//Use RelativeLayout.LayoutParams if your parent is a RelativeLayout
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    width, height);
            image.setLayoutParams(params);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            button = (Button) findViewById(R.id.main_activity_button_login);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onClickLoginButton();

                }
            });
        }
    }

    public void onClickLoginButton() {
        if (this.isCurrentUserLogged()) {
            this.startProfileActivity();
        } else {
            this.startSignInActivity();

        }
    }

    private void startProfileActivity() {

        userHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user1 = documentSnapshot.toObject(user.class);
                if(user1.getIsRest()!=null) {
                    f=true;
                    if (user1.getIsRest()) {
                        Intent intent = new Intent(getApplicationContext(), restaurantActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (user1.getIsRider()) {
                        Intent intent = new Intent(getApplicationContext(), rider.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), client.class);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });



    }

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ub)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);

        // 4 - Handle SignIn Activity response on activity result

    }


    private void createUserInFirestore() {

        if (this.getCurrentUser() != null) {

            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            userHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.toObject(user.class)==null) {
                        userHelper.createUser(uid, username);
                        new Handler().postDelayed(null,6000);
                        Intent intent = new Intent(getApplicationContext(), client.class);
                        startActivity(intent);
                    }else
                        {


                            userHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    user1 = documentSnapshot.toObject(user.class);
                                    if(user1.getIsRest()!=null) {
                                        f=true;
                                        if (user1.getIsRest()) {
                                            Intent intent = new Intent(getApplicationContext(), restaurantActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (user1.getIsRider()) {
                                            Intent intent = new Intent(getApplicationContext(), rider.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), client.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }


                                }
                            });

                    }
                    System.out.println("DocumentSnapshot successfully written!");
                }

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error writing document");
                        }
                    });

            }



        }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){


        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS

                this.createUserInFirestore();

            }
        }
    }
}









