package com.comp3717.itemtracker;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.comp3717.itemtracker.placeholder.PlaceholderContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    FloatingActionButton add, addList, addItem;
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean clicked = false;
    CollectionReference lists_ref = FirebaseFirestore.getInstance().collection("lists");
    CollectionReference items_ref = FirebaseFirestore.getInstance().collection("items");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        LinkedHashMap<String, PlaceholderContent.PlaceholderItem> LIST_TEMP = new LinkedHashMap<>();
        lists_ref.addSnapshotListener((queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                document.getData();
                PlaceholderContent.PlaceholderItem placeholderItem = new PlaceholderContent.PlaceholderItem(
                        document.getId(),
                        Objects.requireNonNull(document.getData().get("Name")).toString(),
                        Objects.requireNonNull(document.getData().get("Detail")).toString(),
                        (ArrayList<String>) document.getData().get("Lists"));
                if (!PlaceholderContent.LISTS.contains(placeholderItem)) {
                    PlaceholderContent.LISTS.add(placeholderItem);
                    finish();
                    startActivity(getIntent());
                }
                LIST_TEMP.put(document.getId(),placeholderItem);
            }
            PlaceholderContent.LIST_MAP = LIST_TEMP;
        });

        LinkedHashMap<String, PlaceholderContent.PlaceholderItem> ITEM_TEMP = new LinkedHashMap<>();
        items_ref.addSnapshotListener((queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                document.getData();
                ITEM_TEMP.put(
                        document.getId(), new PlaceholderContent.PlaceholderItem(document.getId(), Objects.requireNonNull(document.getData().get("Name")).toString())
                );
            }
            PlaceholderContent.ITEM_MAP = ITEM_TEMP;
        });

        // creating animations for floating action buttons
        add = findViewById(R.id.floatingactionbutton_main_add);
        addList = findViewById(R.id.floatingactionbutton_main_list);
        addItem = findViewById(R.id.floatingactionbutton_main_item);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_botton_animation);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_animation);

        add.setOnClickListener(view -> onAddButtonClicked());

        addList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddListActivity.class);
            startActivity(intent);
        });

        addItem.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void setupNavController(NavController navController) {
        this.navController = navController;

        // Make sure actions in the ActionBar get propagated to the NavController
        setupActionBarWithNavController(this, navController);
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            addList.setVisibility(View.VISIBLE);
            addItem.setVisibility(View.VISIBLE);
        } else {
            addList.setVisibility(View.INVISIBLE);
            addItem.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
            addItem.startAnimation(fromBottom);
            addList.startAnimation(fromBottom);
            add.startAnimation(rotateOpen);
        } else {
            addItem.startAnimation(toBottom);
            addList.startAnimation(toBottom);
            add.startAnimation(rotateClose);
        }
    }

    private void setClickable(Boolean clicked) {
        if (!clicked) {
            addList.setClickable(true);
            addItem.setClickable(true);
        } else {
            addList.setClickable(false);
            addItem.setClickable(false);
        }
    }

}