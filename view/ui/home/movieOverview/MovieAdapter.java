package com.example.asg02.view.ui.home.movieOverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg02.model.Movie;
import org.jetbrains.annotations.NotNull;
import com.example.asg02.R;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    List<Movie> movieList = new ArrayList<>();
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }


    public class MovieHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name;
        TextView duration;
        TextView censor;
        public MovieHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            name = itemView.findViewById(R.id.movie_name);
            duration = itemView.findViewById(R.id.movie_duration);
            censor = itemView.findViewById(R.id.movie_censor);
        }

    }

    @NonNull
    @NotNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_item_movie, parent, false);

        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieAdapter.MovieHolder holder, int position) {
        holder.poster.setImageBitmap(ImageUtils.decodeBitmap(movieList.get(position).getPoster()));
        holder.poster.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }

        });
        holder.duration.setText(" " + DateTimeUtils.convertMinsToStringTime(movieList.get(position).getDurationMins()));
        holder.name.setText(movieList.get(position).getName().toUpperCase());
        holder.censor.setText(movieList.get(position).getCensor());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
