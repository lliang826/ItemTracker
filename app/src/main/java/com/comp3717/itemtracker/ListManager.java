package com.comp3717.itemtracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListManager {

    private static ListManager INSTANCE = null;

    private static final String PREFS_NAME = "MyPreferences";

    private final String CACHED_LISTS = "cachedLists";
    private final String PRIVATE_LISTS = "privateLists";
    private final Type MAP_TYPE = new TypeToken<Map<String, com.comp3717.itemtracker.List>>() {}.getType();
    private final Type LIST_TYPE = new TypeToken<List<com.comp3717.itemtracker.List>>() {}.getType();

    private final SharedPreferences prefs = MyApp.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    private Map<String, com.comp3717.itemtracker.List> cachedLists;
    private List<com.comp3717.itemtracker.List> privateLists;

    private ListManager() {
        cachedLists = ((cachedLists = loadCachedLists()) != null)
                ? cachedLists
                : new HashMap<>();
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

    public void save() {
        saveCachedLists();
        savePrivateLists();
    }

    public com.comp3717.itemtracker.List getCachedList(com.comp3717.itemtracker.List list) {
        com.comp3717.itemtracker.List value = cachedLists.get(list.getId());
        if (value != null) {
            return value;
        } else {
            cachedLists.put(list.getId(), list);
            saveCachedLists();
            return list;
        }
    }

    public List<com.comp3717.itemtracker.List> getPrivateLists() {
        return privateLists;
    }

    public boolean addPrivateList(com.comp3717.itemtracker.List list) {
        if (privateLists.contains(list)) {
            return false;
        }
        boolean result = privateLists.add(list);
        savePrivateLists();
        return result;
    }

    public boolean removePrivateList(com.comp3717.itemtracker.List list) {
        boolean result = privateLists.remove(list);
        savePrivateLists();
        return result;
    }

    private Map<String, com.comp3717.itemtracker.List> loadCachedLists() {
        if (cachedLists == null) {
            cachedLists = new Gson().fromJson(prefs.getString(CACHED_LISTS, null), MAP_TYPE);
        }
        return cachedLists;
    }

    private List<com.comp3717.itemtracker.List> loadPrivateLists() {
        if (privateLists == null) {
            privateLists = new Gson().fromJson(prefs.getString(PRIVATE_LISTS, null), LIST_TYPE);
        }
        return privateLists;
    }

    private void saveCachedLists() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(CACHED_LISTS, new Gson().toJson(cachedLists));
        editor.apply();
    }

    private void savePrivateLists() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PRIVATE_LISTS, new Gson().toJson(privateLists));
        editor.apply();
    }
}
