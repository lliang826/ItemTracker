package com.comp3717.itemtracker;

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

    public void addPrivateList(com.comp3717.itemtracker.List list) {
        privateLists.add(list);
    }
}
