package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.domain.model.Event;

public interface EventUseCaseInterface {

    void addEventToFirebase(Event event);

    void getEventFromFirebase();
}
