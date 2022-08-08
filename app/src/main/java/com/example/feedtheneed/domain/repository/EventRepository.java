package com.example.feedtheneed.domain.repository;

import android.view.View;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public interface EventRepository {
    
    Task<QuerySnapshot> createEvent(Event event);

    void updateEvent();

    void deleteEvent();

    void addParticipantsToEvent();

    CollectionReference getAllEvents();

    Task<QuerySnapshot> participateInEvent(String userEmail, String eventId);

    Task<QuerySnapshot> addUserToVolunteerEvent(String userEmail, String eventId, View view);

    Task<QuerySnapshot> getInvolvedEvents(String userEmail);

    Task<QuerySnapshot> getEventDetails(String eventId);
}
