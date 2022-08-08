package com.example.feedtheneed.presentation.chat

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedtheneed.R
import com.example.feedtheneed.data.repository.ChatRepositoryImplementation
import com.example.feedtheneed.domain.model.ChatListItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ChatListActivity : AppCompatActivity(), CoroutineScope {
    private var LOG_TAG = "CHAT_LIST_ACTIVITY"
    private val chatRepositoryImplementation = ChatRepositoryImplementation()
    var adapter: ChatListViewAdapter? = null
    lateinit var recyclerView: RecyclerView
    lateinit var currentChatList: ArrayList<ChatListItem>

    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser;
    var currentUserId = ""
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.feedtheneed.R.layout.chat_people_list_layout)
        Log.d(LOG_TAG, "Running OnCreate")

        currentChatList = chatRepositoryImplementation.getCurrentChatList()
        connectWithUI()
        launch {
            currentUserId = chatRepositoryImplementation.getUserIdByEmail(firebaseUser?.email!!)
            val result =  chatRepositoryImplementation.getChatListForUserId(
                currentUserId)
            Log.d(TAG, "searching for user email: ${firebaseUser?.email!!}")
            Log.d(TAG, "searching for user Id: $currentUserId")
            Log.d(TAG, "Got the chat list: $result")
            updateChatList(result) // onResult is called on the main thread
        }

        // getAllChatsOfUser("GDk7KusL1oZIJKxLgsAoztC9kYw1")
    }



    private fun connectWithUI(){
        recyclerView = findViewById(R.id.rv_message_people)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ChatListViewAdapter( this.currentChatList)
        recyclerView.adapter = adapter




    }

    private fun updateChatList(result: ArrayList<ChatListItem>) {
        for (chatList in result){
            if(currentUserId.equals(chatList.fromUserId)){
                chatList.displayName = chatList.toUserName
            }else{
                chatList.displayName = chatList.fromUserName
            }
        }
        adapter?.dataSet  = result
        adapter?.notifyDataSetChanged()
    }




}


