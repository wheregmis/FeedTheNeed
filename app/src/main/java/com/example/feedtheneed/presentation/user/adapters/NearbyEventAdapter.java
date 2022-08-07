package com.example.feedtheneed.presentation.user.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;

import java.util.ArrayList;

public class NearbyEventAdapter extends RecyclerView.Adapter<NearbyEventAdapter.ViewHolder> {
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
        Event event = eventList.get(position);

        eventName.setText(event.getEventName());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventName);
        }
    }


}
