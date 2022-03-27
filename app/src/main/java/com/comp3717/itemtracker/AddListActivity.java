package com.comp3717.itemtracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AddListActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        EditText text_name = findViewById(R.id.edittext_addlist_name);
        EditText text_detail = findViewById(R.id.edittext_addlist_detail);
        Switch switch_widget = findViewById(R.id.switch_addlist_visibility);

        String name_value = text_name.getText().toString();
        String detail_value = text_detail.getText().toString();

        boolean isChecked = switch_widget.isChecked();
        com.comp3717.itemtracker.List list = new List(name_value, detail_value);

        if (item.getItemId() == R.id.item_add_done) {
            if(isChecked && !name_value.isEmpty()) {
                db.collection("lists2").whereEqualTo("name", name_value)
                        .limit(1).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        assert querySnapshot != null;

                        if (querySnapshot.isEmpty() && !ListManager.getInstance().getPrivateLists().contains(list)) {
                            db.collection("lists2")
                                    .add(list)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "\"" + list.getName() + "\" added",
                                                Toast.LENGTH_LONG).show();

                                        Log.d("AddList", "DocumentSnapshot written with ID: " + documentReference.getId());
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("AddList", "Error adding document", e);
                                        Toast.makeText(this, "Something went wrong in Firestore",
                                                Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            for (QueryDocumentSnapshot ignored : querySnapshot){
                                Toast.makeText(this, "Duplicate list name found - please use another list name",
                                        Toast.LENGTH_LONG).show();
                                Log.d("MESSAGE", "doc found");
                            }
                        }
                    } else {
                        Log.d("ERROR", String.valueOf(task.getException()));
                    }
                });

            } else if (!isChecked && !name_value.isEmpty()) {
                boolean success = ListManager.getInstance().addPrivateList(new com.comp3717.itemtracker.List(name_value, detail_value));

                if (success) {
                    Toast.makeText(this, "\"" + list.getName() + "\" added",
                            Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(this, "Duplicate list name found - please use another list name",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please enter a list name", Toast.LENGTH_LONG).show();
            }
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}