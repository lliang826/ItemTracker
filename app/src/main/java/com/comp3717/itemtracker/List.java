package com.comp3717.itemtracker;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class List implements Serializable {

    @DocumentId
    private String id;

    private String name;
    private String description;
    
    @Exclude
    private final java.util.List<Item> privateItems;

    public List() {
        this.privateItems = new ArrayList<>();
    }

    public List(String name, String description) {
        this.id = ListManager.getInstance().generateUid();
        this.name = name;
        this.description = description;
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

    public java.util.List<Item> getPrivateItems() {
        return privateItems;
    }
}
