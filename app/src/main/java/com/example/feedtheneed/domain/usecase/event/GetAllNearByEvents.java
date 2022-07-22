package com.example.feedtheneed.domain.usecase.event;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.feedtheneed.data.repository.EventRepositoryImplementation;
import com.example.feedtheneed.domain.repository.EventRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    HashMap<String, String> nearbyEventHashMap = new HashMap<String, String>();

    GetAllNearByEvents(LatLng userLatLng){
        eventRepository = new EventRepositoryImplementation();
        this.userLatLng = userLatLng;
        getNearByEvents();
    }

    void getNearByEvents(){
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

        eventRepository.getAllEvents().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> listEvents = task.getResult().getDocuments();
                float[] distanceBetweenUserAndEvent = new float[1];

                for (DocumentSnapshot documentSnapshot: listEvents) {
                    Log.d("Getting Events", "Events"+documentSnapshot.toString());
                    LatLng eventLocation = new LatLng(Double.valueOf(documentSnapshot.get("eventLat").toString()), Double.valueOf(documentSnapshot.get("eventLong").toString()));

                    Location.distanceBetween(userLatLng.latitude, userLatLng.longitude,
                            eventLocation.latitude, eventLocation.longitude,
                            distanceBetweenUserAndEvent);

                    nearbyEventHashMap.put(documentSnapshot.getString("eventId"), String.valueOf(distanceBetweenUserAndEvent[0]));

                }
            }
        });
    }

    HashMap<String, String> getNearbyEventHashMap(){
        Log.d("Nearby Events", "Nearby Events Hashmap"+nearbyEventHashMap.toString());
        return nearbyEventHashMap;
    }

    public LinkedHashMap<Integer, String> sortHashMapByValues(
            HashMap<Integer, String> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, String> sortedMap =
                new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

}
