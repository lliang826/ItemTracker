package com.comp3717.itemtracker;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListManager {

    private static ListManager INSTANCE = null;

    private final List<com.comp3717.itemtracker.List> privateLists;

    private ListManager() {
        privateLists = new ArrayList<>();
    }

    public static ListManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ListManager();
        }
        return(INSTANCE);
    }

    public List<com.comp3717.itemtracker.List> getPrivateLists() {
        return privateLists;
    }

    public String generateUid() {
        DocumentReference ref = FirebaseFirestore.getInstance().collection("lists2").document();
        return ref.getId();
    }
}
