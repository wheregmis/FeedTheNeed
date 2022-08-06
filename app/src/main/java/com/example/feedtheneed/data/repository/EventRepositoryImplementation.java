package com.example.feedtheneed.data.repository;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
    public Task<QuerySnapshot> participateInEvent(String userEmail, String eventId) {

        ArrayList<String> participants = new ArrayList<String>(); // TODO: 25/07/2022 Get Current Event Participants
        participants.add(userEmail);

        return db.collection("event").whereEqualTo("eventId", eventId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("EventsSize","Size is "+task.getResult().size());
                String docId = task.getResult().getDocuments().get(0).getId();
                ArrayList<String> obj = (ArrayList<String>) task.getResult().getDocuments().get(0).get("eventParticipants");
                if (obj != null){
                    participants.addAll(obj);
                }
                db.collection("event").document(docId).update("eventParticipants", participants);
            }
        });

    }

    @Override
    public Task<QuerySnapshot> addUserToVolunteerEvent(String userEmail, String eventId, View view) {
        return db.collection("event").whereEqualTo("eventId", eventId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("EventsSize","Size is "+task.getResult().size());
                String docId = task.getResult().getDocuments().get(0).getId();
                String volunteer = (String) task.getResult().getDocuments().get(0).get("eventVolunteer");
                if (volunteer == null){
                    db.collection("event").document(docId).update("eventVolunteer", userEmail);
                    Snackbar snackbar = Snackbar
                            .make(view, "Successfully Added You as a Volunteer", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar
                            .make(view, "Sorry there is already a volunteer for this event", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    @Override
    public Task<QuerySnapshot> getInvolvedEvents(String userEmail) {
        return db.collection("event").whereArrayContains("eventParticipants", userEmail).get();
    }

    @Override
    public Task<QuerySnapshot> getEventDetails(String eventId) {
        return db.collection("event").whereEqualTo("eventId", eventId).get();
    }
}
