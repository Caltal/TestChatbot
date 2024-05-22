package com.example.asg02.controller;

import com.example.asg02.model.Province;
import com.example.asg02.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class GetProvinceController {

    public GetProvinceController() {
    }

    public List<String> getAllProvinces() {
        List<String> provinces = new ArrayList<>();
        FirebaseUtils.getDatabaseReference("place").child("province").get().addOnSuccessListener(dataSnapshot -> {
            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                Province p = dataSnapshot.child(String.valueOf(i)).getValue(Province.class);
                provinces.add(p.getName());
            }
        });
        return provinces;
    }
}
