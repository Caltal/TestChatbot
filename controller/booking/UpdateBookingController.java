package com.example.asg02.controller.booking;

import com.example.asg02.model.Booking;
import com.example.asg02.utils.FirebaseUtils;

public class UpdateBookingController implements BookingUpdater {

    @Override
    public void updateBooking(Booking booking) {
        FirebaseUtils.getDatabaseReference("Bookings").child(String.valueOf(booking.getId())).setValue(booking);
    }
}
