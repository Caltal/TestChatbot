package com.example.asg02.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class User extends Account {
    private String name;
    private String birthDate;
    private String sex;
    private String phone;
    private String region;
    private String favouriteCinema;
    private long point;
    private double expense;
    private String avatar;

    public User() {
    }

    public User(String email, String password, String name, String birthDate
            , String sex, String phone, String region, String favouriteCinema, String avatar) {
        super(email, password);
        this.name = name;
        this.birthDate = birthDate;
        this.sex = sex;
        this.phone = phone;
        this.region = region;
        this.favouriteCinema = favouriteCinema;
        this.avatar = avatar;
        this.point = 0;
        this.expense = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFavouriteCinema() {
        return favouriteCinema;
    }

    public void setFavouriteCinema(String favouriteCinema) {
        this.favouriteCinema = favouriteCinema;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }
}