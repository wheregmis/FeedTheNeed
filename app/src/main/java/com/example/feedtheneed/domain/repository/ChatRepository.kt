package com.example.feedtheneed.domain.repository

import com.example.feedtheneed.domain.model.Chat
import com.example.feedtheneed.domain.model.ChatListItem

interface ChatRepository {
    suspend fun initiateANewChat(fromUser: String, toUser: String)
    fun getChatById(chatId: String): Chat
    fun getUserChats(userId: String): ArrayList<ChatListItem>
    suspend fun checkIfChatExists(fromUser: String, toUser: String): String
    fun sendANewMessage(message: String, currentUserId: String, currentChatId: String)
    fun getCurrentChatId(): String
    suspend fun getChatListForUserId(userId: String): ArrayList<ChatListItem>
    fun getCurrentChatList(): ArrayList<ChatListItem>
}