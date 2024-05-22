package com.example.asg02.controller.cinema;

import com.example.asg02.model.Cinema;
import com.example.asg02.model.CinemaHall;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetCinemaController implements CinemaReader {

    public GetCinemaController() {
    }

    @Override
    public CompletableFuture<List<Cinema>> getAllCinemas(String manager) {
        CompletableFuture<List<Cinema>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Cinemas").get().addOnSuccessListener(dataSnapshot -> {
            List<Cinema> cinemas = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Cinema cinema = snapshot.getValue(Cinema.class);
                if (cinema.getManager().equals(manager)) {
                    cinemas.add(cinema);
                }
            }
            future.complete(cinemas);
        });
        return future;
    }

    @Override
    public CompletableFuture<Cinema> getCinema(int cinemaId) {
        CompletableFuture<Cinema> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Cinemas").child(String.valueOf(cinemaId)).get().addOnSuccessListener(dataSnapshot -> {
            Cinema cinema = dataSnapshot.getValue(Cinema.class);
            future.complete(cinema);
        });
        return future;
    }

    @Override
    public CompletableFuture<CinemaHall> getCinemaHall(int hallId) {
        CompletableFuture<CinemaHall> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Halls").child(String.valueOf(hallId)).get().addOnSuccessListener(dataSnapshot -> {
            CinemaHall hall = dataSnapshot.getValue(CinemaHall.class);
            future.complete(hall);
        });
        return future;
    }

    @Override
    public CompletableFuture<List<Cinema>> getAllCinemas() {
        CompletableFuture<List<Cinema>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Cinemas").get().addOnSuccessListener(dataSnapshot -> {
            List<Cinema> cinemas = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Cinema cinema = snapshot.getValue(Cinema.class);
                cinemas.add(cinema);
            }
            future.complete(cinemas);
        });
        return future;
    }

    @Override
    public CompletableFuture<List<CinemaHall>> getAllCinemaHalls() {
        CompletableFuture<List<CinemaHall>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Halls").get().addOnSuccessListener(dataSnapshot -> {
            List<CinemaHall> halls = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                CinemaHall hall = snapshot.getValue(CinemaHall.class);
                halls.add(hall);
            }
            future.complete(halls);
        });
        return future;
    }
}
