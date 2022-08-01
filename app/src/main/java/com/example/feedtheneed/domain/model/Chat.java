package com.example.feedtheneed.domain.model;


import java.util.ArrayList;

public class Chat {
    private String fromUser;
    private String toUser;
    private ArrayList<ChatHistory> chatHistory;

    public Chat(String fromUser, String toUser, ArrayList<ChatHistory> chatHistory) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.chatHistory = chatHistory;
    }

    public Chat(String fromUser, String toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public ArrayList<ChatHistory> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(ArrayList<ChatHistory> chatHistory) {
        this.chatHistory = chatHistory;
    }
    public void addToChatHistory(String message, int owner){
        long ts = System.currentTimeMillis()/1000;
        chatHistory.add(new ChatHistory(ts, message, owner));

    }
}

class ChatHistory{
    private long timestamp;
    private String message;
    private int owner;

    public ChatHistory(long timestamp, String message, int owner) {
        this.timestamp = timestamp;
        this.message = message;
        this.owner = owner;
    }
}
