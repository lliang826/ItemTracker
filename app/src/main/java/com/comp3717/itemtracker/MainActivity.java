package com.comp3717.itemtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add, addList, addItem;
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomnavigationview_main);
        bottomNavigation.setOnItemSelectedListener(item -> {
            item.setChecked(true);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (item.getItemId() == R.id.item_bottomnav_mylists) {
                ft.replace(R.id.fragmentcontainerview_main, new MyListsFragment());
            } else {
                ft.replace(R.id.fragmentcontainerview_main, new SearchFragment());
            }
            ft.commit();

            return true;
        });


        // creating animations for floating action buttons
        add = findViewById(R.id.floatingactionbutton_main_add);
        addList = findViewById(R.id.floatingactionbutton_main_list);
        addItem = findViewById(R.id.floatingactionbutton_main_item);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_botton_animation);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_animation);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }
        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddListActivity.class);
                startActivity(intent);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
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