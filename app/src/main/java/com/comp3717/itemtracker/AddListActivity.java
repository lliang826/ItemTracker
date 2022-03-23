package com.comp3717.itemtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddListActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        Switch switch_widget = (findViewById(R.id.switch_addlist_visibility));
        String value = text_name.getText().toString();
        String detail_value = text_detail.getText().toString();
        boolean isChecked = switch_widget.isChecked();
        Map<String, Object> data = new HashMap<>();
        data.put("Name", value);
        data.put("Lists", new ArrayList<>());
        data.put("Detail", detail_value);
        String myvar = "HelloWorld";
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("var1", myvar);
        editor.commit();
        if (item.getItemId() == R.id.item_add_done) {
            if(isChecked && !value.isEmpty()) {
                db.collection("lists").whereEqualTo("Name", value)
                        .limit(1).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.isEmpty()) {
                            Log.d("MESSAGE", "doc not found");
                            db.collection("lists")
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("AddList", "DocumentSnapshot written with ID: " + documentReference.getId());
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("AddList", "Error adding document", e);
                                            alertDialog("Something wrong...");
                                        }
                                    });
                        } else {
                            for (QueryDocumentSnapshot document : querySnapshot){
                                alertDialog("Duplicate list name found. Please use another list name.");
                                Log.d("MESSAGE", "doc found");
                            }
                        }
                    } else {
                        Log.d("ERROR", String.valueOf(task.getException()));
                    }
                });
            } else if(value.isEmpty()) {
                alertDialog("Please enter a list name!");
            }
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void alertDialog(String str) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(str);
        dialog.setTitle("Warning");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}