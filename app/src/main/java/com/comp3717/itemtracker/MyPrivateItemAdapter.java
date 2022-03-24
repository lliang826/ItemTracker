package com.comp3717.itemtracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyPrivateItemAdapter extends RecyclerView.Adapter<MyPrivateItemAdapter.ViewHolder> {

    private List<Item> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkbox;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.textview_item); //error here should be expected, this is a template
            checkbox = view.findViewById(R.id.checkbox_item);
        }

        public TextView getTextView() {
            return textView;
        }
        public CheckBox getCheckbox() {
            return checkbox;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public MyPrivateItemAdapter(List<Item> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_item, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getName());
        viewHolder.getCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}