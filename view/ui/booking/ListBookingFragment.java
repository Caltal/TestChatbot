package com.example.asg02.view.ui.booking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asg02.R;
import com.example.asg02.databinding.FragmentListBookingBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ListBookingFragment extends Fragment {

    FragmentListBookingBinding binding;
    private TabLayoutMediator mediator;
    private ListBookingFragmentAdapter fragmentAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentAdapter = new ListBookingFragmentAdapter(fragmentManager, getLifecycle());

        mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPaper2, (tab, pos) -> {
            tab.setText(fragmentAdapter.getHeader(pos));
        });

        binding.viewPaper2.setAdapter(fragmentAdapter);
        binding.viewPaper2.setUserInputEnabled(false);

        if (!mediator.isAttached()) {
            mediator.attach();
        }

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}