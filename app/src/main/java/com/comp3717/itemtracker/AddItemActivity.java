package com.comp3717.itemtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity {

    FirebaseFirestore db;
    List list;
    ArrayList<com.comp3717.itemtracker.List> allLists = new ArrayList<>();
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        db = FirebaseFirestore.getInstance();
        allLists.addAll(ListManager.getInstance().getPrivateLists());
        spinnerSetup();
    }

    void spinnerSetup() {
        db.collection("lists2")
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("Debug", "onSuccess: LIST EMPTY");
                        return;
                    } else {
                        java.util.List<List> lists
                                = documentSnapshots.toObjects(List.class);
                        allLists.addAll(lists);
                        Log.d("Debug", "onSuccess: " + allLists);
                    }

                    Spinner spinner = findViewById(R.id.addItem_spinner);
                    aSwitch = findViewById(R.id.switch_additem_visibility);

                    ArrayAdapter<List> arrayAdapter = new ArrayAdapter<>(AddItemActivity.this,
                            android.R.layout.simple_spinner_item, allLists);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            list = allLists.get(i);
                            Log.w("list Debug", list.getName());
                            Log.w("allLists Debug", allLists.toString());

                            if (list.getId() == null) {
                                aSwitch.setChecked(false);
                                aSwitch.setEnabled(false);
                            } else {
                                aSwitch.setEnabled(true);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    spinner.setAdapter(arrayAdapter);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.item_add_done) {
            EditText editText = findViewById(R.id.edittext_additem_name);
            String itemName = editText.getText().toString();

            if (itemName.replaceAll("\\s+","").matches("")) {
                Toast.makeText(this, "Please enter a name for your item",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (list.getId() != null && aSwitch.isChecked()) {
                    Item newItem = new Item(itemName);

                    db.collection("lists2").document(list.getId())
                            .collection("items")
                            .add(newItem)
                            .addOnSuccessListener(documentReference -> {
                                Log.d("Debug", "DocumentSnapshot successfully written!");
                                finish();
                                Toast.makeText(AddItemActivity.this,
                                        "\"" + itemName + "\"" + " added to "
                                                + list.getName(), Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> Log.w("Debug", "Error writing document", e));

                } else if (!aSwitch.isChecked()) {
                    Item newItem = new Item(itemName);
                    List cachedList = ListManager.getInstance().getCachedList(list);
                    boolean added = cachedList.addPrivateItem(newItem);
                    if (added) {
                        finish();
                        Toast.makeText(AddItemActivity.this,
                                "\"" + itemName + "\"" + " added to "
                                        + list.getName(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.w("Debug", "Error writing to private list");
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}