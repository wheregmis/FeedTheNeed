package com.example.feedtheneed.presentation.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.feedtheneed.domain.model.ChatListItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class ChatListActivity : AppCompatActivity() {
    private var LOG_TAG = "CHAT_LIST_ACTIVITY"
    private var mFirestore = FirebaseFirestore.getInstance()
    private lateinit var userChatList: ArrayList<ChatListItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "Running OnCreate")
        //getAllChatsOfUser("GDk7KusL1oZIJKxLgsAoztC9kYw1")
    }


    // Should work as a snapshot listener
//    private fun getAllChatsOfUser(userId: String): ArrayList<ChatListItem> {
//        val chatsRef = mFirestore.collection("chat")
//        val chatList = arrayListOf<ChatListItem>()
//
//        val queryChatsFrom = chatsRef.whereEqualTo("fromUser", userId).get()
//        val queryChatsTo = chatsRef.whereEqualTo("toUser", userId).get()
//
//        queryChatsFrom
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    var docIter = document.documents.listIterator()
//                    while (docIter.hasNext()) {
//                        val chatListItem: ChatListItem
//
//                        Log.d(LOG_TAG, "DocumentSnapshot data: ${docIter.next()}")
//                    }
//                    //Log.d(LOG_TAG, "End of documents: $userChatList")
//
//                } else {
//                    Log.d(LOG_TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(LOG_TAG, "get failed with ", exception)
//            }
//    }
}