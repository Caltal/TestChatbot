package com.example.asg02.view.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.databinding.ItemMessageReceiveBinding;
import com.example.asg02.databinding.ItemMessageSendBinding;
import com.example.asg02.model.Message;
import com.example.asg02.utils.DateTimeUtils;

public class MessageAdapter extends ListAdapter<Message, RecyclerView.ViewHolder> {
    private String userId;
    private static final int HOLDER_TYPE_MESSAGE_RECEIVED = 1;
    private static final int HOLDER_TYPE_MESSAGE_SENT = 2;

    public MessageAdapter(String userId) {
        super(new MessageDiffCallback());
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSenderId().equals(userId)) {
            return HOLDER_TYPE_MESSAGE_SENT;
        } else {
            return HOLDER_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == HOLDER_TYPE_MESSAGE_SENT) {
            ItemMessageSendBinding binding = ItemMessageSendBinding.inflate(layoutInflater, parent, false);
            return new SentViewHolder(binding);
        } else if (viewType == HOLDER_TYPE_MESSAGE_RECEIVED) {
            ItemMessageReceiveBinding binding = ItemMessageReceiveBinding.inflate(layoutInflater, parent, false);
            return new ReceivedViewHolder(binding);
        } else {
            throw new IllegalStateException("Unexpected view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = getItem(position);
        if (holder.getItemViewType() == HOLDER_TYPE_MESSAGE_SENT) {
            ((SentViewHolder) holder).bind(message);
        } else {
            ((ReceivedViewHolder) holder).bind(message);
        }
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageReceiveBinding binding;

        public ReceivedViewHolder(ItemMessageReceiveBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.messageText.setText(message.getText());
            binding.timeText.setText(DateTimeUtils.convertTimestampToDate(message.getTimestamp()));
        }
    }

    class SentViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageSendBinding binding;

        public SentViewHolder(ItemMessageSendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.messageText.setText(message.getText());
            binding.timeText.setText(DateTimeUtils.convertTimestampToDate(message.getTimestamp()));
        }
    }

    static class MessageDiffCallback extends DiffUtil.ItemCallback<Message> {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp();
        }
    }
}
