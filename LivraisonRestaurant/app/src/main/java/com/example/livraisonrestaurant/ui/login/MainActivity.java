package com.example.livraisonrestaurant.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livraisonrestaurant.R;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.main_activity_button_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Do something in response to button click
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
        });
    }
}