package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class ParticipateInEvent {
    EventRepository eventRepository;
    String userEmail;

    ParticipateInEvent(String userEmail){
        eventRepository = new EventRepositoryImplementation();
        this.userEmail = userEmail;
    }

    Task<QuerySnapshot> participateInEvent(String eventId){
        return eventRepository.participateInEvent(userEmail, eventId);
    }
}
