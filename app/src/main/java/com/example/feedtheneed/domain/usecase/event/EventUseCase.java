package com.example.feedtheneed.domain.usecase.event;

import android.view.View;

import com.example.feedtheneed.domain.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

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
    public Task<QuerySnapshot> getAllNearByEvents(LatLng userLatLng) {
        return new GetAllNearByEvents(userLatLng).getNearByEvents();
    }

    @Override
    public Task<QuerySnapshot> participateInEvent(String userEmail, String eventId) {
        return new ParticipateInEvent(userEmail).participateInEvent(eventId);
    }

    @Override
    public Task<QuerySnapshot> addUserToVolunteerEvent(String userEmail, String eventId, View view) {
        return new VolunteerInEvent(userEmail).addUserToVolunteerEvent(eventId, view);
    }

    @Override
    public Task<QuerySnapshot> getInvolvedEvents(String userEmail) {
        return new GetInvolvedEvents().getInvolvedEventsFromFirebase(userEmail);
    }

    @Override
    public Task<QuerySnapshot> getEventDetails(String eventId) {
        return new GetEventDetails(eventId).getEventDetailsFromFirebase();
    }
}
