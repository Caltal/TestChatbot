package com.example.asg02.view.ui.booking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asg02.R;
import com.example.asg02.controller.booking.GetBookingController;
import com.example.asg02.controller.booking.UpdateBookingController;
import com.example.asg02.databinding.FragmentListBookingDetailsBinding;
import com.example.asg02.model.Booking;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.CinemaHall;
import com.example.asg02.model.Movie;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.utils.ViewUtils;
import com.example.asg02.vm.AccountViewModel;
import com.example.asg02.vm.BaseViewModel;
import com.example.asg02.vm.ListBookingViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListBookingDetailsFragment extends Fragment {
    private boolean isExpired = false;
    private AccountViewModel accountViewModel;
    private ListBookingViewModel listBookingViewModel;
    private FragmentListBookingDetailsBinding binding;

    private List<Booking> bookingList = new ArrayList<>();
    private List<Movie> movieList = new ArrayList<>();
    private List<Cinema> cinemaList = new ArrayList<>();
    private List<CinemaHall> cinemaHallList = new ArrayList<>();

    private BookingAdapter bookingAdapter;

    private RecyclerView recyclerView;
    private String userId;

    public ListBookingDetailsFragment(boolean isExpired) {
        this.isExpired = isExpired;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        listBookingViewModel = new ViewModelProvider(requireActivity()).get(ListBookingViewModel.class);

        binding = FragmentListBookingDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerview;
        bookingAdapter = new BookingAdapter(bookingList, movieList, cinemaList, cinemaHallList);
        recyclerView.setAdapter(bookingAdapter);

        bookingAdapter.setOnItemClickListener(position -> {
            Booking booking = bookingList.get(position);
            ViewUtils.createAskingDialog("Bạn chỉ có một lần sử dụng. Bạn có chắc chắn muốn sử dụng vé này không?"
                    , requireContext()
                    , v -> {
                        booking.setStatus(Booking.STATUS_USED);
                        listBookingViewModel.setSelectedBooking(booking);
                        new UpdateBookingController().updateBooking(booking);
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.action_nav_listBooking_to_nav_qr_code_of_booking);
                    }
            ).show();
        });

        accountViewModel.getUserId().observe(
                getViewLifecycleOwner(),
                userId -> {
                    this.userId = userId;
                    updateUI();
                }

        );

        return root;
    }

    private void updateUI() {
        if (userId == null) {
            return;
        }
        listBookingViewModel.setUserId(userId);
        listBookingViewModel.isDataReady().observe(
                getViewLifecycleOwner(),
                isReady -> {
                    if (isReady) {
                        bookingList.clear();
                        movieList.clear();
                        cinemaList.clear();
                        cinemaHallList.clear();

                        List<Booking> bookings = listBookingViewModel.getBookingList().getValue();
                        for (int i = 0; i < bookings.size(); i++) {
                            String dateTime = bookings.get(i).getShow().getDate() + " " + bookings.get(i).getShow().getEndTime();
                            if (isExpired) {
                                if (!DateTimeUtils.isAfterNow(dateTime)) {
                                    bookingList.add(bookings.get(i));
                                    movieList.add(listBookingViewModel.getMovieList().getValue().get(i));
                                    cinemaList.add(listBookingViewModel.getCinemaList().getValue().get(i));
                                    cinemaHallList.add(listBookingViewModel.getCinemaHallList().getValue().get(i));
                                }
                            } else {
                                if (DateTimeUtils.isAfterNow(dateTime)) {
                                    bookingList.add(bookings.get(i));
                                    movieList.add(listBookingViewModel.getMovieList().getValue().get(i));
                                    cinemaList.add(listBookingViewModel.getCinemaList().getValue().get(i));
                                    cinemaHallList.add(listBookingViewModel.getCinemaHallList().getValue().get(i));
                                }
                            }
                        }
                        bookingAdapter.notifyDataSetChanged();
                    }
                }
        );
    }
}