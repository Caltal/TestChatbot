package com.example.asg02.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Booking implements Serializable {
    private int id;
    private String userID;
    private Show show;
    private List<CarryItem> carryItems = new ArrayList<>();
    private List<String> seats = new ArrayList<>();
    private Payment payment;
    private int status = 0;

    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_EXPIRED = 1;
    public static final int STATUS_USED = 2;

    public Booking() {
    }

    public Booking(String userID, Show show, List<CarryItem> carryItems, List<String> seats, Payment payment) {
        this.userID = userID;
        this.show = show;
        this.carryItems = carryItems;
        this.seats = seats;
        this.payment = payment;
        id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<CarryItem> getCarryItems() {
        return carryItems;
    }

    public void setCarryItems(List<CarryItem> carryItems) {
        this.carryItems = carryItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
