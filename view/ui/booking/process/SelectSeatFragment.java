package com.example.asg02.view.ui.booking.process;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asg02.R;
import com.example.asg02.controller.booking.GetBookingController;
import com.example.asg02.controller.cinema.GetCinemaController;
import com.example.asg02.databinding.FragmentSelectSeatBinding;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.Manager;
import com.example.asg02.model.Movie;
import com.example.asg02.model.Seat;
import com.example.asg02.model.Show;
import com.example.asg02.utils.StringUtils;
import com.example.asg02.vm.BookingViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectSeatFragment extends Fragment implements View.OnClickListener {
    private Show show;

    private BookingViewModel bookingViewModel;
    private FragmentSelectSeatBinding binding;
    private int row;
    private int col;
    private List<String> selectedSeat;
    List<TextView> seatViews = new ArrayList<>();
    List<Seat> seatList;
    int seatSize = 100;
    int seatGaping = 10;
    private double totalPrice;

    ViewGroup viewGroup;
    private GetCinemaController getCinemaController;
    private GetBookingController getBookingController;
    public static final double PRICE_VIP = 100;
    public static final double PRICE_NORMAL = 75;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        binding = FragmentSelectSeatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        selectedSeat = new ArrayList<>();
        bookingViewModel.setSeats(null);


        totalPrice = 0;

        getCinemaController = new GetCinemaController();
        getBookingController = new GetBookingController();

        bookingViewModel.isSelectSeatsReady().observe(
                getViewLifecycleOwner(),
                selectSeatsReady -> {
                    if (selectSeatsReady) {
                        show = bookingViewModel.getShow().getValue();
                        updateUI();
                    }
                }
        );

        binding.bookingButton.setOnClickListener(v -> {
            if (selectedSeat.size() == 0) {
                Snackbar.make(v, "Vui lòng chọn ghế", Snackbar.LENGTH_LONG).show();
            } else {
                Collections.sort(selectedSeat);
                bookingViewModel.setSeats(selectedSeat);
                bookingViewModel.setTotalPrice(totalPrice);
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_select_seat_to_nav_pay);
            }
        });



        return root;

    }

    private void updateUI() {
        if (show == null) {
            return;
        }
        getCinemaController.getCinemaHall(show.getHallId()).thenAccept(hall -> {
            if (hall != null) {
                row = hall.getSeatsPerRow();
                col = hall.getSeatsPerColumn();

                seatList = new ArrayList<>();
                for (int i = 0; i < row * col; i++) {
                    String sRow = String.valueOf((char) ('A' + i / col));
                    Seat seat = new Seat(sRow, i % col + 1, 0, 1);
                    if ((i / col) >= row / 2) {
                        seat.setPrice(PRICE_VIP);
                    } else {
                        seat.setPrice(PRICE_NORMAL);
                    }
                    seatList.add(seat);
                }

                getBookingController.getAllBookings().thenAccept(bookings -> {
                    if (bookings != null) {
                        for (int i = 0; i < bookings.size(); i++) {
                            if (bookings.get(i).getShow().getId() != show.getId()) {
                                continue;
                            }
                            List<String> bookedSeats = bookings.get(i).getSeats();
                            for (String s : bookedSeats) {
                                for (Seat seat__ : seatList) {
                                    if ((seat__.getSeatRow() + seat__.getSeatNumber()).equals(s)) {
                                        seat__.setStatus(Seat.BOOKED);
                                    }
                                }
                            }
                        }

                        //set UI
                        viewGroup = binding.layoutSeat;

                        LinearLayout layoutSeat = new LinearLayout(getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutSeat.setOrientation(LinearLayout.VERTICAL);
                        layoutSeat.setLayoutParams(params);
                        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
                        viewGroup.addView(layoutSeat);

                        LinearLayout layout;

                        int count = 0;

                        for (int i = 0; i < row; i++) {
                            layout = new LinearLayout(getContext());
                            layout.setOrientation(LinearLayout.HORIZONTAL);
                            layoutSeat.addView(layout);

                            for (int j = 0; j < col; j++) {
                                Seat s = seatList.get(count);
                                TextView seat = new TextView(getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                seat.setLayoutParams(layoutParams);
                                seat.setPadding(seatGaping, 0, seatGaping, 2 * seatGaping);
                                seat.setGravity(Gravity.CENTER);
                                seat.setId(s.getId());
                                if (s.getPrice() > 75){
                                    seat.setHint("VIP");
                                } else {
                                    seat.setHint("Normal");
                                }
                                seat.setText("" + s.getSeatRow().toUpperCase() + s.getSeatNumber());
                                seat.setTag(s.getStatus());
                                switch (s.getStatus()) {
                                    case Seat.AVAILABLE:
                                        if (s.getPrice() > 75) {
                                            seat.setBackgroundResource(R.drawable.ic_seats_available_vip);
                                        } else {
                                            seat.setBackgroundResource(R.drawable.ic_seats_available);
                                        }
                                        seat.setTextColor(getResources().getColor(R.color.black));
                                        break;
                                    case Seat.BOOKED:
                                        seat.setBackgroundResource(R.drawable.ic_seats_booked);
                                        seat.setTextColor(getResources().getColor(R.color.white));
                                        break;
                                }

                                layout.addView(seat);
                                seatViews.add(seat);
                                seat.setOnClickListener(this);
                                count++;
                            }
                        }
                        // end set UI
                    }
                });
            }
        });

    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        for (Seat seat : seatList) {
            if (seat.getId() == v.getId()) {
                if ((int) v.getTag() == Seat.AVAILABLE) {
                    String nameSeat = String.format("%s%d", seat.getSeatRow(), seat.getSeatNumber());
                    if (selectedSeat.contains(nameSeat)) {
                        selectedSeat.remove(nameSeat);
                        if (((TextView) v).getHint().equals("VIP")) {
                            v.setBackgroundResource(R.drawable.ic_seats_available_vip);
                            totalPrice -= 100;
                        } else {
                            v.setBackgroundResource(R.drawable.ic_seats_available);
                            totalPrice -= 75;
                        }
                        ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                    } else {
                        totalPrice += seat.getPrice();
                        selectedSeat.add(nameSeat);
                        v.setBackgroundResource(R.drawable.ic_seats_selected);
                        ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    }
                } else if ((int) v.getTag() == Seat.BOOKED) {
                    Snackbar.make(v, "Ghế đã được đặt, vui lòng chọn ghế khác", Snackbar.LENGTH_LONG).show();
                }
                binding.totalPrice.setText(StringUtils.formatMoney(totalPrice * 1000) + "đ");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}