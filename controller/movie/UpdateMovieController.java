package com.example.asg02.controller.movie;

import com.example.asg02.model.Movie;
import com.example.asg02.utils.FirebaseUtils;

public class UpdateMovieController implements MovieUpdater {
    @Override
    public void updateMovie(Movie movie) {
        FirebaseUtils.getDatabaseReference("Movies").child(String.valueOf(movie.getId())).setValue(movie);
    }
}
