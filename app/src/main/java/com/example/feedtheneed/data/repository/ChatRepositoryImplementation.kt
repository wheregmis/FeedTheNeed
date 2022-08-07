package com.example.feedtheneed.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.feedtheneed.domain.model.Chat
import com.example.feedtheneed.domain.model.ChatListItem
import com.example.feedtheneed.domain.model.User
import com.example.feedtheneed.domain.repository.ChatRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

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
    override fun initiateANewChat(fromUser: String, toUser: String) {
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
    override fun checkIfChatExists(fromUser: String, toUser: String) {
        // As of current implementation chat data is centralized for two users
        val queryChatPrimary = chatCollection.whereEqualTo("fromUser", fromUser)
            .whereEqualTo("toUser", toUser).get()
        val queryChatSecondary = chatCollection.whereEqualTo("fromUser", fromUser)
            .whereEqualTo("toUser", toUser).get()

        queryChatPrimary.addOnSuccessListener { documents ->
            if(documents.isEmpty ){
                queryChatSecondary.addOnSuccessListener { documentsSec ->
                    if(documentsSec.isEmpty){
                        Log.d(TAG, "Creating new Chat Record")
                        createNewChat(fromUser, toUser)
                    }else{
                        Log.d(TAG, "Checking Secondary Docs")
                        for (document in documentsSec) {
                            Log.d(TAG, "Secondary Documents")
                            Log.d(TAG, "${document.id} => ${document.data}")
                            updateCurrentChatData(document)
                            return@addOnSuccessListener
                        }
                    }

                }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }else{
                Log.d(TAG, "Checking Primary Docs")
                for (document in documents) {
                    Log.d(TAG, "Primary Documents")
                    Log.d(TAG, "${document.id} => ${document.data}")
                    updateCurrentChatData(document)
                    return@addOnSuccessListener
                }

            }


            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


        Log.d(TAG, "Document Check done and Updated")


    }

    /**
     *
     * Updates current chat data stored within the data repo
     *
     * @param document document snapshot with chat data
     */
    private fun updateCurrentChatData(document: DocumentSnapshot){
        currentChatId = document.id
        this.currentChat = document.toObject<Chat>()!!


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



    override fun getChatListForUserId(userId: String) {
        val queryChatPrimary = chatCollection.whereEqualTo("fromUser", userId).get()
        val queryChatSecondary = chatCollection.whereEqualTo("toUser", userId).get()

        queryChatPrimary
            .addOnSuccessListener { documentsPrim ->
                populateChatToChatItem(documentsPrim)

             }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

            queryChatSecondary.addOnSuccessListener { documentsPrim ->
                populateChatToChatItem(documentsPrim)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


    }


    private fun populateChatToChatItem(chatDocument: QuerySnapshot){
        for (chatDocument in chatDocument) {
            val chatTemp = chatDocument.toObject<Chat>()

            Log.d(TAG, "Query From User: ${chatTemp.fromUser}")
            userCollection.document(chatTemp.fromUser).get()
                .addOnSuccessListener { fromUserDocument ->
                    if (fromUserDocument != null) {
                        Log.d(TAG, "User data: ${fromUserDocument}")
                        val fromUserTemp = fromUserDocument.toObject<User>()
                        Log.d(TAG, "Query To User: ${chatTemp.toUser}")
                        userCollection.document(chatTemp.toUser).get()
                            .addOnSuccessListener { toUserDocument ->
                                if (toUserDocument != null) {
                                    Log.d(TAG, "User data: ${toUserDocument.data}")

                                    val toUserTemp = toUserDocument.toObject<User>()

                                    val chatListItem = toUserTemp?.let {
                                        fromUserTemp?.let { it1 ->
                                            ChatListItem(chatDocument.id,
                                                chatTemp.fromUser,
                                                it1.userFullName,
                                                chatTemp.toUser,
                                                it.userFullName)
                                        }
                                    }
                                    if (chatListItem != null) {
                                        currentChatList.add(chatListItem)
                                    }
                                    Log.d(TAG, "Added to current Chat List $currentChatList")
                                } else {
                                    Log.d(TAG, "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "get failed with ", exception)
                            }

                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }


        }
    }


}