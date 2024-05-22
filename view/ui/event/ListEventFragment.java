package com.example.asg02.view.ui.event;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asg02.R;
import com.example.asg02.databinding.FragmentListEventsBinding;
import com.example.asg02.model.Event;
import com.example.asg02.vm.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListEventFragment extends Fragment {
    private FragmentListEventsBinding binding;
    private BaseViewModel baseViewModel;
    private RecyclerView recyclerView;
    private ListEventAdapter adapter;
    private List<Event> events = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseViewModel = new ViewModelProvider(requireActivity()).get(BaseViewModel.class);
        binding = FragmentListEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerview;
        adapter = new ListEventAdapter(events);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            Event event = events.get(position);
            baseViewModel.setSelectedEvent(event);
            NavController navController = Navigation.findNavController(requireActivity()
                    , R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_events_detail);
        });

        baseViewModel.getEventList().observe(
                getViewLifecycleOwner(),
                events -> {
                    this.events.clear();
                    this.events.addAll(events);
                    adapter.notifyDataSetChanged();
                }
        );

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}