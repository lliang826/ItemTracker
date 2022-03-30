package com.comp3717.itemtracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    public MyItemRecyclerViewAdapter(List<Item> items, @NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Item model) {
        if (position < super.getItemCount()) {
            // Public
            holder.mItem = model;
            holder.mContentView.setText(model.getName());
            holder.mContentView.setTextColor(Color.BLUE);
        } else {
            // Private
            position -= super.getItemCount();
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).getName());
            holder.mContentView.setTextColor(Color.BLACK);
        }
        holder.checkBox.setChecked(holder.mItem.isDone());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.mItem.setDone(isChecked);
                if (isChecked) {
                    ItemFragment.total_checked++;
                } else {
                    ItemFragment.total_checked--;
                }
                int percentage = 0;
                if(getItemCount() > 0) {
                    percentage = ItemFragment.total_checked * 100 / getItemCount() ;
                }
                ItemFragment.progressTextView.setText(percentage + "%");
                ItemFragment.progressBar.setProgress(percentage);
            }
        });
    }

    @NonNull
    @Override
    public Item getItem(int position) {
        return position < super.getItemCount() ? super.getItem(position) : mValues.get(position - super.getItemCount());
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