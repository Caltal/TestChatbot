package com.example.asg02.controller.account;

import com.example.asg02.model.Account;
import com.example.asg02.model.User;
import com.example.asg02.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.concurrent.CompletableFuture;

public class UpdateAccountController implements AccountUpdater, AccountCreator {
    private boolean emailExists = false;
    private boolean phoneExists = false;

    public UpdateAccountController() {
    }

    @Override
    public int updateCurrentAccount(Account newAccount) {
        if (newAccount == null) {
            return FAIL;
        }

        User user = (User) newAccount;
        FirebaseUser currentUser = FirebaseUtils.getAuth().getCurrentUser();
        if (currentUser == null) {
            return FAIL;
        }
        FirebaseUtils.getDatabaseReference("Users").child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User oldUser = task.getResult().getValue(User.class);
                if (oldUser == null) {
                    return;
                }
                if (!oldUser.getEmail().equals(user.getEmail())) {
                    checkExistEmail(user.getEmail()).thenAccept(exist -> {
                        if (exist) {
                            emailExists = true;
                        }
                    });
                }

                if (!oldUser.getPhone().equals(user.getPhone())) {
                    checkExistPhone(user.getPhone()).thenAccept(exist -> {
                        if (exist) {
                            phoneExists = true;
                        }
                    });
                }
            }
        });

        if (emailExists) {
            return EMAIL_EXISTS;
        } else if (phoneExists) {
            return PHONE_EXISTS;
        } else {
            currentUser.updateEmail(user.getEmail());
            FirebaseUtils.getAuth().updateCurrentUser(currentUser);
            FirebaseUtils.getDatabaseReference("Users").child(currentUser.getUid()).setValue(user);
            return SUCCESS;
        }
    }

    @Override
    public CompletableFuture<Boolean> checkExistEmail(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        FirebaseUtils.getDatabaseReference("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emailExists = false;
                for (DataSnapshot data : task.getResult().getChildren()) {
                    User u = data.getValue(User.class);
                    if (u.getEmail().equals(email)) {
                        emailExists = true;
                        break;
                    }
                }
                future.complete(emailExists);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Boolean> checkExistPhone(String phone) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        FirebaseUtils.getDatabaseReference("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                phoneExists = false;
                for (DataSnapshot data : task.getResult().getChildren()) {
                    User u = data.getValue(User.class);
                    if (u.getPhone().equals(phone)) {
                        phoneExists = true;
                        break;
                    }
                }
                future.complete(phoneExists);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Integer> createAccount(Account account) {
        return CompletableFuture.completedFuture(FAIL);
    }
}
