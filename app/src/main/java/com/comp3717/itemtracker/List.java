package com.comp3717.itemtracker;

import com.comp3717.itemtracker.placeholder.PlaceholderContent;

import java.io.Serializable;

public class List implements Serializable {
    private PlaceholderContent.PlaceholderItem placeholderItem;

    public List(PlaceholderContent.PlaceholderItem placeholderItem) {
        this.placeholderItem = placeholderItem;
    }

    public PlaceholderContent.PlaceholderItem getPlaceholderItem() {
        return placeholderItem;
    }
}
