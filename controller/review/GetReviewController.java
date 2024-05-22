package com.example.asg02.controller.review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asg02.model.Review;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GetReviewController implements ReviewReader {
    @Override
    public void getReviewsOfMovie(int movieId, Consumer<Review> onReviewAdded) {
        FirebaseUtils.getDatabaseReference("Reviews")
                .child(String.valueOf(movieId))
                .orderByChild("timestamp")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Review review = snapshot.getValue(Review.class);
                        if (review != null) {
                            onReviewAdded.accept(review);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
