package com.example.feedtheneed.presentation.chat

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ChatListActivity : AppCompatActivity() {
    private var LOG_TAG = "CHAT_LIST_ACTIVITY"
    private var mFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.d(LOG_TAG, "Running OnCreate")
        getAllChatsOfUser("GDk7KusL1oZIJKxLgsAoztC9kYw1")
    }

    private fun getAllChatsOfUser(userId: String){
        val citiesRef = mFirestore.collection("chat")

        val query = citiesRef.whereEqualTo("toUser", userId).whereEqualTo("fromUser", userId)


        Log.d(LOG_TAG, "Retrieved Data: ${query.get()}")
    }
}