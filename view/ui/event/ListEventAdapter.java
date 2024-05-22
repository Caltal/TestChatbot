package com.example.asg02.view.ui.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.R;
import com.example.asg02.model.Event;
import com.example.asg02.utils.ImageUtils;

import java.util.List;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {
    private List<Event> events;
    private OnItemClickListener listener;

    public ListEventAdapter(List<Event> events) {
        this.events = events;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout;
        ImageView image;
        TextView name;
        TextView expiredDate;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            expiredDate = itemView.findViewById(R.id.expiredDate);
            layout = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_events_verrtical_recyclerview, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageBitmap(ImageUtils.decodeBitmap(events.get(position).getPoster()));
        holder.name.setText(events.get(position).getEventName());
        holder.expiredDate.setText(events.get(position).getStartDate() + " - " + events.get(position).getEndDate());
        holder.layout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
