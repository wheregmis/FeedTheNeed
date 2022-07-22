package com.example.feedtheneed.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventRepositoryImplementation implements EventRepository {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Task<DocumentReference> createEvent(Event event) {
        // Add a new document with a generated ID
        return db.collection("event")
                .add(event)
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
    public void updateEvent() {

    }

    @Override
    public void deleteEvent() {

    }

    @Override
    public void addParticipantsToEvent() {

    }

    @Override
    public CollectionReference getAllEvents() {
//        return db.collection("event").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//            }
//        });

        return db.collection("event");
    }
}
