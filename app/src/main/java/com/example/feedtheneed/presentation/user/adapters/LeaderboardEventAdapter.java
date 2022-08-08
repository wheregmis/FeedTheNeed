package com.example.feedtheneed.presentation.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.model.Leaderboard;
import com.example.feedtheneed.presentation.event.ViewEventActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class LeaderboardEventAdapter extends RecyclerView.Adapter<LeaderboardEventAdapter.ViewHolder>{
    LayoutInflater inflater;
    Context mContext;
    ArrayList<Leaderboard> eventList;

    public LeaderboardEventAdapter(Context mContext, ArrayList<Leaderboard> eventList) {
        this.mContext = mContext;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.restaurantbased_list_layout, parent, false);

        return new ViewHolder(view, eventList, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView eventName = holder.eventName;
        TextView count = holder.count;
        Leaderboard event = eventList.get(position);
        eventName.setText(event.getName());
        count.setText("0");
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView eventName;
        TextView count;

        public ViewHolder(@NonNull View itemView, ArrayList<Leaderboard> eventList, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            eventName = itemView.findViewById(R.id.name);
            count = itemView.findViewById(R.id.noofparticipants);
        }

        @Override
        public void onClick(View view) {
        }
    }


}
