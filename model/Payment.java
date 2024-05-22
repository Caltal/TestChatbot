package com.example.asg02.model;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    private String token;
    private double totalAmount;
    private String createOn;

    public Payment() {
    }

    public Payment(String token, double totalAmount, String createOn) {
        this.token = token;
        this.totalAmount = totalAmount;
        this.createOn = createOn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }
}


