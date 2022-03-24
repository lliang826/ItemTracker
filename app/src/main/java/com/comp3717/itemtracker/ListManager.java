package com.comp3717.itemtracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListManager {

    private static ListManager INSTANCE = null;

    private static final String PREFS_NAME = "MyPreferences";

    private final String PRIVATE_LISTS = "privateLists";
    private final Type LIST_TYPE = new TypeToken<List<com.comp3717.itemtracker.List>>() {}.getType();

    private final SharedPreferences prefs = MyApp.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    private List<com.comp3717.itemtracker.List> privateLists;

    private ListManager() {
        privateLists = ((privateLists = loadPrivateLists()) != null)
                ? privateLists
                : new ArrayList<>();
    }

    public static ListManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ListManager();
        }
        return(INSTANCE);
    }

    public void savePrivateLists() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PRIVATE_LISTS, new Gson().toJson(privateLists));
        editor.apply();
    }

    public List<com.comp3717.itemtracker.List> getPrivateLists() {
        return privateLists;
    }

    public boolean addPrivateList(com.comp3717.itemtracker.List list) {
        boolean result = privateLists.add(list);
        savePrivateLists();
        return result;
    }

    public boolean removePrivateList(com.comp3717.itemtracker.List list) {
        boolean result = privateLists.remove(list);
        savePrivateLists();
        return result;
    }

    private List<com.comp3717.itemtracker.List> loadPrivateLists() {
        if (privateLists == null) {
            privateLists = new Gson().fromJson(prefs.getString(PRIVATE_LISTS, null), LIST_TYPE);
        }
        return privateLists;
    }
}
