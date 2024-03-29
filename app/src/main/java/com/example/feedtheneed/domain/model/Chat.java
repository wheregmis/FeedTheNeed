package com.example.feedtheneed.domain.model;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class Chat {
    private String fromUser;
    private String toUser;
    private ArrayList<ChatHistory> chatHistory = new ArrayList<>();

    public Chat(){
        this.fromUser = null;
        this.toUser = null;
        this.chatHistory = new ArrayList<ChatHistory>();
    }
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
        if(chatHistory == null){
           chatHistory = new ArrayList<>();
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar .getTimeInMillis();

        long ts = timeInMili;
        chatHistory.add(new ChatHistory(ts, message, owner));

    }

    @Override
    public String toString() {
        return "Chat{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", chatHistory=" + chatHistory +
                '}';
    }
}


