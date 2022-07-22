package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;

public class EventUseCase implements EventUseCaseInterface {

    @Override
    public void addEventToFirebase(Event event) {
        new AddEventToFirebase(event).addEventToFirebaseImplementation();
    }

    @Override
    public void getEventFromFirebase() {

    }

    @Override
    public CollectionReference getAllEventsFromFirebase() {
        return new GetAllEventsFromFirebase().getAllEventsFromFirebase();
    }

    @Override
    public HashMap<String, String> getAllNearByEvents(LatLng userLatLng) {
        return new GetAllNearByEvents(userLatLng).getNearbyEventHashMap();
    }
}
