package com.example.asg02.view.ui.home.movieOverview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.asg02.R;
import com.example.asg02.controller.movie.GetMovieController;
import com.example.asg02.databinding.FragmentCurrentMovieBinding;
import com.example.asg02.model.Movie;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.vm.BaseViewModel;
import com.example.asg02.vm.BookingViewModel;

import java.util.ArrayList;
import java.util.List;

public class CurrentMovieFragment extends Fragment {

    private FragmentCurrentMovieBinding binding;
    private BookingViewModel bookingViewModel;
    private BaseViewModel baseViewModel;
    private RecyclerView recyclerView;
    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private GetMovieController movieController;

    private int currPos = 0;

    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        baseViewModel = new ViewModelProvider(requireActivity()).get(BaseViewModel.class);
        binding = FragmentCurrentMovieBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerview;

        movieAdapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(movieAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                    if (centerView != null) {
                        int pos = recyclerView.getLayoutManager().getPosition(centerView);
                        if (pos != RecyclerView.NO_POSITION && pos != currPos) {
                            currPos = pos;
                        }
                    }
                }
            }
        });

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
                        if (!DateTimeUtils.isAfterToday(movie.getReleaseDate())) {
                            movieList.add(movie);
                        }
                    }
                    movieAdapter.notifyDataSetChanged();

                    binding.bookingButton.setOnClickListener(v -> {
                        NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                        bookingViewModel.setMovie(movieList.get(currPos));
                        controller.navigate(R.id.nav_select_complex);
                    });
                }
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}