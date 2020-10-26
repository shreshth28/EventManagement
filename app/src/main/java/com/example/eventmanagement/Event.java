package com.example.eventmanagement;

public class Event {
    private String eventName;
    private String imageLogoUrl;
    private String eventDescription;
    private String eventRegLink;
    private String eventUID;

    public String getEventName() {
        return eventName;
    }

    public String getImageLogoUrl() {
        return imageLogoUrl;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventRegLink() {
        return eventRegLink;
    }

    public String getEventUID() {
        return eventUID;
    }

    public Event(String eventName, String imageLogoUrl, String eventDescription, String eventRegLink, String eventUID) {
        this.eventName = eventName;
        this.imageLogoUrl = imageLogoUrl;
        this.eventDescription = eventDescription;
        this.eventRegLink = eventRegLink;
        this.eventUID = eventUID;
    }
}
