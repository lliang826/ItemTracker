package com.comp3717.itemtracker;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Item implements Serializable {

    @DocumentId
    private String id;

    private String name;

    @Exclude
    private boolean done;

    public Item() {
        this.done = false;
    }

    public Item(String name) {
        this.name = name;
        this.done = false;
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

    @Exclude
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        ListManager.getInstance().save();
    }
}
