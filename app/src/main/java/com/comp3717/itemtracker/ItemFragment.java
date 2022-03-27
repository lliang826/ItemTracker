package com.comp3717.itemtracker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
        titleTextView.setText(list.getName());
        descriptionTextView.setText(list.getDescription());
        progressTextView = view.findViewById(R.id.textview_itemlist_progress);
        progressBar =view.findViewById(R.id.progress_itemlist_horizontal);

        titleTextView.setText(list.getName());
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
        return view;
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
