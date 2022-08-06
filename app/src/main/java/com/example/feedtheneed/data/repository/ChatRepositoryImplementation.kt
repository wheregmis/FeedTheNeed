package com.example.feedtheneed.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.feedtheneed.domain.model.Chat
import com.example.feedtheneed.domain.model.ChatListItem
import com.example.feedtheneed.domain.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepositoryImplementation: ChatRepository {
    private val TAG = this.javaClass.canonicalName

    private var mFirestore = FirebaseFirestore.getInstance()

    /**
     *
     * Will Initiate new Chat with the given two users
     *
     * @param fromUser UserId of the user1 typically add the one who initiates the chat.
     * @param toUser UserId of the user2 typically add the one whom the chat is intended for.
     */
    override fun initiateANewChat(fromUser: String, toUser: String): String{
        var chatId = ""
        var chat = Chat()

        val isChatExists = checkIfChatExists(fromUser, toUser)
        if(checkIfChatExists(fromUser, toUser) != "" ){
            return isChatExists
        }
        mFirestore.collection("chat").document()
            .set(chat)
            .addOnSuccessListener {documentRef ->
                Log.d(ContentValues.TAG, "New Chat initiated: $documentRef")

                //initListView()
            }.addOnFailureListener {
                    e -> Log.e(ContentValues.TAG, "Error writing document", e)
            }
        return chatId
    }
    override fun getChatById(chatId: String): Chat {
        TODO("Not yet implemented")
    }

    override fun getUserChats(userId: String): ArrayList<ChatListItem> {
        TODO("Not yet implemented")
    }


    /**
     *
     * check wheth
     *
     * @param fromUser UserId of the user1 typically add the one who initiates the chat.
     * @param toUser UserId of the user2 typically add the one whom the chat is intended for.
     */
    override fun checkIfChatExists(fromUser: String, toUser: String): String {
        // As of current implementation chat data is centralized for two users
        return ""

    }
}