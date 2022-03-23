package com.comp3717.itemtracker;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp3717.itemtracker.databinding.FragmentItemBinding;
import com.comp3717.itemtracker.placeholder.PlaceholderContent;
import com.comp3717.itemtracker.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<String> mValues;
    public MyItemRecyclerViewAdapter(ArrayList<String> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PlaceholderItem result = PlaceholderContent.ITEM_MAP.get(mValues.get(position));
        if (result != null) {
            holder.mContentView.setText(result.content);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final CheckBox checkBox;
        public PlaceholderItem mItem;

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