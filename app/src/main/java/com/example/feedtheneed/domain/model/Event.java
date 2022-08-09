package com.example.feedtheneed.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private String eventId;
    private String eventName;
    private String eventHost;
    private String eventDescription;
    private String eventDate;
    private String eventTime;
    private String eventLat;
    private String eventLong;
    private String eventFoodType;
    private String eventParticipantsCount;
    private ArrayList<String> eventParticipants;
    private ArrayList<String> eventImageUrls;
    public String eventVolunteer;


    public Event(String eventId, String eventName, String eventHost,
                 String eventDescription, String eventDate, String eventTime,
                 String eventLat, String eventLong, ArrayList<String> eventParticipants,
                 String eventFoodType, String eventParticipantsCount, ArrayList<String> eventImageUrls, String eventVolunteer) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventHost = eventHost;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLat = eventLat;
        this.eventLong = eventLong;
        this.eventParticipants = eventParticipants;
        this.eventFoodType = eventFoodType;
        this.eventParticipantsCount = eventParticipantsCount;
        this.eventImageUrls = eventImageUrls;
        this.eventVolunteer = eventVolunteer;
    }

    public Event(){

    }


    public String getEventVolunteer() {
        return eventVolunteer;
    }

    public void setEventVolunteer(String eventVolunteer) {
        this.eventVolunteer = eventVolunteer;
    }

    public ArrayList<String> getEventImageUrls() {
        return eventImageUrls;
    }

    public void setEventImageUrls(ArrayList<String> eventImageUrls) {
        this.eventImageUrls = eventImageUrls;
    }

    public String getEventFoodType() {
        return eventFoodType;
    }

    public void setEventFoodType(String eventFoodType) {
        this.eventFoodType = eventFoodType;
    }

    public String getEventParticipantsCount() {
        return eventParticipantsCount;
    }

    public void setEventParticipantsCount(String eventParticipantsCount) {
        this.eventParticipantsCount = eventParticipantsCount;
    }

    public List<String> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(ArrayList<String> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public String getEventLat() {
        return eventLat;
    }

    public void setEventLat(String eventLat) {
        this.eventLat = eventLat;
    }

    public String getEventLong() {
        return eventLong;
    }

    public void setEventLong(String eventLong) {
        this.eventLong = eventLong;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
