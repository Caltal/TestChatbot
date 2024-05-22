package com.example.asg02.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;

public class Show implements Serializable {
    private int id;
    private int movieId;
    private int cinemaId;
    private int hallId;
    private String Date;
    private String startTime;
    private String endTime;

    public Show() {
    }

    public Show(int id, int movieId, int cinemaId, int hallId, String date, String startTime, String endTime) {
        this.id = id;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.hallId = hallId;
        Date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
