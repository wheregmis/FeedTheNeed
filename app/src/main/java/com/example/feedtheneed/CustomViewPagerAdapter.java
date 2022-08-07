package com.example.feedtheneed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.presentation.user.adapters.NearbyEventAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private HashMap<Integer, String> tabs;
    private ArrayList<Event> nearbyEvents;
    private ArrayList<Event> involvedEvents;
    private final int INVOLVED_EVENT = 1;
    RecyclerView rvNearbyEvents;

    public CustomViewPagerAdapter(Context context, HashMap<Integer, String> tabs, ArrayList<Event> nearbyEvents, ArrayList<Event> involvedEvents) {
        mContext = context;
        this.tabs = tabs;
        this.nearbyEvents = nearbyEvents;
        this.involvedEvents = involvedEvents;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.bottom_sheet_main_content, collection, false);
        collection.addView(layout);


        rvNearbyEvents = (RecyclerView) layout.findViewById(R.id.rvNearbyEvent);
        rvNearbyEvents.setLayoutManager(new LinearLayoutManager(mContext));

        if (position == INVOLVED_EVENT) {
            nearbyEvents = involvedEvents;
        }

        rvNearbyEvents.setAdapter(new NearbyEventAdapter(mContext, nearbyEvents));

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