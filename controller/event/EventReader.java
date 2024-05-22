package com.example.asg02.controller.event;

import com.example.asg02.model.Event;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface EventReader {
    void getAllEvents(Consumer<Event> onEventAdded);
}
