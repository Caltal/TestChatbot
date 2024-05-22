package com.example.asg02.model;

public class Seat {
    private int id;
    private String seatRow;
    private int seatNumber;
    private int status;
    private double price;

    public static final int BOOKED = 1;
    public static final int AVAILABLE = 2;

    public Seat() {
    }

    public Seat(String seatRow, int seatNumber, int hallId, int showId, double price) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.price = price;
        status = AVAILABLE;
        id = hashCode();
    }

    public Seat(String seatRow, int seatNumber, int hallId, double price) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.price = price;
        status = AVAILABLE;
        id = hashCode();
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
