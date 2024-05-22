package com.example.asg02.vm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asg02.controller.booking.GetBookingController;
import com.example.asg02.controller.cinema.GetCinemaController;
import com.example.asg02.controller.movie.GetMovieController;
import com.example.asg02.model.Booking;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.CinemaHall;
import com.example.asg02.model.Movie;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ListBookingViewModel extends ViewModel {
    MutableLiveData<List<Booking>> bookingList = new MutableLiveData<>();
    MutableLiveData<List<Cinema>> cinemaList = new MutableLiveData<>();
    MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();
    MutableLiveData<List<CinemaHall>> cinemaHall = new MutableLiveData<>();

    MediatorLiveData<Boolean> dataReady = new MediatorLiveData<>();

    MutableLiveData<Booking> selectedBooking = new MutableLiveData<>();
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ListBookingViewModel() {
        reloadBookingData();

        dataReady.addSource(bookingList, value -> checkDataReady());
        dataReady.addSource(cinemaList, value -> checkDataReady());
        dataReady.addSource(movieList, value -> checkDataReady());
        dataReady.addSource(cinemaHall, value -> checkDataReady());

        FirebaseUtils.getDatabaseReference("Bookings").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        reloadBookingData();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        reloadBookingData();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        reloadBookingData();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void reloadBookingData() {
        new Thread(() -> {
            if (userId == null) {
                return;
            }
            List<Booking> bookings = new ArrayList<>();
            List<Cinema> cinemas = new ArrayList<>();
            List<Movie> movies = new ArrayList<>();
            List<CinemaHall> halls = new ArrayList<>();

            GetBookingController bookingController = new GetBookingController();
            GetCinemaController cinemaController = new GetCinemaController();
            GetMovieController movieController = new GetMovieController();

            bookingController.getBookings(userId, booking -> {
                bookings.add(booking);
                CompletableFuture<Movie> movieFuture = movieController.getMovie(booking.getShow().getMovieId());
                CompletableFuture<Cinema> cinemaFuture = cinemaController.getCinema(booking.getShow().getCinemaId());
                CompletableFuture<CinemaHall> hallFuture = cinemaController.getCinemaHall(booking.getShow().getHallId());

                CompletableFuture.allOf(movieFuture, cinemaFuture, hallFuture).thenRun(() -> {
                    movieFuture.thenAccept(movies::add);
                    cinemaFuture.thenAccept(cinemas::add);
                    hallFuture.thenAccept(halls::add);

                    bookingList.postValue(bookings);
                    movieList.postValue(movies);
                    cinemaList.postValue(cinemas);
                    cinemaHall.postValue(halls);
                });
            });
        }).start();
    }


    public void checkDataReady() {
        if (bookingList == null || cinemaList == null || movieList == null || cinemaHall == null) {
            dataReady.postValue(false);
            return;
        }

        if (bookingList.getValue() == null || cinemaList.getValue() == null || movieList.getValue() == null || cinemaHall.getValue() == null) {
            dataReady.postValue(false);
            return;
        }
        int size = bookingList.getValue().size();
        if (size == cinemaList.getValue().size()
                && size == movieList.getValue().size()
                    && size == cinemaHall.getValue().size()) {
            dataReady.postValue(true);
            return;
        }
        dataReady.postValue(false);
    }

    public void setMovieList(List<Movie> movies) {
        movieList.postValue(movies);
    }

    public void setCinemaList(List<Cinema> cinemas) {
        cinemaList.postValue(cinemas);
    }

    public void setCinemaHallList(List<CinemaHall> halls) {
        cinemaHall.postValue(halls);
    }

    public MutableLiveData<List<Movie>> getMovieList() {
        return movieList;
    }

    public MutableLiveData<List<Cinema>> getCinemaList() {
        return cinemaList;
    }

    public MutableLiveData<List<CinemaHall>> getCinemaHallList() {
        return cinemaHall;
    }


    public void setBookingList(List<Booking> bookings) {
        bookingList.postValue(bookings);
    }

    public void setSelectedBooking(Booking booking) {
        selectedBooking.postValue(booking);
    }

    public MutableLiveData<List<Booking>> getBookingList() {
        return bookingList;
    }

    public MutableLiveData<Booking> getSelectedBooking() {
        return selectedBooking;
    }

    public MediatorLiveData<Boolean> isDataReady() {
        return dataReady;
    }

    public void clearData() {
        bookingList.postValue(null);
        cinemaList.postValue(null);
        movieList.postValue(null);
        cinemaHall.postValue(null);
        selectedBooking.postValue(null);
    }
}
