package com.example.feedtheneed.domain.model

data class ChatListItem(
    var chatId: String,
    var fromUserId: String,
    var fromUserName: String,
    var toUserId: String,
    var toUserName: String,
    var displayName: String
)
