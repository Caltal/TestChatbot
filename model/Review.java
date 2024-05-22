package com.example.asg02.model;

public class Review {
    private String userId;
    private float rating;
    private String content;
    private long timestamp;

    public Review() {
    }

    public Review(String userId, float rating, String content, long timestamp) {
        this.userId = userId;
        this.rating = rating;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
