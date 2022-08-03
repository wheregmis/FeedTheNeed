package com.example.feedtheneed;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedtheneed.presentation.authentication.AuthActivity;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                FirebaseApp.initializeApp(SplashActivity.this);

                // Initialize firebase auth
                firebaseAuth= FirebaseAuth.getInstance();
                // Initialize firebase user
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                // Check condition
                if(firebaseUser!=null)
                {
                    // When user already sign in
                    // redirect to profile activity
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); finish();
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, AuthActivity.class);
                    //i.putExtra("Lat",currentLocation.getLatitude());
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
