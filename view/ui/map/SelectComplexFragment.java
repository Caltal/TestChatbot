package com.example.asg02.view.ui.map;

import android.os.Bundle;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.asg02.R;
import com.example.asg02.controller.account.GetAllManagerController;
import com.example.asg02.databinding.FragmentSelectComplexBinding;
import com.example.asg02.model.Account;
import com.example.asg02.model.Manager;
import com.example.asg02.model.Movie;
import com.example.asg02.view.ui.booking.process.adapter.GridViewAdapter;
import com.example.asg02.vm.BookingViewModel;
import com.example.asg02.vm.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectComplexFragment extends Fragment {
    private MapViewModel mapViewModel;
    private FragmentSelectComplexBinding binding;
    private GridViewAdapter adapter;
    private List<Manager> managers = new ArrayList<>();
    private GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        binding = FragmentSelectComplexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        gridView = binding.gridView;

        new GetAllManagerController().getAllManagers().thenAccept(accounts -> {
            if (accounts != null) {
                managers.clear();

                for (Account a : accounts) {
                    Manager m = (Manager) a;
                    managers.add(m);
                }

                adapter = new GridViewAdapter(getContext(), managers);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener((parent, view, position, id) -> {
                    Manager manager = managers.get(position);
                    mapViewModel.setManager(manager);
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.action_nav_select_complex_map_to_nav_select_cinema_map);
                });
            }
        });
        return root;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}