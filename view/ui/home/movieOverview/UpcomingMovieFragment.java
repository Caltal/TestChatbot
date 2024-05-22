package com.example.asg02.view.ui.home.movieOverview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.asg02.R;
import com.example.asg02.controller.movie.GetMovieController;
import com.example.asg02.databinding.FragmentUpcomingMovieBinding;
import com.example.asg02.model.Movie;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.vm.BaseViewModel;
import com.example.asg02.vm.BookingViewModel;

import java.util.ArrayList;
import java.util.List;

public class UpcomingMovieFragment extends Fragment {
    private BookingViewModel bookingViewModel;
    private BaseViewModel baseViewModel;
    private FragmentUpcomingMovieBinding binding;
    private RecyclerView recyclerView;
    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        baseViewModel = new ViewModelProvider(requireActivity()).get(BaseViewModel.class);

        binding = FragmentUpcomingMovieBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        movieAdapter = new MovieAdapter(movieList);

        recyclerView = binding.recyclerview;
        recyclerView.setAdapter(movieAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        movieAdapter.setOnItemClickListener(pos -> {
            NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            bookingViewModel.setMovie(movieList.get(pos));
            controller.navigate(R.id.action_nav_home_to_nav_movie_details);

        });

        baseViewModel.getMovieList().observe(
                getViewLifecycleOwner(),
                movies -> {
                    movieList.clear();
                    for (Movie movie : movies) {
                        if (DateTimeUtils.isAfterToday(movie.getReleaseDate())) {
                            movieList.add(movie);
                        }
                    }

                    movieAdapter.notifyDataSetChanged();
                }
        );

        return root;
    }
}