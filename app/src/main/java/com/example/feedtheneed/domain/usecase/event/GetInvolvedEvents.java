package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class GetInvolvedEvents {
    EventRepository eventRepository;

    GetInvolvedEvents(){
        eventRepository = new EventRepositoryImplementation();
    }

    Task<QuerySnapshot> getInvolvedEventsFromFirebase(String userEmail){
        return eventRepository.getInvolvedEvents(userEmail);
    }
}
