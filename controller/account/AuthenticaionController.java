package com.example.asg02.controller.account;

import com.example.asg02.model.User;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.concurrent.CompletableFuture;

public class AuthenticaionController {
    public AuthenticaionController() {
    }

    private String verificationId;
    public void sendVerificationCode(String email) {
        FirebaseUtils.getAuth().sendPasswordResetEmail(email);
    }

    public CompletableFuture<String> getEmailFromPhone(String phone) {
        CompletableFuture<String> future = new CompletableFuture<>();
        // get email from phone
        FirebaseUtils.getDatabaseReference("Users").get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                User u = data.getValue(User.class);
                if (u.getPhone().equals(phone)) {
                    future.complete(u.getEmail());
                }
            }
        });
        return future;
    }

}
