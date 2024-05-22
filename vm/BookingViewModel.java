package com.example.asg02.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asg02.model.Cinema;
import com.example.asg02.model.Manager;
import com.example.asg02.model.Movie;
import com.example.asg02.model.Show;
import com.example.asg02.model.User;

import java.util.List;

public class BookingViewModel extends ViewModel {
    private MutableLiveData<Manager> manager = new MutableLiveData<>();
    private MutableLiveData<Cinema> cinema = new MutableLiveData<>();
    private MutableLiveData<Movie> movie = new MutableLiveData<>();
    private MutableLiveData<Show> show = new MutableLiveData<>();
    private MutableLiveData<List<String>> seats = new MutableLiveData<>();
    private MutableLiveData<Double> totalPrice = new MutableLiveData<>();

    // Mediator
    private MediatorLiveData<Boolean> selectCinemaAndShowReady = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> selectSeatsReady = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> payReady = new MediatorLiveData<>();

    public BookingViewModel() {
        selectCinemaAndShowReady.addSource(manager, value -> checkSelectCinemaAndShowReady());
        selectCinemaAndShowReady.addSource(movie, value -> checkSelectCinemaAndShowReady());

        selectSeatsReady.addSource(show, value -> checkSelectSeatsReady());

        payReady.addSource(manager, value -> checkPayReady());
        payReady.addSource(movie, value -> checkPayReady());
        payReady.addSource(cinema, value -> checkPayReady());
        payReady.addSource(show, value -> checkPayReady());
        payReady.addSource(seats, value -> checkPayReady());
        payReady.addSource(totalPrice, value -> checkPayReady());
    }

    private void checkSelectCinemaAndShowReady() {
        if (manager.getValue() != null && movie.getValue() != null) {
            selectCinemaAndShowReady.setValue(true);
            return;
        }
        selectCinemaAndShowReady.setValue(false);
    }

    private void checkSelectSeatsReady() {
        if (show.getValue() != null) {
            selectSeatsReady.setValue(true);
            return;
        }
        selectSeatsReady.setValue(false);
    }

    private void checkPayReady() {
        if (manager.getValue() != null && movie.getValue() != null
                && cinema.getValue() != null && show.getValue() != null
                    && seats.getValue() != null && totalPrice.getValue() != null){
            payReady.setValue(true);
            return;
        }
        payReady.setValue(false);
    }

    public LiveData<Manager> getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager.setValue(manager);
    }

    public LiveData<Cinema> getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema.setValue(cinema);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie.setValue(movie);
    }

    public LiveData<Show> getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show.setValue(show);
    }

    public LiveData<List<String>> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats.setValue(seats);
    }

    public LiveData<Double> getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.setValue(totalPrice);
    }
    public void clearData() {
        manager.setValue(null);
        cinema.setValue(null);
        movie.setValue(null);
        show.setValue(null);
        seats.setValue(null);
        totalPrice.setValue(null);

        selectCinemaAndShowReady.setValue(false);
        selectSeatsReady.setValue(false);
        payReady.setValue(false);
    }

    public LiveData<Boolean> isSelectCinemaAndShowReady() {
        return selectCinemaAndShowReady;
    }

    public LiveData<Boolean> isSelectSeatsReady() {
        return selectSeatsReady;
    }

    public LiveData<Boolean> isPayReady() {
        return payReady;
    }
}
