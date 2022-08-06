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
    var currentChatId = ""
    var currentUserId = ""
    var currentChat: Chat = Chat("user1", "user2")


    private var mFirestore = FirebaseFirestore.getInstance()
    private val chatRepositoryImplementation = ChatRepositoryImplementation()
    var adapter: ChatViewAdapter? = null

    // UI Elements here
    lateinit var btnSend: Button
    lateinit var etMessageBody: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.feedtheneed.R.layout.chat_layout)
        connectWithUI()
        connectWithFireBase()
        //readChatById()
        chatRepositoryImplementation.initiateANewChat("LRQvUwUFIqXKu2APtj09fWOBhPD3", "LRQvUwUFIqXKu2APtj09fWOBhPD3")

        // TODO: Start a new Chat entry if the chat does not exist

        // Adding recycle view



    }


    private fun connectWithUI(){
         btnSend = findViewById<Button>(R.id.btn_send)
         etMessageBody = findViewById<EditText>(com.example.feedtheneed.R.id.et_message_input)
         btnSend.setOnClickListener {
            // your code to perform when the user clicks on the button
            if(etMessageBody.text.toString() != null){
                Log.d(TAG, "Got text: ${etMessageBody.text}")
                sendNewChat(etMessageBody.text.toString(), 1)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_messages)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ChatViewAdapter( this.currentChat.chatHistory)
        recyclerView.adapter = adapter
    }

    private fun connectWithFireBase(){
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    /**
     *
     * Will Initiate new Chat with the given two users
     *
     * @param fromUser UserId of the user1 typically add the one who initiates the chat.
     * @param toUser UserId of the user2 typically add the one whom the chat is intended for.
     */
    private fun initiateANewChat(fromUser: String, toUser: String): String{
        var chatId = ""
        var chat = Chat()
        mFirestore.collection("chat").document()
            .set(chat)
            .addOnSuccessListener {documentRef ->
                Log.d(TAG, "New Chat initiated: ${documentRef}")

                //initListView()
            }.addOnFailureListener {
                    e -> Log.e(TAG, "Error writing document", e)
            }
        return chatId
    }


    private fun readChatById(){

        val docRef = mFirestore.collection("chat").document(currentChatId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                this.currentChat = Chat()
                Log.d(TAG, "DocumentSnapshot data obj: ${currentChat.fromUser}")
                Log.d(TAG, "DocumentSnapshot data chat history obj: ${currentChat.chatHistory}")
                adapter?.dataSet  = this.currentChat.chatHistory
                adapter?.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Current data: null")
            }
        }


        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // assign data to chat object
                    Log.d(TAG, "DocumentSnapshot data: $document.data")
                    this.currentChat = document.toObject<Chat>()!!
                    if (this.currentChat != null) {
                        Log.d(TAG, "DocumentSnapshot data obj: ${currentChat.fromUser}")
                        Log.d(TAG, "DocumentSnapshot data chat history obj: ${currentChat.chatHistory}")
                        adapter?.dataSet  = this.currentChat.chatHistory
                        adapter?.notifyDataSetChanged()
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }

    private fun sendNewChat(message: String, owner: Int){
        this.currentChat.addToChatHistory(etMessageBody.text.toString(), owner)
        Log.d(TAG, this.currentChat.toString())
        etMessageBody.text.clear()

        mFirestore.collection("chat").document(this.currentChatId)
            .set(this.currentChat)
            .addOnSuccessListener {
                //Toast.makeText(this, "Message sent Success!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot successfully written!")
                adapter?.dataSet  = this.currentChat.chatHistory
                adapter?.notifyDataSetChanged()

                //initListView()
            }.addOnFailureListener {
                    e -> Log.e(TAG, "Error writing document", e)
            }
    }


}