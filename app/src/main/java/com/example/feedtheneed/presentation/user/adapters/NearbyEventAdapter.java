package com.example.feedtheneed.presentation.user.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedtheneed.HomeActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class NearbyEventAdapter extends RecyclerView.Adapter<NearbyEventAdapter.ViewHolder>{
    LayoutInflater inflater;
    Context mContext;
    ArrayList<Event> eventList;

    public NearbyEventAdapter(Context mContext, ArrayList<Event> eventList) {
        this.mContext = mContext;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_event_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView eventName = holder.eventName;
        TextView eventHost = holder.eventHost;
        Event event = eventList.get(position);
        eventName.setText(event.getEventName());
        eventHost.setText(event.getEventHost());
        holder.eventFoodType.setText(event.getEventFoodType());
        if (event.getEventImageUrls().size() > 0){
            Glide.with(mContext)
                    .load(event.getEventImageUrls().get(0))
                    .into(holder.eventImage);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        TextView eventHost;
        CircularImageView eventImage;
        TextView eventFoodType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
            eventHost = itemView.findViewById(R.id.eventHost);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventFoodType = itemView.findViewById(R.id.eventFoodType);
        }

    }


}
