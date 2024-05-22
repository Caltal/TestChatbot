package com.example.asg02.controller.account;

import com.example.asg02.model.Account;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class LoginLogic {
    private final LoginController loginController;

    public LoginLogic(LoginController loginController) {
        this.loginController = loginController;
    }

    public CompletableFuture<Account> login(String id, String password) {
        if (id == null || password == null) {
            return CompletableFuture.completedFuture(null);
        }
        if (LoginController.isValidEmail(id)) {
            return loginController.getAccountWithEmail(id, password);
        } else if (LoginController.isValidPhoneNumber(id)) {
            return loginController.getAccountWithPhone(id, password);
        }
//        return loginController.login(id, password);
        return CompletableFuture.completedFuture(null);
    }
}