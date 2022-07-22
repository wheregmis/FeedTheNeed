package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.domain.model.Event;
import com.google.firebase.firestore.CollectionReference;

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
}
