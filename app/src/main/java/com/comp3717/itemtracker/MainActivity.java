package com.comp3717.itemtracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomnavigationview_main);
        bottomNavigation.setOnItemSelectedListener(item -> {

            item.setChecked(true);
            System.out.println(item.getTitle());
            return true;
        });
    }
}