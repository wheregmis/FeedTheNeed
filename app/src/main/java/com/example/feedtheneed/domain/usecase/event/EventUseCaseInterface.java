package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;

public interface EventUseCaseInterface {

    void addEventToFirebase(Event event);

    void getEventFromFirebase();

    CollectionReference getAllEventsFromFirebase();

    HashMap<String, String> getAllNearByEvents(LatLng userLatLng);
}
