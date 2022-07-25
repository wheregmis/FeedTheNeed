package com.example.feedtheneed.domain.repository;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public interface EventRepository {
    
    Task<DocumentReference> createEvent(Event event);

    void updateEvent();

    void deleteEvent();

    void addParticipantsToEvent();

    CollectionReference getAllEvents();

    void participateInEvent(String userEmail, String eventId);

}
