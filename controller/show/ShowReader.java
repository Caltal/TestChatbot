package com.example.asg02.controller.show;

import com.example.asg02.model.Show;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ShowReader {
    CompletableFuture<List<Show>> getAllShowsByCinema(int cinemaId);
    CompletableFuture<List<Show>> getAllShowsByCinemaAndMovie(int cinemaId, int movieId);
    CompletableFuture<List<Show>> getAllShowsByCinemaAndMovie(int cinemaId, int movieId, String date);
}
