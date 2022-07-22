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
//        eventRepository.getAllEvents().addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                List<DocumentSnapshot> listEvents = value.getDocuments();
//                float[] distanceBetweenUserAndEvent = new float[1];
//
//                for (DocumentSnapshot documentSnapshot: listEvents) {
//                    Log.d("Getting Events", "Events"+documentSnapshot.toString());
//                    LatLng eventLocation = new LatLng(Double.valueOf(documentSnapshot.get("eventLat").toString()), Double.valueOf(documentSnapshot.get("eventLong").toString()));
//
//                    Location.distanceBetween(userLatLng.latitude, userLatLng.longitude,
//                            eventLocation.latitude, eventLocation.longitude,
//                            distanceBetweenUserAndEvent);
//
//                    nearbyEventHashMap.put(documentSnapshot.getString("eventId"), String.valueOf(distanceBetweenUserAndEvent[0]));
//
//                }
//            }
//        });

        return eventRepository.getAllEvents().get();
    }


}
