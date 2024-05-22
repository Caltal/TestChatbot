package com.example.asg02.controller.account;

import com.example.asg02.model.Account;
import com.example.asg02.model.User;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class RegisterLogic {
    private final RegisterController registerController;

    public RegisterLogic(RegisterController registerController) {
        this.registerController = registerController;
    }

    public CompletableFuture<Boolean> checkExistEmail(String email) {
        return registerController.checkExistEmail(email);
    }

    public CompletableFuture<Boolean> checkExistPhone(String phone) {
        return registerController.checkExistPhone(phone);
    }

    public CompletableFuture<Integer> createAccount(Account account) {
        if (account == null) {
            return CompletableFuture.completedFuture(RegisterController.FAIL);
        }
        User user = (User) account;
        return checkExistEmail(user.getEmail()).thenCompose(emailExists -> {
            if (emailExists) {
                return CompletableFuture.completedFuture(RegisterController.EMAIL_EXISTS);
            } else {
                return checkExistPhone(user.getPhone()).thenCompose(phoneExists -> {
                    if (phoneExists) {
                        return CompletableFuture.completedFuture(RegisterController.PHONE_EXISTS);
                    } else {
                        return registerController.createAccount(user);
                    }
                });
            }
        });
    }
}

