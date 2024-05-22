package com.example.asg02.controller.show;

import com.example.asg02.model.Show;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetShowController implements ShowReader {

    public GetShowController() {
    }

    @Override
    public CompletableFuture<List<Show>> getAllShowsByCinema(int cinemaId) {
        CompletableFuture<List<Show>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Shows").get().addOnSuccessListener(dataSnapshot -> {
            List<Show> shows = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Show show = snapshot.getValue(Show.class);
                if (show.getCinemaId() == cinemaId) {
                    shows.add(show);
                }
            }
            future.complete(shows);
        });
        return future;
    }

    @Override
    public CompletableFuture<List<Show>> getAllShowsByCinemaAndMovie(int cinemaId, int movieId) {
        CompletableFuture<List<Show>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Shows").get().addOnSuccessListener(dataSnapshot -> {
            List<Show> shows = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Show show = snapshot.getValue(Show.class);
                if (show.getCinemaId() == cinemaId && show.getMovieId() == movieId) {
                    shows.add(show);
                }
            }
            future.complete(shows);
        });
        return future;
    }

    @Override
    public CompletableFuture<List<Show>> getAllShowsByCinemaAndMovie(int cinemaId, int movieId, String date) {
        CompletableFuture<List<Show>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Shows").get().addOnSuccessListener(dataSnapshot -> {
            List<Show> shows = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Show show = snapshot.getValue(Show.class);
                if (show.getCinemaId() == cinemaId && show.getMovieId() == movieId && show.getDate().equals(date)) {
                    shows.add(show);
                }
            }
            future.complete(shows);
        });
        return future;
    }
}
