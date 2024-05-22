package com.example.asg02.view.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.R;
import com.example.asg02.model.Event;
import com.example.asg02.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    List<Event> newsList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public EventAdapter(List<Event> newsList) {
        this.newsList = newsList;
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public EventHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.events_image);
        }
    }

    @NonNull
    @NotNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_events_recyclerview, parent, false);

        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.imageView.setImageBitmap(ImageUtils.decodeBitmap(newsList.get(position).getPoster()));
        holder.imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
