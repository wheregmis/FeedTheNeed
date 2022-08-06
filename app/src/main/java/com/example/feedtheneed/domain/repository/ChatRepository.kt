package com.example.feedtheneed.domain.repository

import com.example.feedtheneed.domain.model.Chat
import com.example.feedtheneed.domain.model.ChatListItem

interface ChatRepository {
    fun initiateANewChat(fromUser: String, toUser: String): String
    fun getChatById(chatId: String): Chat
    fun getUserChats(userId: String): ArrayList<ChatListItem>
    fun checkIfChatExists(fromUser: String, toUser: String): String
}