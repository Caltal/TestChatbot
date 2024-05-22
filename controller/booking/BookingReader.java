package com.example.asg02.controller.booking;

import com.example.asg02.model.Booking;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface BookingReader {
    CompletableFuture<List<Booking>> getAllBookings();
    void getBookings(String userId, Consumer<Booking> onBookingAdded);
}
