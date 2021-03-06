package com.comp3717.itemtracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp3717.itemtracker.databinding.FragmentItemBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item}.
 */
public class MyItemRecyclerViewAdapter extends FirestoreRecyclerAdapter<Item, MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Item> mValues;
    private final com.comp3717.itemtracker.List list;

    public MyItemRecyclerViewAdapter(List<Item> items, com.comp3717.itemtracker.List list, @NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
        mValues = items;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Item model) {
        holder.mItem = model;
        holder.mContentView.setText(model.getName());
        if (position < super.getItemCount()) {
            // Public
            holder.mContentView.setTextColor(Color.BLUE);
        } else {
            // Private
            holder.mContentView.setTextColor(Color.BLACK);
        }
        holder.checkBox.setChecked(model.isDone());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()) {
                    model.setDone(true);
                } else {
                    model.setDone(false);
                }
                setProgressBar();
            }
        });
    }

    public void setProgressBar() {
        float counter = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).isDone()) {
                counter++;
            }
        }
        counter = counter / getItemCount() * 100;
        ItemFragment.progressTextView.setText((int) counter + "%");
        ItemFragment.progressBar.setProgress((int) counter);
    }

    @NonNull
    @Override
    public Item getItem(int position) {
        return position < super.getItemCount()
                ? list.getCachedItem(super.getItem(position))
                : mValues.get(position - super.getItemCount());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final CheckBox checkBox;
        public Item mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.textviewItem;
            checkBox = binding.checkboxItem;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}