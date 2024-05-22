package com.example.asg02.controller.movie;

import com.example.asg02.model.Movie;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MovieReader {
    void getAllMovies(Consumer<Movie> onBookingAdded);
    CompletableFuture<Movie> getMovie(int movieId);
}
