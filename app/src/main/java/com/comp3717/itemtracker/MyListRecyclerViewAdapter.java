package com.comp3717.itemtracker;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp3717.itemtracker.ListFragmentDirections.ActionListFragmentToItemFragment;
import com.comp3717.itemtracker.placeholder.PlaceholderContent;
import com.comp3717.itemtracker.placeholder.PlaceholderContent.PlaceholderItem;
import com.comp3717.itemtracker.databinding.FragmentListBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    private final RecyclerView mRecyclerView;

    public MyListRecyclerViewAdapter(List<PlaceholderItem> items, RecyclerView rcv) {
        mValues = items;
        mRecyclerView = rcv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @NonNull FragmentListBinding viewBinding = FragmentListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        viewBinding.getRoot().setOnClickListener(mOnClickListener);
        return new ViewHolder(viewBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentListBinding binding) {
            super(binding.getRoot());
            mContentView = binding.textviewList;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            PlaceholderItem item = mValues.get(itemPosition);
            ActionListFragmentToItemFragment action = ListFragmentDirections.actionListFragmentToItemFragment(new com.comp3717.itemtracker.List(item));
            Log.d("Debug", "Clicked option: " +  PlaceholderContent.LISTS.get(itemPosition));
            Navigation.findNavController(view).navigate(action);
        }
    }
}