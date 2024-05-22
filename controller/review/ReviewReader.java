package com.example.asg02.controller.review;

import com.example.asg02.model.Review;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface ReviewReader {
    void getReviewsOfMovie(int movieId, Consumer<Review> onReviewAdded);
}
