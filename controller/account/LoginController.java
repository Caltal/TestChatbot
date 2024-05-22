package com.example.asg02.controller.account;

import androidx.annotation.NonNull;

import com.example.asg02.model.Account;
import com.example.asg02.model.User;
import com.example.asg02.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class LoginController implements AccountReader {

    public LoginController() {
    }

    public CompletableFuture<Account> login(String id, String password) {
        if (id == null || password == null) {
            return CompletableFuture.completedFuture(null);
        }
        if (isValidEmail(id)) {
            return getAccountWithEmail(id, password);
        } else if (isValidPhoneNumber(id)) {
            return getAccountWithPhone(id, password);
        }
        return CompletableFuture.completedFuture(null);
    }

    public static boolean isValidEmail(String id) {
        String emailRegex = "^[a-zA-Z0-9_]+(?:\\.[a-zA-Z0-9_]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return id != null && id.matches(emailRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^0\\d{9}$";
        return phoneNumber != null && phoneNumber.matches(phoneNumberRegex);
    }

    @Override
    public CompletableFuture<Account> getAccountWithPhone(String phone, String password) {
        if (phone == null || password == null) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<Account> future = new CompletableFuture<>();
        FirebaseUtils.getDatabaseReference("Users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User u = data.getValue(User.class);
                    if (u.getPhone().equals(phone)) {
                        // return account
                        FirebaseUtils.getAuth().signInWithEmailAndPassword(u.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    future.complete(u);
                                } else {
                                    // return null
                                }
                            }
                        });
                    }
                }
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Account> getAccountWithEmail(String email, String password) {
        CompletableFuture<Account> future = new CompletableFuture<>();
        FirebaseUtils.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uID = FirebaseUtils.getAuth().getCurrentUser().getUid();
                            FirebaseUtils.getDatabaseReference("Users").child(uID).get()
                                    .addOnCompleteListener(task_ -> future.complete(task_.getResult().getValue(User.class)));
                        } else {
                            future.complete(null);
                        }
                    }
                });
        return future;
    }
}
