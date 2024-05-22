package com.example.asg02.view.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asg02.R;
import com.example.asg02.controller.message.GetAllMessageController;
import com.example.asg02.databinding.ActivityChatBinding;
import com.example.asg02.model.Message;
import com.example.asg02.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;

    private String userId;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private GetAllMessageController messageController;
    private List<Message> messages = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            throw new IllegalStateException("User id must not be null");
        }

        recyclerView = binding.messagesRecyclerView;
        adapter = new MessageAdapter(userId);
        adapter.submitList(messages);
        recyclerView.setAdapter(adapter);

        messageController = new GetAllMessageController();
        messageController.getMessages(userId
                , message -> {
                    messages.add(message);
                    recyclerView.scrollToPosition(messages.size() - 1);
        });

        binding.buttonSendMessage.setOnClickListener(
            v -> {
                String text = binding.editTextMessage.getText().toString();
                if (!text.isEmpty()) {
                    Message message = new Message(userId, text, System.currentTimeMillis());
                    messageController.addMessage(message);
                    binding.editTextMessage.setText("");
                }
            }
        );

        binding.messagesRecyclerView.setOnTouchListener(
            (v, event) -> {
                hideKeyboard();
                return false;
            }
        );


        binding.toolbar.setNavigationOnClickListener(
            v -> {
                finish();
            }
        );
    }

}