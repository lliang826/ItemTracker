package com.comp3717.itemtracker;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class List implements Serializable {

    @Exclude
    private final Map<String, Item> cachedItems;
    @Exclude
    private final java.util.List<Item> privateItems;
    @DocumentId
    private String id;
    private String name;
    private String description;

    public List() {
        this.cachedItems = new HashMap<>();
        this.privateItems = new ArrayList<>();
    }

    public List(String name, String description) {
        this.name = name;
        this.description = description;
        this.cachedItems = new HashMap<>();
        this.privateItems = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Item getCachedItem(Item item) {
        Item value = cachedItems.get(item.getId());
        if (value != null) {
            return value;
        } else {
            cachedItems.put(item.getId(), item);
            ListManager.getInstance().save();
            return item;
        }
    }

    @Exclude
    public java.util.List<Item> getPrivateItems() {
        return privateItems;
    }

    public boolean addPrivateItem(Item item) {
        boolean result = privateItems.add(item);
        ListManager.getInstance().save();
        return result;
    }

    public boolean removePrivateItem(Item item) {
        boolean result = privateItems.remove(item);
        ListManager.getInstance().save();
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof com.comp3717.itemtracker.List) {
            com.comp3717.itemtracker.List l = (com.comp3717.itemtracker.List) o;
            return this.name.equals(l.getName());
        } else
            return false;
    }
}
