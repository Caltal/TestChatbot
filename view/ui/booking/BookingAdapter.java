package com.example.asg02.view.ui.booking;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.R;
import com.example.asg02.controller.movie.GetMovieController;
import com.example.asg02.model.Booking;
import com.example.asg02.model.Cinema;
import com.example.asg02.model.CinemaHall;
import com.example.asg02.model.Manager;
import com.example.asg02.model.Movie;
import com.example.asg02.model.Show;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.utils.FirebaseUtils;
import com.example.asg02.utils.ImageUtils;
import com.example.asg02.vm.BaseViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {
    private List<Booking> bookingList = new ArrayList<>();
    private List<Movie> movieList = new ArrayList<>();
    private List<Cinema> cinemaList = new ArrayList<>();
    private List<CinemaHall> cinemaHallList = new ArrayList<>();

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookingAdapter(List<Booking> bookingList, List<Movie> movieList, List<Cinema> cinemaList, List<CinemaHall> cinemaHallList) {
        this.bookingList = bookingList;
        this.movieList = movieList;
        this.cinemaList = cinemaList;
        this.cinemaHallList = cinemaHallList;
    }

    public class BookingHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView duration;
        TextView hall;
        TextView seats;
        TextView expiredDate;
        TextView status;
        Button useButton;
        LinearLayout layout;


        public BookingHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            name = itemView.findViewById(R.id.movie_name);
            duration = itemView.findViewById(R.id.movie_duration);
            hall = itemView.findViewById(R.id.hall);
            seats = itemView.findViewById(R.id.seats);
            expiredDate = itemView.findViewById(R.id.expired_date);
            status = itemView.findViewById(R.id.status);
            useButton = itemView.findViewById(R.id.use);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);

        return new BookingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {
        Booking booking = bookingList.get(position);
        Show show = booking.getShow();
        Movie movie = movieList.get(position);
        Cinema cinema = cinemaList.get(position);
        CinemaHall cinemaHall = cinemaHallList.get(position);

        holder.imageView.setImageBitmap(ImageUtils.decodeBitmap(movie.getPoster()));
        holder.name.setText(movie.getName());
        holder.duration.setText(DateTimeUtils.convertMinsToStringTime(movie.getDurationMins()));

        holder.hall.setText(cinema.getName() + " - " + cinemaHall.getName());
        StringBuilder sb1 = new StringBuilder();
        for (String s : booking.getSeats()) {
            sb1.append(s).append(", ");
        }
        holder.seats.setText(sb1.toString().substring(0, sb1.length() - 2));

        holder.expiredDate.setText(show.getStartTime() + " - " + show.getEndTime() + ", " + show.getDate());
        switch (booking.getStatus()) {
            case Booking.STATUS_AVAILABLE:
                holder.status.setText("");
                holder.useButton.setVisibility(View.VISIBLE);
                break;
            case Booking.STATUS_EXPIRED:
                holder.status.setText("Quá hạn sử dụng");
                holder.useButton.setVisibility(View.GONE);
                break;
            case Booking.STATUS_USED:
                holder.status.setText("Vé đã sử dụng");
                holder.useButton.setVisibility(View.GONE);
                break;
        }
        holder.useButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

//        holder.layout.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return this.bookingList.size();
    }
}
