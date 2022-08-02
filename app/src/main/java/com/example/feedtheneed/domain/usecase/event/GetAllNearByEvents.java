package com.example.feedtheneed.domain.usecase.event;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class GetAllNearByEvents {
    LatLng userLatLng;
    EventRepository eventRepository;


    GetAllNearByEvents(LatLng userLatLng){
        eventRepository = new EventRepositoryImplementation();
        this.userLatLng = userLatLng;
        getNearByEvents();
    }

    Task<QuerySnapshot> getNearByEvents(){
        return eventRepository.getAllEvents().get();
    }


}
