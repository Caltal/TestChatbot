package com.example.asg02.controller.event;

import androidx.annotation.NonNull;

import com.example.asg02.model.Event;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GetAllEventController implements EventReader {

    public GetAllEventController() {
    }

    @Override
    public void getAllEvents(Consumer<Event> onEventAdded) {
        FirebaseUtils.getDatabaseReference("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        onEventAdded.accept(event);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
