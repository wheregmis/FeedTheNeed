package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;

public class ParticipateInEvent {
    EventRepository eventRepository;
    String userEmail;

    ParticipateInEvent(String userEmail){
        eventRepository = new EventRepositoryImplementation();
        this.userEmail = userEmail;
    }

    void participateInEvent(String eventId){
        eventRepository.participateInEvent(userEmail, eventId);
    }
}
