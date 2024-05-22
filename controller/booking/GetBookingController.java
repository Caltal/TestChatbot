package com.example.asg02.controller.booking;

import androidx.annotation.NonNull;

import com.example.asg02.model.Booking;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GetBookingController implements BookingReader {

    public GetBookingController() {}

    @Override
    public CompletableFuture<List<Booking>> getAllBookings() {
        CompletableFuture<List<Booking>> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Bookings").get().addOnSuccessListener(dataSnapshot -> {
            List<Booking> bookings = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Booking booking = snapshot.getValue(Booking.class);
                if (booking.getStatus() == Booking.STATUS_AVAILABLE) {
                    String dateTime = booking.getShow().getDate() + " " + booking.getShow().getEndTime();
                    if (!DateTimeUtils.isAfterNow(dateTime)) {
                        booking.setStatus(Booking.STATUS_EXPIRED);
                        new UpdateBookingController().updateBooking(booking);
                    }
                }
                bookings.add(booking);
            }
            future.complete(bookings);
        });
        return future;
    }


    @Override
    public void getBookings(String userId, Consumer<Booking> onBookingAdded) {
        FirebaseUtils.getDatabaseReference("Bookings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking booking = snapshot.getValue(Booking.class);
                    if (booking != null && booking.getUserID().equals(userId)) {
                        if (booking.getStatus() == Booking.STATUS_AVAILABLE) {
                            String dateTime = booking.getShow().getDate() + " " + booking.getShow().getEndTime();
                            if (!DateTimeUtils.isAfterNow(dateTime)) {
                                booking.setStatus(Booking.STATUS_EXPIRED);
                                new UpdateBookingController().updateBooking(booking);
                            }
                        }
                        onBookingAdded.accept(booking);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
