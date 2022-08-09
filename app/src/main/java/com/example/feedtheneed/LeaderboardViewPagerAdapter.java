package com.example.feedtheneed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.model.Leaderboard;
import com.example.feedtheneed.presentation.user.adapters.LeaderboardEventAdapter;
import com.example.feedtheneed.presentation.user.adapters.NearbyEventAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private HashMap<Integer, String> tabs;
    private ArrayList<Leaderboard> restaurantLeaderboard;
    private ArrayList<Leaderboard> userLeaderboard;
    private final int USER = 1;
    RecyclerView rvLeaderboard;

    public LeaderboardViewPagerAdapter(Context context, HashMap<Integer, String> tabs, ArrayList<Leaderboard> restaurantLeaderboard, ArrayList<Leaderboard> userLeaderboard) {
        mContext = context;
        this.tabs = tabs;
        this.restaurantLeaderboard = restaurantLeaderboard;
        this.userLeaderboard = userLeaderboard;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.chat_people_list_layout, collection, false);
        collection.addView(layout);


        rvLeaderboard = (RecyclerView) layout.findViewById(R.id.rvRestaurantLeaderboard);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(mContext));

        if (position == USER) {
            restaurantLeaderboard = userLeaderboard;
        }

        rvLeaderboard.setAdapter(new LeaderboardEventAdapter(mContext, restaurantLeaderboard));

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

}