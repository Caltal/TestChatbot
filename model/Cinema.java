package com.example.asg02.model;

import java.io.Serializable;
import java.util.List;

public class Cinema implements Serializable {
    private int id;
    private String name;
    private String manager;
    private double latitude;
    private double longitude;
    private String detailAddress;

    public Cinema() {
    }

    public Cinema(int id, String name, String manager, double latitude, double longitude, String detailAddress) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailAddress = detailAddress;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
