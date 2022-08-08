package com.example.feedtheneed.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.feedtheneed.domain.model.Chat
import com.example.feedtheneed.domain.model.ChatListItem
import com.example.feedtheneed.domain.model.User
import com.example.feedtheneed.domain.repository.ChatRepository
import com.example.feedtheneed.presentation.chat.ChatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class ChatRepositoryImplementation: ChatRepository {
    private val TAG = this.javaClass.canonicalName

    private val mFirestore = FirebaseFirestore.getInstance()
    private val chatCollection = mFirestore.collection("chat")
    private val userCollection = mFirestore.collection("users")


    // User Related Variables
    private var currentChat:Chat = Chat()
    private lateinit  var currentChatId: String
    private  var currentChatList: ArrayList<ChatListItem> = ArrayList()




    /**
     *
     * Will Initiate new Chat with the given two users
     *
     * @param fromUser UserId of the user1 typically add the one who initiates the chat.
     * @param toUser UserId of the user2 typically add the one whom the chat is intended for.
     */
    override suspend fun initiateANewChat(fromUser: String, toUser: String) {
        checkIfChatExists(fromUser, toUser)
    }

    private fun createNewChat(fromUser: String, toUser: String){
        var chat = Chat(fromUser, toUser)
        chatCollection.document()
            .set(chat)
            .addOnSuccessListener {
                Log.d(TAG, "Created New Chat record")
            }.addOnFailureListener {
                    e -> Log.e(ContentValues.TAG, "Error writing document", e)
            }
    }
    override fun getChatById(chatId: String): Chat {
        TODO("Not yet implemented")
    }

    override fun getUserChats(userId: String): ArrayList<ChatListItem> {
        TODO("Not yet implemented")
    }


    /**
     *
     * check whether there's a chat alreasy exists - will interchange from and to to get
     * and will update the current chat loaded
     *
     * @param fromUser UserId of the user1 typically add the one who initiates the chat.
     * @param toUser UserId of the user2 typically add the one whom the chat is intended for.
     */
    override suspend fun checkIfChatExists(fromUser: String, toUser: String) : String{
        // As of current implementation chat data is centralized for two users
        return try {
            val queryChatPrimary = chatCollection.whereEqualTo("fromUser", fromUser)
                .whereEqualTo("toUser", toUser).get().await()
            val queryChatSecondary = chatCollection.whereEqualTo("fromUser", fromUser)
                .whereEqualTo("toUser", toUser).get().await()
            if(!queryChatPrimary.isEmpty || !queryChatSecondary.isEmpty){
                for (document in (queryChatPrimary + queryChatSecondary)){
                    currentChatId = document.id
                    this.currentChat = document.toObject<Chat>()!!
                }
            }else{
                var chat = Chat(fromUser, toUser)
                chatCollection.document().set(chat).await()
                var chatCreated = checkIfChatExists(fromUser, toUser)
                Log.d(TAG, "New Chat Entry Created: $chatCreated")

            }

            currentChatId
        }catch (e: java.lang.Exception){
            Log.e(TAG, "Failed to upload: ${e.message.orEmpty()}")
            return "" // or you can rethrow or return null if you want
        }




        Log.d(TAG, "Document Check done and Updated")


    }



    /**
     *
     * Sends a new message
     *
     * @param message UserId of the user1 typically add the one who initiates the chat.
     * @param currentUserId UserId of the user2 typically add the one whom the chat is intended for.
     */
    override fun sendANewMessage(message: String, currentUserId: String) {
        var owner = 0
        // define ownerId
        if(currentChat.fromUser === currentUserId){
            owner = 1
        }else{
            owner = 2
        }

        this.currentChat.addToChatHistory(message, owner)
        Log.d(ContentValues.TAG, this.currentChat.toString())

        mFirestore.collection("chat").document(this.currentChatId)
            .set(this.currentChat)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Message sent Successfully")
            }.addOnFailureListener {
                    e -> Log.e(ContentValues.TAG, "Error writing document", e)
            }
    }

    override fun getCurrentChatId(): String{
        return this.currentChatId
    }





    override suspend fun getChatListForUserId(userId: String): ArrayList<ChatListItem> {

        return try {
            Log.d(TAG, "Quering for user: $userId")
            var userChatList: ArrayList<ChatListItem> = ArrayList()
            val queryChatPrimary = chatCollection.whereEqualTo("fromUser", userId).get().await()
            val queryChatSecondary = chatCollection.whereEqualTo("toUser", userId).get().await()
            for (document in (queryChatPrimary + queryChatSecondary)){
                val chatDoc = document.toObject<Chat>()
                Log.d(TAG, "Got maching chat: $chatDoc")
                val chatDocToUserData = userCollection.document(chatDoc.toUser).get().await().toObject<User>()
                val chatDocFromUserData = userCollection.document(chatDoc.fromUser).get().await().toObject<User>()
                var chatDisplayName = ""
                if(userId === chatDoc.fromUser){
                    chatDisplayName = chatDocToUserData!!.userFullName
                }else{
                    chatDisplayName = chatDocFromUserData!!.userFullName
                }
                val chatListItem = ChatListItem(document.id,
                    chatDoc.fromUser,
                    chatDocFromUserData!!.userFullName,
                    chatDoc.toUser,
                    chatDocToUserData!!.userFullName,
                    chatDisplayName
                )
                userChatList.add(chatListItem)
            }
            userChatList
        } catch(e: Exception) {
            Log.e(TAG, "Failed to upload: ${e.message.orEmpty()}")
            return ArrayList() // or you can rethrow or return null if you want
        }




    }

    override fun getCurrentChatList() : ArrayList<ChatListItem>{
        return this.currentChatList
    }


//    private fun populateChatToChatItem(chatDocument: QuerySnapshot){
//        for (chatDocument in chatDocument) {
//            val chatTemp = chatDocument.toObject<Chat>()
//
//            Log.d(TAG, "Query From User:${chatTemp.fromUser}")
//            userCollection.document(chatTemp.fromUser).get()
//                .addOnSuccessListener { fromUserDocument ->
//                    if (fromUserDocument != null) {
//                        Log.d(TAG, "User data:${fromUserDocument.data}")
//                        val fromUserTemp = fromUserDocument.toObject<User>()
//                        Log.d(TAG, "Query To User:${chatTemp.toUser}")
//                        userCollection.document(chatTemp.toUser).get()
//                            .addOnSuccessListener { toUserDocument ->
//                                if (toUserDocument != null) {
//                                    Log.d(TAG, "User data:${toUserDocument.data}")
//
//                                    val toUserTemp = toUserDocument.toObject<User>()
//
//                                    val chatListItem = toUserTemp?.let {
//                                        fromUserTemp?.let { it1 ->
//                                            ChatListItem(chatDocument.id,
//                                                chatTemp.fromUser,
//                                                it1.userFullName,
//                                                chatTemp.toUser,
//                                                it.userFullName)
//                                        }
//                                    }
//                                    if (chatListItem != null) {
//                                        currentChatList.add(chatListItem)
//                                    }
//                                    Log.d(TAG, "Added to current Chat List $currentChatList")
//
//                                } else {
//                                    Log.d(TAG, "No such document")
//                                }
//
//                            }
//                            .addOnFailureListener { exception ->
//                                Log.d(TAG, "get failed with ", exception)
//                            }
//
//                    } else {
//                        Log.d(TAG, "No such document")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d(TAG, "get failed with ", exception)
//                }
//
//
//        }
//    }


}