package com.example.asg02.model;

import java.io.Serializable;

public class Event implements Serializable {
    private int id;
    private String eventName;
    private String startDate;
    private String endDate;
    private String eventInfo;
    private String poster;

    public Event() {
    }

    public Event(int id, String eventName, String startDate, String endDate, String eventInfo, String poster) {
        this.id = id;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventInfo = eventInfo;
        this.poster = poster;
    }

    public Event(int id, String eventName, String startDate, String endDate, String eventInfo) {
        this.id = id;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventInfo = eventInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
