package com.example.asg02.controller.cinema;

import com.example.asg02.model.Cinema;
import com.example.asg02.model.CinemaHall;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CinemaReader {
    CompletableFuture<List<Cinema>> getAllCinemas(String managerId);
    CompletableFuture<CinemaHall> getCinemaHall(int hallId);
    CompletableFuture<Cinema> getCinema(int cinemaId);
    CompletableFuture<List<Cinema>> getAllCinemas();
    CompletableFuture<List<CinemaHall>> getAllCinemaHalls();
}
