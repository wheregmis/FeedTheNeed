package com.example.feedtheneed.presentation.leaderboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.feedtheneed.R;
import com.example.feedtheneed.presentation.leaderboard.adapters.LeaderboardAdapter;
import com.google.android.material.tabs.TabLayout;

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