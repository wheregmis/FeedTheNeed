package com.example.feedtheneed.presentation.chat

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.feedtheneed.R
import com.example.feedtheneed.domain.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class ChatActivity : AppCompatActivity() {
    var currentChatId = "VP01IGN5YWoFBbnFXQTi"
    //val btnSend = findViewById<Button>(R.id.btn_send)
    lateinit var cuurentChat: Chat
    private var mFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_layout)
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        readChatById()
        val btnSend = findViewById(R.id.btn_send) as Button
        // set on-click listener
        btnSend.setOnClickListener {
            // your code to perform when the user clicks on the button
            //Toast.makeText(this, "You clicked me.", Toast.LENGTH_SHORT).show()
            readChatById()
        }        //writeDataOnFirestore()
    }

    private fun writeDataOnFirestore(){



        var chat = Chat("user1", "user2")


        mFirestore.collection("chat").document(currentChatId)
            .set(chat)
            .addOnSuccessListener {
                Toast.makeText(this, "DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot successfully written!")

                //initListView()
            }.addOnFailureListener {
                    e -> Log.e(TAG, "Error writing document", e)
            }
    }

    private fun readChatById(){

        val docRef = mFirestore.collection("chat").document(currentChatId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // assign data to chat object
                    var toUserId = document.data?.get("toUser")
                    var fromUserId = document.data?.get("fromUser")
                    var chatHistory = document.data?.get("chatHistory")
                    Chat currentChat = Chat(toUserId, fromUserId,)
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }


}