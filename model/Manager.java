package com.example.asg02.model;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Account {
    private String name;
//    private String avatar;

    public Manager() {
    }


//    public Manager(String email, String password, String name, String avatar) {
//        super(email, password);
//        this.name = name;
//        this.avatar = avatar;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
//    public List<Integer> getMovieIds() {
//        return movieIds;
//    }
}
