package com.example.feedtheneed.domain.usecase.event;

public interface EventUseCaseInterface {
    void addEventToFirebase();

    void getEventFromFirebase();
}
