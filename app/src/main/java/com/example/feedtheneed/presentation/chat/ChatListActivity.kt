package com.example.feedtheneed.presentation.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.feedtheneed.data.repository.ChatRepositoryImplementation
import com.example.feedtheneed.domain.model.ChatListItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class ChatListActivity : AppCompatActivity() {
    private var LOG_TAG = "CHAT_LIST_ACTIVITY"
    private val chatRepositoryImplementation = ChatRepositoryImplementation()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "Running OnCreate")
        chatRepositoryImplementation.getChatListForUserId(
                "yZiAeAdxV49PkwYaNKSk")
        connectWithUI()
        //getAllChatsOfUser("GDk7KusL1oZIJKxLgsAoztC9kYw1")
    }

    fun connectWithUI(){

    }




}