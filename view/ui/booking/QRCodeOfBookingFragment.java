package com.example.asg02.view.ui.booking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asg02.R;
import com.example.asg02.databinding.FragmentQRCodeOfBookingBinding;
import com.example.asg02.model.Booking;
import com.example.asg02.utils.ImageUtils;
import com.example.asg02.vm.BaseViewModel;
import com.example.asg02.vm.ListBookingViewModel;

public class QRCodeOfBookingFragment extends Fragment {
    private Booking booking;
    private ListBookingViewModel listBookingViewModel;
    private FragmentQRCodeOfBookingBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listBookingViewModel = new ViewModelProvider(requireActivity()).get(ListBookingViewModel.class);
        binding = FragmentQRCodeOfBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listBookingViewModel.getSelectedBooking().observe(
            getViewLifecycleOwner(),
            booking -> {
                this.booking = booking;
                updateUI();
            }
        );

        binding.finish.setOnClickListener(v -> {
            listBookingViewModel.setSelectedBooking(null);
            getParentFragmentManager().popBackStack();
        });

        return root;
    }

    private void updateUI() {
        if (booking == null) {
            return;
        }
        binding.code.setImageBitmap(ImageUtils.generateQRCode(String.valueOf(booking.getId()), 500, 500));
        binding.ticketCode.setText(String.valueOf(booking.getId()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}