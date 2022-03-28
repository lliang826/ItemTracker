package com.comp3717.itemtracker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    public static int total_checked = 0;
    public static TextView progressTextView;
    public static ProgressBar progressBar;

    private MyItemRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        List list = ItemFragmentArgs.fromBundle(getArguments()).getList();
        TextView titleTextView = view.findViewById(R.id.textview_itemlist_title);
        TextView subtitleTextView = view.findViewById(R.id.textview_itemlist_subtitle);
        TextView descriptionTextView = view.findViewById(R.id.textview_itemlist_description);
        progressTextView = view.findViewById(R.id.textview_itemlist_progress);
        progressBar =view.findViewById(R.id.progress_itemlist_horizontal);

        titleTextView.setText(list.getName());
        subtitleTextView.setText(list.getId() != null ? "Public List" : "Private List");
        descriptionTextView.setText(list.getDescription());

        DocumentReference document;
        if (list.getId() != null) {
            document = FirebaseFirestore.getInstance()
                    .collection("lists2").document(list.getId());
        } else {
            document = FirebaseFirestore.getInstance()
                    .collection("lists2").document();
        }
        Query query = document.collection("items");

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_itemlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyItemRecyclerViewAdapter(list.getPrivateItems(), options);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // swiping left allows the user to delete an item
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
                        Item item = adapter.getItem(viewHolder.getBindingAdapterPosition());

                        // set up delete confirmation popup
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setMessage("Are you sure you want to delete \"" + item.getName() + "\"?");
                        builder.setPositiveButton("Confirm",
                                (dialog, which) -> {
                                    // delete the swiped item
                                    deleteItem(list, item, context, viewHolder);
                                });
                        builder.setNegativeButton(android.R.string.cancel, (dialog, which)
                                -> adapter.notifyItemChanged(viewHolder.getBindingAdapterPosition()));
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void deleteItem(List list, Item item, Context context, RecyclerView.ViewHolder viewHolder) {
        if (list.getId() == null) {
            list.removePrivateItem(item);
        } else {
            FirebaseFirestore.getInstance().collection("lists2")
                    .document(list.getId()).collection("items")
                    .document(item.getId())
                    .delete()
                    .addOnSuccessListener(unused -> Log.d("Debug", "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("Debug", "Error deleting document"));
        }
        Toast.makeText(context, "\"" + item.getName() + "\"" +
                " successfully deleted", Toast.LENGTH_LONG).show();
        adapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
    }

    @Override
    public void onStart() {
        super.onStart();
        List list = ItemFragmentArgs.fromBundle(getArguments()).getList();
        if (list.getId() != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        List list = ItemFragmentArgs.fromBundle(getArguments()).getList();
        if (list.getId() != null) {
            adapter.stopListening();
        }
        total_checked = 0;
    }
}
