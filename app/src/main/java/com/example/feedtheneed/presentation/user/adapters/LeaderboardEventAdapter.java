package com.example.feedtheneed.presentation.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedtheneed.HomeActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.presentation.event.ViewEventActivity;
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

        return new ViewHolder(view, eventList, mContext);
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


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView eventName;
        TextView eventHost;
        CircularImageView eventImage;
        TextView eventFoodType;
        ArrayList<Event> eventList;
        Context context;

        public ViewHolder(@NonNull View itemView, ArrayList<Event> eventList, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.eventList = eventList;
            this.context = context;
            eventName = itemView.findViewById(R.id.eventName);
            eventHost = itemView.findViewById(R.id.eventHost);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventFoodType = itemView.findViewById(R.id.eventFoodType);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(view.getContext(), "position = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, ViewEventActivity.class);

            //Create the bundle
            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putString("eventId", eventList.get(getLayoutPosition()).getEventId());

            //Add the bundle to the intent
            i.putExtras(bundle);

            //Fire that second activity
            context.startActivity(i);
        }
    }


}
