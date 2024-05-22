package com.example.asg02.controller.review;

import com.example.asg02.model.Review;
import com.example.asg02.utils.FirebaseUtils;

public class CreateReviewController implements ReviewCreator {
    @Override
    public void createReview(int movieId, Review review) {
        FirebaseUtils.getDatabaseReference("Reviews")
                .child(String.valueOf(movieId))
                .push()
                .setValue(review);
    }
}
