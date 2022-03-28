package com.comp3717.itemtracker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 */
public class ListFragment extends Fragment {

    private MyListRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_list, container, false);

        Query query = FirebaseFirestore.getInstance()
                .collection("lists2");

        FirestoreRecyclerOptions<List> options = new FirestoreRecyclerOptions.Builder<List>()
                .setQuery(query, List.class)
                .build();

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_listlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyListRecyclerViewAdapter(ListManager.getInstance().getPrivateLists(), recyclerView, options);
        recyclerView.setAdapter(adapter);

        // swiping left allows the user to delete a list
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List list = adapter.getItem(viewHolder.getBindingAdapterPosition());

                // set up delete confirmation popup
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to delete \"" + list.getName() + "\"?");
                builder.setPositiveButton("Confirm",
                        (dialog, which) -> {
                            // delete the swiped list
                            deleteList(list, context, viewHolder);
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which)
                        -> adapter.notifyItemChanged(viewHolder.getBindingAdapterPosition()));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void deleteList(List list, Context context, RecyclerView.ViewHolder viewHolder) {
        if (list.getId() == null) {
            ListManager.getInstance().removePrivateList(list);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<String> documentIDs = new ArrayList<>();
            db.collection("lists2")
                    .document(list.getId()).collection("items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d("Debug", document.getId() + " => " + document.getData());
                                documentIDs.add(document.getId());
                            }
                        } else {
                            Log.d("Debug", "Error getting sub-collection documents: ",
                                    task.getException());
                        }
                    });

            for (String doc : documentIDs) {
                db.collection("lists2").document(list.getId())
                        .collection("items").document(doc)
                        .delete()
                        .addOnSuccessListener(unused -> Log.d("Debug", "Sub-collection document deleted!"))
                        .addOnFailureListener(e -> Log.w("Debug", "Error deleting sub-collection document"));
            }

            db.collection("lists2").document(list.getId())
                    .delete()
                    .addOnSuccessListener(unused -> Log.d("Debug", "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("Debug", "Error deleting document"));
        }
        Toast.makeText(context, "\"" + list.getName() + "\"" + " successfully deleted",
                Toast.LENGTH_LONG).show();
        adapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}