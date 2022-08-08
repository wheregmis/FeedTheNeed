package com.example.feedtheneed.presentation.rating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feedtheneed.R;
import com.example.feedtheneed.data.repository.FoodRatingRepoImplementation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class FoodRatingActivity extends AppCompatActivity {

    private TextView rating, comment;
    private AppCompatButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_rating);

        rating = findViewById(R.id.tvStarInput);
        comment = findViewById(R.id.tvComment);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 07/08/2022 Save ratings in firebase

                FoodRatingRepoImplementation foodRatingRepoImplementation = new FoodRatingRepoImplementation();
                foodRatingRepoImplementation.insertFoodRatingValue("", "", "").addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                    }
                });
            }
        });
        
    }
}