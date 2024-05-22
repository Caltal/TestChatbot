package com.example.asg02.model;

public class CinemaHall {
    private String name;
    private int id;
    private int cinemaId;
    private int seatsPerRow;
    private int seatsPerColumn;

    public CinemaHall() {
    }

    public CinemaHall(String name, int cinemaId, int seatsPerRow, int seatsPerCol) {
        this.name = name;
        this.cinemaId = cinemaId;
        this.seatsPerRow = seatsPerRow;
        this.seatsPerColumn = seatsPerCol;
        id = hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public int getSeatsPerColumn() {
        return seatsPerColumn;
    }

    public void setSeatsPerColumn(int seatsPerColumn) {
        this.seatsPerColumn = seatsPerColumn;
    }
}
