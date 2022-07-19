package com.example.feedtheneed.domain.repository;

public interface EventRepository {
    void createEvent();

    void updateEvent();

    void deleteEvent();

    void addParticipantsToEvent();

}
