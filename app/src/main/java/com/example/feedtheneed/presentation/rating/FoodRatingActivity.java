package com.example.feedtheneed.presentation.rating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feedtheneed.R;
import com.example.feedtheneed.data.repository.FoodRatingRepoImplementation;
import com.example.feedtheneed.presentation.event.ViewEventActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public class FoodRatingActivity extends AppCompatActivity {

    private TextView rating, comment;
    private AppCompatButton btnSubmit;
    private String eventIdGlobal;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_rating);

        rating = findViewById(R.id.tvStarInput);
        comment = findViewById(R.id.tvCommentInput);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initialize firebase auth
        firebaseAuth= FirebaseAuth.getInstance();

        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        eventIdGlobal = bundle.getString("eventId");
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 07/08/2022 Save ratings in firebase
                FoodRatingRepoImplementation foodRatingRepoImplementation = new FoodRatingRepoImplementation();
                foodRatingRepoImplementation.insertFoodRatingValue(eventIdGlobal, firebaseUser.getUid(), comment.getText().toString()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Snackbar snackbar = Snackbar
                                .make(view, "You have successfully rated this event", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        Intent i = new Intent(getApplicationContext(), ViewEventActivity.class);

                        //Create the bundle
                        Bundle bundle = new Bundle();

                        //Add your data to bundle
                        bundle.putString("eventId", eventIdGlobal);

                        //Add the bundle to the intent
                        i.putExtras(bundle);

                        //Fire that second activity
                        startActivity(i);
                    }
                });
            }
        });
        
    }
}