package com.example.asg02.view.ui.movie;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.util.Log;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.asg02.R;
import com.example.asg02.controller.account.UpdateAccountController;
import com.example.asg02.controller.movie.UpdateMovieController;
import com.example.asg02.controller.review.CreateReviewController;
import com.example.asg02.controller.review.GetReviewController;
import com.example.asg02.databinding.FragmentMovieDetailsBinding;
import com.example.asg02.model.Movie;
import com.example.asg02.model.Review;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.utils.ImageUtils;
import com.example.asg02.utils.StringUtils;
import com.example.asg02.utils.ViewUtils;
import com.example.asg02.view.MainActivity;
import com.example.asg02.view.ui.movie.review.ReviewAdapter;
import com.example.asg02.vm.AccountViewModel;
import com.example.asg02.vm.BookingViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private BookingViewModel bookingViewModel;
    private FragmentMovieDetailsBinding binding;
    private Movie movie;

    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private GetReviewController getReviewController;
    private CreateReviewController createReviewController;

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews = new ArrayList<>();
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getReviewController = new GetReviewController();
        createReviewController = new CreateReviewController();

        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewAdapter = new ReviewAdapter();
        reviewAdapter.submitList(reviews);
        recyclerView.setAdapter(reviewAdapter);

        bookingViewModel.getMovie().observe(
                getViewLifecycleOwner(),
                movie -> {
                    this.movie = movie;
                    updateUI();
                }

        );

        getReviewController.getReviewsOfMovie(bookingViewModel.getMovie().getValue().getId()
                , review -> {
                    reviews.add(review);
                    reviewAdapter.notifyDataSetChanged();
                });


        binding.bookingButton.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            controller.navigate(R.id.nav_select_complex);
        });

        binding.sendReview.setOnClickListener(v -> {
            ViewUtils.createAskingDialog("Bạn có chắc chắn muốn gửi đánh giá này không?"
                    , requireContext()
                    , v_ -> {
                        Review review = new Review();
                        review.setUserId(accountViewModel.getUserId().getValue());
                        review.setContent(binding.review.getText().toString());
                        review.setRating(binding.ratingBar.getRating());
                        review.setTimestamp(System.currentTimeMillis());

                        binding.review.setText("");

                        float totalRating = 0;
                        for (Review r : reviews) {
                            totalRating += r.getRating();
                        }
                        totalRating += review.getRating();

                        movie.setRating(totalRating / (reviews.size() + 1));
                        bookingViewModel.setMovie(movie);
                        createReviewController.createReview(movie.getId(), review);
                        new UpdateMovieController().updateMovie(movie);
                        Snackbar.make(binding.review, "Đánh giá của bạn đã được gửi!", Snackbar.LENGTH_SHORT).show();
                    }).show();


        });

        binding.linearLayout.setOnTouchListener((v, event) -> {
            ((MainActivity) getActivity()).hideKeyboard();
            return false;
        });

        return root;
    }

    private void updateUI() {
        if (movie == null) {
            return;
        }

        binding.movieName.setText(movie.getName().toUpperCase());
        binding.movieRating.setRating(movie.getRating());
        binding.movieRating.setOnTouchListener((v, event) -> true);
        binding.movieDuration.setText(DateTimeUtils.convertMinsToStringTime(movie.getDurationMins()));
        binding.releaseDate.setText(movie.getReleaseDate());
        binding.genre.setText(movie.getGenre());
        binding.language.setText(movie.getLanguage());
        binding.description.setText(movie.getDescription());
        binding.actors.setText(movie.getActors());
        binding.director.setText(movie.getDirector());
        binding.censor.setText(movie.getCensor() + " - " + StringUtils.generateDetailsOfCensor(movie.getCensor()));
        binding.poster.setImageBitmap(ImageUtils.decodeBitmap(movie.getPoster()));

        String trailerLink = movie.getTrailerLink();
        String embedLink = StringUtils.createEmbedLinkFromYoutube(trailerLink);
        WebView webView = binding.trailerVideo;
        webView.loadData(embedLink, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        binding.bookingButton.setVisibility(
                DateTimeUtils.isAfterToday(movie.getReleaseDate()) ? View.GONE : View.VISIBLE
        );
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}