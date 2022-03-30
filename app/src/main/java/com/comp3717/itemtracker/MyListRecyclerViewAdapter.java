package com.comp3717.itemtracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.comp3717.itemtracker.ListFragmentDirections.ActionListFragmentToItemFragment;
import com.comp3717.itemtracker.databinding.FragmentListBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.comp3717.itemtracker.List}.
 */
public class MyListRecyclerViewAdapter extends FirestoreRecyclerAdapter<com.comp3717.itemtracker.List, MyListRecyclerViewAdapter.ViewHolder> {

    private final List<com.comp3717.itemtracker.List> mValues;

    private final RecyclerView mRecyclerView;

    public MyListRecyclerViewAdapter(List<com.comp3717.itemtracker.List> items, RecyclerView rcv, @NonNull FirestoreRecyclerOptions<com.comp3717.itemtracker.List> options) {
        super(options);
        mValues = items;
        mRecyclerView = rcv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull com.comp3717.itemtracker.List model) {
        if (position < super.getItemCount()) {
            // Public
            holder.mItem = model;
            holder.mContentViewName.setText(model.getName());
            holder.mContentViewName.setTextColor(Color.BLUE);
            holder.mContentViewDescription.setText(model.getDescription());
        } else {
            // Private
            position -= super.getItemCount();
            holder.mItem = mValues.get(position);
            holder.mContentViewName.setText(mValues.get(position).getName());
            holder.mContentViewName.setTextColor(Color.BLACK);
            holder.mContentViewDescription.setText(mValues.get(position).getDescription());
        }

        View.OnClickListener onClickListener = new MyOnClickListener(holder.mItem);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @NonNull
    @Override
    public com.comp3717.itemtracker.List getItem(int position) {
        return position < super.getItemCount()
                ? ListManager.getInstance().getCachedList(super.getItem(position))
                : mValues.get(position - super.getItemCount());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentViewName;
        public final TextView mContentViewDescription;
        public com.comp3717.itemtracker.List mItem;

        public ViewHolder(FragmentListBinding binding) {
            super(binding.getRoot());
            mContentViewName = binding.textviewListName;
            mContentViewDescription = binding.textviewListDescription;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentViewName.getText() + "'";
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final com.comp3717.itemtracker.List item;

        public MyOnClickListener(com.comp3717.itemtracker.List item) {
            this.item = item;
        }

        @Override
        public void onClick(View view) {
            ActionListFragmentToItemFragment action = ListFragmentDirections.actionListFragmentToItemFragment(this.item);
            Navigation.findNavController(view).navigate(action);
        }
    }
}