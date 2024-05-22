package com.example.asg02.view.ui.map;

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
import com.example.asg02.controller.cinema.GetCinemaController;
import com.example.asg02.databinding.FragmentSelectCinemaMapBinding;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.Manager;
import com.example.asg02.vm.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectCinemaFragment extends Fragment {
    private MapViewModel mapViewModel;
    private FragmentSelectCinemaMapBinding binding;
    private Manager manager;
    private GetCinemaController getCinemaController;
    private List<Cinema> cinemas = new ArrayList<>();
    private RecyclerView recyclerView;
    private CinemaAdapter cinemaAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        binding = FragmentSelectCinemaMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerview;
        cinemaAdapter = new CinemaAdapter(cinemas, mapViewModel);
        recyclerView.setAdapter(cinemaAdapter);

        cinemaAdapter.setOnItemClickListener(
                position -> {
                    mapViewModel.setCinema(cinemas.get(position));
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.action_nav_select_cinema_map_to_nav_maps);
                }
        );

        getCinemaController = new GetCinemaController();

        mapViewModel.getManager().observe(
                getViewLifecycleOwner(),
                manager -> {
                    this.manager = manager;
                    updateUI();
                }
        );

        return root;
    }

    private void updateUI() {
        if (manager == null) return;

        getCinemaController.getAllCinemas(manager.getEmail()).thenAccept(
                cinemas -> {
                    if (cinemas != null) {
                        this.cinemas.clear();
                        this.cinemas.addAll(cinemas);
                        cinemaAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}