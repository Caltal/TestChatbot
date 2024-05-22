package com.example.asg02.view.ui.booking;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListBookingFragmentAdapter extends FragmentStateAdapter {
    List<Fragment> fragments = new ArrayList<>();
    List<String> headers = new ArrayList<>();

    public ListBookingFragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        init();
    }

    private void init() {
        fragments.add(new ListBookingDetailsFragment(false));
        headers.add("Vé còn hiệu lưc");
        fragments.add(new ListBookingDetailsFragment(true));
        headers.add("Vé hết hiệu lực");
    }
    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public String getHeader(int position) {
        return headers.get(position);
    }
}
