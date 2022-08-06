package com.example.feedtheneed.domain.usecase.event;

import android.view.View;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class VolunteerInEvent {
    EventRepository eventRepository;
    String userEmail;
    
    VolunteerInEvent(String userEmail){
        eventRepository = new EventRepositoryImplementation();
        this.userEmail = userEmail;
    }
    
    Task<QuerySnapshot> addUserToVolunteerEvent(String eventId, View view){
        return eventRepository.addUserToVolunteerEvent(userEmail, eventId, view);
    }
}
