package com.comp3717.itemtracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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
    }
}