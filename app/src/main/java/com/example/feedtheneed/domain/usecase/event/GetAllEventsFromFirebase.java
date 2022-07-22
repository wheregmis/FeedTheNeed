package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.firebase.firestore.CollectionReference;

public class GetAllEventsFromFirebase {
    EventRepository eventRepository;

    GetAllEventsFromFirebase() {
        eventRepository = new EventRepositoryImplementation();
    }

    public CollectionReference getAllEventsFromFirebase(){
        return eventRepository.getAllEvents();
    }
}
