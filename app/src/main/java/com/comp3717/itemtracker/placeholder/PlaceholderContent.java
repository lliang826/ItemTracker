package com.comp3717.itemtracker.placeholder;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();
    public static List<PlaceholderItem> LISTS = new ArrayList<PlaceholderItem>();
    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static LinkedHashMap<String, PlaceholderItem> ITEM_MAP = new LinkedHashMap<>();
    public static LinkedHashMap<String, PlaceholderItem> LIST_MAP = new LinkedHashMap<>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            // add private items
            addItem(createPlaceholderItem(i));
            // add private lists
            addList(createPlaceholderList(i));
        }
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static void addList(PlaceholderItem list) {
        LISTS.add(list);
        LIST_MAP.put(list.id, list);
    }

    private static PlaceholderItem createPlaceholderItem(int position) {
        return new PlaceholderItem(String.valueOf(position), "Local Item " + position, makeDetails(position),  new ArrayList<>());
    }

    private static PlaceholderItem createPlaceholderList(int position) {
        return new PlaceholderItem(String.valueOf(position), "Local List " + position, makeDetails(position),  new ArrayList<>());
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String content;
        public final String details;
        public final ArrayList<String> lists;
        public PlaceholderItem(String id, String content, String details, ArrayList<String> lists) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.lists = lists;
        }

        public PlaceholderItem(String id, String content) {
            this.id = id;
            this.content = content;
            this.details = "";
            this.lists = new ArrayList<>();
        }

        public ArrayList<String> getItemsArray() {
            if (this.lists != null) {
                return this.lists;
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        public String toString() {
            return content;
        }

        @Override
        public boolean equals(Object v) {
            boolean retVal = false;
            if (v instanceof PlaceholderItem){
                PlaceholderItem ptr = (PlaceholderItem) v;
                retVal = ptr.id.equals(this.id);
            }
            return retVal;
        }
    }
}