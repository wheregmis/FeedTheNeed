package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class AddEventToFirebase {
    Event event;
    EventRepository eventRepository;

    AddEventToFirebase(Event event){
        this.event = event;
        eventRepository = new EventRepositoryImplementation();
    }

    Task<DocumentReference> addEventToFirebaseImplementation(){
        return eventRepository.createEvent(event);
    }
}
