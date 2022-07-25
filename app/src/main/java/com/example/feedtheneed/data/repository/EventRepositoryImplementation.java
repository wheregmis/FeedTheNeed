package com.example.feedtheneed.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void participateInEvent(String userEmail, String eventId) {
        ArrayList<String> participants = new ArrayList<String>(); // TODO: 25/07/2022 Get Current Event Participants
        participants.add(userEmail);
//        db.collection("event").document(eventId).update("eventParticipants", participants).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Log.d("Participants Added", "Participants Added Successfully"+task.getResult().toString());
//            }
//        });

        db.collection("event").document(eventId).update("eventParticipants", participants).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Participants Added", "Participants Added Successfully"+task.getResult().toString());
            }
        });
    }
}
