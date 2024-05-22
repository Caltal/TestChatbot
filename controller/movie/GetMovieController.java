package com.example.asg02.controller.movie;

import androidx.annotation.NonNull;

import com.example.asg02.model.Movie;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GetMovieController implements MovieReader {

    public GetMovieController() {
    }

    @Override
    public void getAllMovies(Consumer<Movie> onBookingAdded) {
        FirebaseUtils.getDatabaseReference("Movies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    if (movie != null) {
                        onBookingAdded.accept(movie);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public CompletableFuture<Movie> getMovie(int movieId) {
        CompletableFuture<Movie> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Movies").child(String.valueOf(movieId)).get().addOnSuccessListener(dataSnapshot -> {
            Movie movie = dataSnapshot.getValue(Movie.class);
            future.complete(movie);
        });
        return future;
    }
}
