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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> data = new HashMap<>();
            data.put("name", "Tokyo");
            data.put("country", "Japan");

            db.collection("cities")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            System.out.println( "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error writing document" + e);
                        }
                    });



            this.startSignInActivity();
        }
    }

    private user getUser(String uid) {
        final user[] user1 = {null};
        userHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {


            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user1[0] = documentSnapshot.toObject(user.class);
            }
        });
        return user1[0];
    }

    private void startProfileActivity() {
        if (this.getUser(this.getCurrentUser().getUid()).getIsRest()) {
            Intent intent = new Intent(this, restaurant.class);
            startActivity(intent);
        } else if (this.getUser(this.getCurrentUser().getUid()).getIsRider()) {
            Intent intent = new Intent(this, rider.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, client.class);
            startActivity(intent);
        }
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
            System.out.println("---|||||||||||||||||||||||||||---------------------------------------------------------||||||||||||||||||||||||4");

        }
    }
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){


        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                System.out.println("---|||||||||||||||||||||||||||---------------------------------------------------------||||||||||||||||||||||||3");

                this.createUserInFirestore();

            }
        }
    }
}









