package com.example.feedtheneed.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feedtheneed.R
import com.example.feedtheneed.domain.model.ChatHistory
import com.example.feedtheneed.domain.model.ChatListItem
import java.util.*


class ChatViewAdapter(var dataSet: ArrayList<ChatHistory>, var chatInfo: ChatListItem) :
    RecyclerView.Adapter<ChatViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewMessage: TextView
        val textViewOwner: TextView
        val textViewTime: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textViewMessage = view.findViewById(R.id.tv_chat_message)
            textViewOwner = view.findViewById(R.id.tv_chat_message_owner)
            textViewTime = view.findViewById(R.id.tv_time)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chat_messsage_list_item, viewGroup, false)


        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        var owner = ""
        if(dataSet[position].owner === 1){
            owner = chatInfo.fromUserName
        }else if(dataSet[position].owner === 2){
            owner = chatInfo.toUserName
        }
//

        viewHolder.textViewMessage.text = dataSet[position].message
        viewHolder.textViewOwner.text = owner
        viewHolder.textViewTime.text = getShortDate(dataSet[position].timestamp * 1000)

        //viewHolder.itemView.layoutDirection = Gravity.RIGHT
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private fun getShortDate(ts:Long?):String{
        if(ts == null) return ""
        //Get instance of calendar
        val calendar = Calendar.getInstance(Locale.getDefault())
        //get current date from ts
        calendar.timeInMillis = ts
        //return formatted date
        return android.text.format.DateFormat.format("E, dd MM yyyy HH:MM", calendar).toString()
    }

}
