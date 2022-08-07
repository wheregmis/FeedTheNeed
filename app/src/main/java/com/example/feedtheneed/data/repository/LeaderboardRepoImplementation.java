package com.example.feedtheneed.data.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.feedtheneed.domain.repository.LeaderboardRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class LeaderboardRepoImplementation implements LeaderboardRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Task<QuerySnapshot> getTopRestaurantsBasedOnEventHosted() {
        return db.collection("event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                HashMap<String, Integer> leaderboard = new HashMap<String, Integer>();
                for (DocumentSnapshot event: task.getResult().getDocuments()) {
                    String eventHost = event.getString("eventHost");
                    int count = 1;
                    if(leaderboard.containsKey(eventHost)){
                        count = leaderboard.get(eventHost) + 1;
                        leaderboard.put(eventHost, count);
                    }else{
                        leaderboard.put(eventHost, count);
                    }
                }
                Log.d("Leaderboard", "getTopRestaurantsBasedOnEventHosted"+leaderboard);
            }
        });
    }

    @Override
    public Task<QuerySnapshot> getTopUserBasedOnVolunteerEvent() {
        return db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskUser) {
                HashMap<String, Integer> leaderboard = new HashMap<String, Integer>();
                for(DocumentSnapshot user: taskUser.getResult().getDocuments()){
                    db.collection("event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot event: task.getResult().getDocuments()) {
                                String eventVolunteer = event.getString("eventVolunteer");
                                if (user.getString("userEmail").equalsIgnoreCase(eventVolunteer)){
                                    int count = 1;
                                    if(leaderboard.containsKey(eventVolunteer)){
                                        count = leaderboard.get(eventVolunteer) + 1;
                                        leaderboard.put(eventVolunteer, count);
                                    }else{
                                        leaderboard.put(eventVolunteer, count);
                                    }
                                }
                            }
                            Log.d("Leaderboard", "getTopUserBasedOnVolunteerEvent"+leaderboard);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getTopRestaurant() {

    }
}
