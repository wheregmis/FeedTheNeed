package com.example.feedtheneed.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Key;
import java.util.HashMap;

public class FoodRatingRepoImplementation {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Task<DocumentReference> insertFoodRatingValue(String restaurantId, String userId, String comment) {
        HashMap<String, String> foodRating = new HashMap<String, String>();

        foodRating.put("restaurantId", restaurantId);
        foodRating.put("userId", userId);
        foodRating.put("comment", comment);
        foodRating.put("status", "Positive Sentiment");
        foodRating.put("sentimentScore", "");

        return db.collection("ratings")
                .add(foodRating)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Food Rating", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Food Rating", "Error adding document", e);
                    }
                });
    }
}
