package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

public interface EventUseCaseInterface {

    void addEventToFirebase(Event event);

    void getEventFromFirebase();

    CollectionReference getAllEventsFromFirebase();

    Task<QuerySnapshot> getAllNearByEvents(LatLng userLatLng);

    Task<QuerySnapshot> participateInEvent(String userEmail, String eventId);

    Task<QuerySnapshot> addUserToVolunteerEvent(String userEmail, String eventId);
}
