package com.example.feedtheneed.domain.model;

public class ChatHistory{
    public long timestamp;
    public String message;
    public int owner;
    public ChatHistory(){}
    public ChatHistory(long timestamp, String message, int owner) {
        this.timestamp = timestamp;
        this.message = message;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ChatHistory{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", owner=" + owner +
                '}';
    }
}
