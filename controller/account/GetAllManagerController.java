package com.example.asg02.controller.account;

import com.example.asg02.model.Account;
import com.example.asg02.model.Manager;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetAllManagerController {

    public GetAllManagerController() {
    }
    public CompletableFuture<List<Account>> getAllManagers() {
        CompletableFuture<List<Account>> future = new CompletableFuture<>();
        // get all managers
        FirebaseUtils.getDatabaseReference("Managers").get().addOnSuccessListener(dataSnapshot -> {
            List<Account> managers = new ArrayList<>();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Manager m = data.getValue(Manager.class);
                managers.add(m);
            }
            future.complete(managers);
        });
        return future;
    }
}
