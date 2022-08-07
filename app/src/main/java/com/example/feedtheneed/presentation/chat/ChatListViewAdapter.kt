package com.example.feedtheneed.presentation.chat

import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.feedtheneed.R
import com.example.feedtheneed.domain.model.ChatListItem


class ChatListViewAdapter(var dataSet: ArrayList<ChatListItem>) :
    RecyclerView.Adapter<ChatListViewAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.tv_chat_person)

        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
            Log.d(TAG, "Chat List item was clicked")
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chat_people_list_item, viewGroup, false)

        return ViewHolder(view)
    }
    private fun onItemClick(position: Int) {
       Log.d(TAG, "Recycle view clicked!")
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].toUserName
        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "clicked the view: ${dataSet[position]}")
            val intent = Intent(viewHolder.itemView.context, ChatActivity::class.java)
            intent.putExtra("chatId", dataSet[position].chatId)
            viewHolder.itemView.context.startActivity(intent)

        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
