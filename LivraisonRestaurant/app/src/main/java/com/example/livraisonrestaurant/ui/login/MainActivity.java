package com.example.livraisonrestaurant.ui.login;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.client;
import com.example.livraisonrestaurant.ui.login.Auth.restaurant;
import com.example.livraisonrestaurant.ui.login.Auth.rider;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.example.livraisonrestaurant.ui.login.BaseActivity;
import com.example.livraisonrestaurant.ui.login.models.user;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 123;
    private user user1;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(this.isCurrentUserLogged()){
            this.startProfileActivity();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tv = findViewById(R.id.home_textview);
        //assuming your layout is in a LinearLayout as its root

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels ;
        int width = displayMetrics.widthPixels;
        tv.setPadding(width - 1040,height - 500,0,0);
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

                if (user1.getIsRest()) {
                    Intent intent = new Intent(getApplicationContext(), restaurant.class);
                    startActivity(intent);
                } else if (user1.getIsRider()) {
                    Intent intent = new Intent(getApplicationContext(), rider.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), client.class);
                    startActivity(intent);
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

            userHelper.createUser(uid, username).addOnFailureListener(this.onFailureListener());


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









