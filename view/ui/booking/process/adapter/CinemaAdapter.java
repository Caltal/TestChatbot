package com.example.asg02.view.ui.booking.process.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.asg02.R;
import com.example.asg02.controller.show.GetShowController;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.Show;
import com.example.asg02.utils.MapsUtils;
import com.example.asg02.view.ui.map.MapsFragment;
import com.example.asg02.vm.MapViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaHolder> {

    private List<Cinema> cinemas;
    private List<List<Show>> shows = new ArrayList<>();
    private List<ShowAdapter> showAdapters = new ArrayList<>();
    private int movieId;
    private String date;
    private MapViewModel mapViewModel;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int cinemaPos, int showPos);
        void onItemCLick(int cinemaPos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<ShowAdapter> getShowAdapters() {
        return showAdapters;
    }

    public CinemaAdapter(List<Cinema> cinemas, MapViewModel mapViewModel) {
        this.cinemas = cinemas;
        this.mapViewModel = mapViewModel;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public class CinemaHolder extends RecyclerView.ViewHolder {
        TextView cinemaName;
        RecyclerView showLayout;
        ImageView location;
        TextView distance;
        public CinemaHolder(View itemView) {
            super(itemView);
            cinemaName = itemView.findViewById(R.id.name_cinema);
            showLayout = itemView.findViewById(R.id.show_layout);
            location = itemView.findViewById(R.id.locate);
            distance = itemView.findViewById(R.id.distance);
        }
    }

    @NonNull
    @Override
    public CinemaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cinema_and_shows, parent, false);

        return new CinemaHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CinemaHolder holder, int position) {
        if (position >= shows.size()) {
            shows.add(new ArrayList<>());
            showAdapters.add(new ShowAdapter(shows.get(position)));
        }

        String name = cinemas.get(position).getName();
        holder.cinemaName.setText(name);

        holder.location.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemCLick(position);
            }
        });

        mapViewModel.getCurrentLatLng().observeForever(latLng -> {
            if (latLng != null) {
                LatLng cinemaLatLng = new LatLng(cinemas.get(position).getLatitude(), cinemas.get(position).getLongitude());
                holder.distance.setText(String.format("%.2fKm", MapsUtils.calculateDistance(latLng, cinemaLatLng)));
            }
        });

        ShowAdapter showAdt = showAdapters.get(position);
        holder.showLayout.setAdapter(showAdt);
        showAdt.setOnItemClickListener(showPos -> {
            if (listener != null) {
                listener.onItemClick(position, showPos);
            }
        });

        new GetShowController().getAllShowsByCinemaAndMovie(cinemas.get(position).getId(), movieId, date).thenAccept(shows -> {
            if (shows != null) {
                if (shows != null) {
                    this.shows.get(position).clear();
                    this.shows.get(position).addAll(shows);
                    showAdt.notifyDataSetChanged();
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return cinemas.size();
    }
}
