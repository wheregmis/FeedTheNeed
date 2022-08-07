package com.example.feedtheneed.presentation.chat

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedtheneed.R
import com.example.feedtheneed.data.repository.ChatRepositoryImplementation
import com.example.feedtheneed.domain.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject


class ChatActivity : AppCompatActivity() {
    var currentUserId = "SocS85cN1vetNqnssQ2WedMau2S2"
    var currentChat: Chat = Chat("user1", "user2")


    private var mFirestore = FirebaseFirestore.getInstance()
    private val chatRepositoryImplementation = ChatRepositoryImplementation()
    var adapter: ChatViewAdapter? = null

    // UI Elements here
    lateinit var btnSend: Button
    lateinit var etMessageBody: EditText
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.feedtheneed.R.layout.chat_layout)
        connectWithUI()
        connectWithFireBase()
        chatRepositoryImplementation.initiateANewChat(currentUserId, "LRQvUwUFIqXKu2APtj09fWOBhPD3")
        //startListeningToChatUpdates(chatRepositoryImplementation.getCurrentChatId())

    }


    private fun connectWithUI(){
         btnSend = findViewById(R.id.btn_send)
         etMessageBody = findViewById(R.id.et_message_input)
         btnSend.setOnClickListener {
            if(etMessageBody.text.toString() != null){
                Log.d(TAG, "Got text: ${etMessageBody.text}")
                chatRepositoryImplementation.sendANewMessage(etMessageBody.text.toString(), currentUserId)
                etMessageBody.text.clear()
            }
        }

        recyclerView = findViewById(R.id.rv_messages)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ChatViewAdapter( this.currentChat.chatHistory)
        recyclerView.adapter = adapter
    }

    private fun connectWithFireBase(){
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }





    fun startListeningToChatUpdates(chatId: String){
        val docRef = mFirestore.collection("chat").document(chatId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                this.currentChat = Chat()
                currentChat = snapshot.toObject<Chat>()!!
                Log.d(TAG, "DocumentSnapshot data obj: ${currentChat.fromUser}")
                Log.d(TAG, "DocumentSnapshot data chat history obj: ${currentChat.chatHistory}")
                adapter?.dataSet  = this.currentChat.chatHistory
                adapter?.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Current data: null")
            }
        }





    }




}