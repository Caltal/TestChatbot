package com.example.asg02.view.ui.booking.process.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.asg02.R;
import com.example.asg02.model.Show;
import com.example.asg02.view.ui.booking.BookingAdapter;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowHolder> {
    private List<Show> showList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<Show> getShowList() {
        return showList;
    }

    public ShowAdapter(List<Show> showList) {
        this.showList = showList;
    }

    public class ShowHolder extends RecyclerView.ViewHolder {
        TextView startTime;
        public ShowHolder(View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.start_time);
        }
    }

    @NonNull
    @Override
    public ShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show, parent, false);

        return new ShowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHolder holder, int position) {
        holder.startTime.setText(showList.get(position).getStartTime());
        holder.startTime.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.showList.size();
    }
}
