package com.example.feedtheneed.domain.model

import java.io.Serializable

data class ChatListItem (
    var chatId: String,
    var fromUserId: String,
    var fromUserName: String,
    var toUserId: String,
    var toUserName: String,
    var displayName: String): Serializable{

}

