package com.example.asg02.vm;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asg02.controller.account.GetAllManagerController;
import com.example.asg02.controller.booking.GetBookingController;
import com.example.asg02.controller.cinema.GetCinemaController;
import com.example.asg02.controller.event.GetAllEventController;
import com.example.asg02.controller.movie.GetMovieController;
import com.example.asg02.model.Account;
import com.example.asg02.model.Booking;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.CinemaHall;
import com.example.asg02.model.Event;
import com.example.asg02.model.Manager;
import com.example.asg02.model.Movie;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class BaseViewModel extends ViewModel {
    MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();
    MutableLiveData<List<Event>> eventList = new MutableLiveData<>();
    MutableLiveData<Event> selectedEvent = new MutableLiveData<>();


    public BaseViewModel() {
        reLoadMovieData();
        reLoadEventData();

        FirebaseUtils.getDatabaseReference("Movies").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        reLoadMovieData();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        reLoadMovieData();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        reLoadMovieData();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        FirebaseUtils.getDatabaseReference("Events").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        reLoadEventData();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        reLoadEventData();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        reLoadEventData();
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

    public LiveData<List<Movie>> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList.setValue(movieList);
    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList.setValue(eventList);
    }

    public LiveData<Event> getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event event) {
        selectedEvent.setValue(event);
    }

    public void clearData() {
        movieList.setValue(null);
        eventList.setValue(null);
        selectedEvent.setValue(null);
    }

    public void reLoadMovieData() {
        new Thread(() -> {
            List<Movie> movies = new ArrayList<>();
            new GetMovieController().getAllMovies(movie -> {
                movies.add(movie);
                movieList.postValue(movies);
            });
        }).start();
    }

    public void reLoadEventData() {
        List<Event> events = new ArrayList<>();
        new GetAllEventController().getAllEvents(event -> {
            events.add(event);
            eventList.postValue(events);
        });
    }


}
