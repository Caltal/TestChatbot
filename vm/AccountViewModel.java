package com.example.asg02.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.asg02.model.User;

public class AccountViewModel extends ViewModel {
    private MutableLiveData<String> userId = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    private MediatorLiveData<Boolean> dataReady = new MediatorLiveData<>();

    public AccountViewModel() {
        dataReady.addSource(userId, value -> checkDataReady());
        dataReady.addSource(user, value -> checkDataReady());
    }

    private void checkDataReady() {
        if (userId.getValue() != null && user.getValue() != null) {
            dataReady.setValue(true);
            return;
        }
        dataReady.setValue(false);
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public void clearData() {
        userId.setValue(null);
        user.setValue(null);
    }

    public LiveData<Boolean> isDataReady() {
        return dataReady;
    }
}
