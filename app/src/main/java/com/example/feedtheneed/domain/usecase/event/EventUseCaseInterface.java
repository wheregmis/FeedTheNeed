package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.domain.model.Event;
import com.google.firebase.firestore.CollectionReference;

public interface EventUseCaseInterface {

    void addEventToFirebase(Event event);

    void getEventFromFirebase();

    CollectionReference getAllEventsFromFirebase();
}
