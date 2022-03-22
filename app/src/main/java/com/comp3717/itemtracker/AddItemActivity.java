package com.comp3717.itemtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    FirebaseFirestore db;
    String listName;
    HashMap<String, String> publicLists = new HashMap<>();
    ArrayList<String> publicListsNames = new ArrayList<>();
    ArrayList<String> privateLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();

        spinnerSetup();
    }

    void spinnerSetup() {
        db.collection("lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Debug", document.getData().toString());

                                publicLists.put(document.getId(), document.getData().get("Name").toString());
                            }
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }

                        Spinner spinner = findViewById(R.id.addItem_spinner);
                        Switch switchy = findViewById(R.id.switch_additem_visibility);

                        publicListsNames.addAll(publicLists.values());

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddItemActivity.this,
                                android.R.layout.simple_spinner_item, publicListsNames);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                listName = spinner.getSelectedItem().toString();
                                if (privateLists.contains(listName)) {
                                    switchy.setChecked(false);
                                    switchy.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        spinner.setAdapter(arrayAdapter);
                    }
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

            Switch switchy = findViewById(R.id.switch_additem_visibility);

            if (itemName.matches("")) {
                Toast.makeText(this, "Please enter a name for your item",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (publicListsNames.contains(listName) && switchy.isChecked()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("Name", itemName);

                    db.collection("items")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("Debug", "DocumentSnapshot successfully written!");
                                    String itemId = documentReference.getId();

                                    String listId = "";
                                    for (String s : publicLists.keySet()) {
                                        if (publicLists.get(s).equals(listName)) {
                                            listId = s;
                                        }
                                    }

                                    db.collection("lists").document(listId)
                                            .update("Lists", FieldValue.arrayUnion(itemId))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Debug", "DocumentSnapshot successfully updated!");
                                            finish();
                                            Toast.makeText(AddItemActivity.this,
                                                    "\"" + itemName + "\"" + " successfully added to " + listName,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Debug", "Error writing document", e);
                                }
                            });
                }
            }
        }

//        int myvar = 12;
//        SharedPreferences preferences = AddItemActivity.this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt("var1", myvar);
//        editor.commit();

        return super.onOptionsItemSelected(item);
    }
}