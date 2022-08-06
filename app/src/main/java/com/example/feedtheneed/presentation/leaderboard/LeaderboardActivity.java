package com.example.feedtheneed.presentation.leaderboard;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.feedtheneed.R;
import com.example.feedtheneed.data.repository.LeaderboardRepoImplementation;
import com.example.feedtheneed.domain.repository.LeaderboardRepository;
import com.example.feedtheneed.presentation.leaderboard.adapters.LeaderboardAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class LeaderboardActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.getTabAt(0).setIcon( R.drawable.leaderboard);
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.getTabAt(1).setIcon( R.drawable.leaderboard);
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        tabLayout.getTabAt(2).setIcon( R.drawable.leaderboard);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final LeaderboardAdapter adapter = new LeaderboardAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        // TODO: 06/08/2022 Getting Leaderboard Value
        LeaderboardRepository leaderboardRepository = new LeaderboardRepoImplementation();
        leaderboardRepository.getTopRestaurantsBasedOnEventHosted().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // TODO: 06/08/2022 You have value for this leaderboard on this hashmap
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        leaderboardRepository.getTopUserBasedOnVolunteerEvent().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // TODO: 06/08/2022 You have value for this leaderboard on this hashmap
                HashMap<String, Integer> leaderboard = new HashMap<String, Integer>();
                for(DocumentSnapshot user: task.getResult().getDocuments()){
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}