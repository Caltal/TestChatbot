package com.example.asg02.controller.account;

import com.example.asg02.model.Account;

import java.util.concurrent.CompletableFuture;

public interface AccountReader {
    CompletableFuture<Account> getAccountWithPhone(String phone, String password);
    CompletableFuture<Account> getAccountWithEmail(String email, String password);
}
