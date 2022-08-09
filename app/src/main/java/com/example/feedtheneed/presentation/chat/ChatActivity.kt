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
import com.example.feedtheneed.data.repository.UserRepositoryImplementation
import com.example.feedtheneed.domain.model.Chat
import com.example.feedtheneed.domain.model.ChatListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ChatActivity : AppCompatActivity(), CoroutineScope {
    var currentUserId = ""
    var currentChat: Chat = Chat("user1", "user2")

//        imageView.findViewById(R.id.imageView);
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser;


    private var mFirestore = FirebaseFirestore.getInstance()
    private val chatRepositoryImplementation = ChatRepositoryImplementation()

    var adapter: ChatViewAdapter? = null
    var chatInfo: ChatListItem? = null
    var currentChatId: String? = null

    // UI Elements here
    lateinit var btnSend: Button
    lateinit var etMessageBody: EditText
    lateinit var recyclerView: RecyclerView



    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_message_layout)




        var chatId:String = intent.getStringExtra("chatId").toString()
        var toUserId:String = intent.getStringExtra("toUserId").toString()
        chatInfo = intent.getSerializableExtra("chatInfo") as? ChatListItem
        Log.d(TAG, "Received Chat Info Bundle: $chatInfo")
        Log.d(TAG, "Received chatId: $chatId")
        Log.d(TAG, "Received toUserId: $toUserId")
        currentChatId = chatId



        launch {
            currentUserId = chatRepositoryImplementation.getUserIdByEmail(firebaseUser?.email!!);

            if(chatInfo === null){
                var toUserInfo = chatRepositoryImplementation.getUserById(toUserId)
                var fromUserInfo = chatRepositoryImplementation.getUserById(currentUserId)
                chatInfo = fromUserInfo?.let {
                    toUserInfo?.let { it1 ->
                        ChatListItem("",
                            currentUserId,
                            it.userFullName,
                            toUserId,
                            it1.userFullName,
                            toUserInfo.userFullName)
                    }
                }
            }
            if(chatId === null){
                currentUserId = firebaseUser?.email?.let {
                    chatRepositoryImplementation.getUserIdByEmail(
                        it
                    )
                }!!

            }


            chatId = chatRepositoryImplementation.checkIfChatExists(
                chatInfo!!.fromUserId, chatInfo!!.toUserId)

            Log.d(TAG, "created new chat with Id: $chatId")
            connectWithUI()
            connectWithFireBase()


            updateChatList(chatId)
        }

    }




    private fun connectWithUI(){
         btnSend = findViewById(R.id.btn_send)
         etMessageBody = findViewById(R.id.et_message_input)
         btnSend.setOnClickListener {
            if(etMessageBody.text.toString() != null){
                Log.d(TAG, "Got text: ${etMessageBody.text}")
                chatRepositoryImplementation.sendANewMessage(etMessageBody.text.toString(), currentUserId, this.currentChatId!!)
                etMessageBody.text.clear()
            }
        }

        recyclerView = findViewById(R.id.rv_messages)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Log.d(TAG, "chatinfo: $chatInfo")
        adapter = ChatViewAdapter( this.currentChat.chatHistory, this.chatInfo!!)
        recyclerView.adapter = adapter
    }

    private fun connectWithFireBase(){
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }





    private fun startListeningToChatUpdates(chatId: String){
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
                 chatRepositoryImplementation.setCurrentChat(this.currentChat)
                Log.d(TAG, "DocumentSnapshot data obj: ${currentChat.fromUser}")
                Log.d(TAG, "DocumentSnapshot data chat history obj: ${currentChat.chatHistory}")
                adapter?.dataSet  = this.currentChat.chatHistory
                adapter?.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Current data: null")
            }
        }


    }


    private fun updateChatList(chatId: String) {
        startListeningToChatUpdates(chatId)
    }

}

