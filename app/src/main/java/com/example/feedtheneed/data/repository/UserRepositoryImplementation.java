package com.example.feedtheneed.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.feedtheneed.domain.model.User;
import com.example.feedtheneed.domain.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepositoryImplementation implements UserRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Task<DocumentReference> insertUser(User user) {
        // Add a new document with a generated ID
        return db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Authentication", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Authentication", "Error adding document", e);
                    }
                });
    }

    @Override
    public Task<QuerySnapshot> getUser(String userEmail) {
        return db.collection("users")
                .whereEqualTo("email", userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Getting User", "Successfully retrieved the user information");


                        } else {
                            Log.w("Retrieval", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public void getUsers() {

    }
}
