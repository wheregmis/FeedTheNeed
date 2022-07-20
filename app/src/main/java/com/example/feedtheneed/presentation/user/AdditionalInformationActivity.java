package com.example.feedtheneed.presentation.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.feedtheneed.MapsActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.presentation.authentication.AuthActivity;

public class AdditionalInformationActivity extends AppCompatActivity {
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_information);
        btnNext = (Button) findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Redirect user to dashboard
                startActivity(new Intent(AdditionalInformationActivity.this
                        , MapsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
}