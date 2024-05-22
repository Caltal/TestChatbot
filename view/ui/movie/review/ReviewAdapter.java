package com.example.asg02.view.ui.movie.review;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.R;
import com.example.asg02.databinding.ItemReviewBinding;
import com.example.asg02.model.Review;
import com.example.asg02.utils.DateTimeUtils;
import com.example.asg02.utils.FirebaseUtils;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends ListAdapter<Review, RecyclerView.ViewHolder> {

    public ReviewAdapter() {
        super(new ReviewDiffCallback());
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ReviewViewHolder(ItemReviewBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = getItem(position);
        ((ReviewViewHolder) holder).bind(review);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ItemReviewBinding binding;

        public ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Review review) {
            FirebaseUtils.getDatabaseReference("Users")
                    .child(review.getUserId())
                    .get().addOnSuccessListener(snapshot -> {
                        binding.username.setText(snapshot.child("name").getValue().toString());
                        binding.content.setText(review.getContent());
                        binding.time.setText(DateTimeUtils.convertTimestampToDate(review.getTimestamp()));
                        binding.rating.setRating(review.getRating());
                    });
        }
    }

    static class ReviewDiffCallback extends DiffUtil.ItemCallback<Review> {
        @Override
        public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp();
        }
    }

}
