package com.example.feedtheneed.domain.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface LeaderboardRepository {

    Task<QuerySnapshot> getTopRestaurantsBasedOnEventHosted();

    Task<QuerySnapshot> getTopUserBasedOnVolunteerEvent();

    void getTopRestaurant();
}
