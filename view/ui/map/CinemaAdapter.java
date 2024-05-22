package com.example.asg02.view.ui.map;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.R;
import com.example.asg02.model.Cinema;
import com.example.asg02.utils.MapsUtils;
import com.example.asg02.vm.MapViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaHolder> {

    private List<Cinema> cinemas;
    private int movieId;
    private MapViewModel mapViewModel;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemCLick(int cinemaPos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CinemaAdapter(List<Cinema> cinemas, MapViewModel mapViewModel) {
        this.cinemas = cinemas;
        this.mapViewModel = mapViewModel;
    }

    public class CinemaHolder extends RecyclerView.ViewHolder {
        CardView layout;
        TextView cinemaName;
        TextView distance;
        public CinemaHolder(View itemView) {
            super(itemView);
            cinemaName = itemView.findViewById(R.id.name_cinema);
            layout = itemView.findViewById(R.id.cinema_layout);
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
        String name = cinemas.get(position).getName();
        holder.cinemaName.setText(name);

        holder.layout.setOnClickListener(v -> {
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
    }



    @Override
    public int getItemCount() {
        return cinemas.size();
    }
}
