package com.example.feedtheneed.presentation.leaderboard.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.feedtheneed.presentation.leaderboard.fragments.TopRestaurants;

public class LeaderboardAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public LeaderboardAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TopRestaurants topRestaurantsFrag = new TopRestaurants();
                return topRestaurantsFrag;
            case 1:
                TopRestaurants cricketFragment = new TopRestaurants();
                return cricketFragment;
            case 2:
                TopRestaurants nbaFragment = new TopRestaurants();
                return nbaFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}