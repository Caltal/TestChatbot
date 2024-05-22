package com.example.asg02.controller.account;

import com.example.asg02.model.Account;

import java.util.concurrent.CompletableFuture;

public interface AccountUpdater {
    int updateCurrentAccount(Account newAccount);
}
