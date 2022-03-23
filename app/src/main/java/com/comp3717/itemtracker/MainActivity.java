package com.comp3717.itemtracker;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    FloatingActionButton add, addList, addItem;
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

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