package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class GetEventDetails {
    EventRepository eventRepository;
    String eventId;

    GetEventDetails(String eventId){
        eventRepository = new EventRepositoryImplementation();
        this.eventId = eventId;
    }

    Task<QuerySnapshot> getEventDetailsFromFirebase(){
        return eventRepository.getEventDetails(eventId);
    }
}
