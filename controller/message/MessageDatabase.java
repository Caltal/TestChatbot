package com.example.asg02.controller.message;

import com.example.asg02.model.Message;

import java.util.function.Consumer;

public interface MessageDatabase {
    void getMessages(String uID, Consumer<Message> onMessageAdded);
    void addMessage(Message message);
}
