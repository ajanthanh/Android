package com.sourmilq.sourmilq.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;
import com.sourmilq.sourmilq.R;
import com.sourmilq.sourmilq.callBacks.onCallCompleted;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Philip on 2016-10-15.
 */

public class GroceryItemListAdapter extends RecyclerView.Adapter<GroceryItemListAdapter.ViewHolder> implements Observer, ItemTouchHelperAdapter {
    private onCallCompleted listener;
    private ArrayList<Item> mDataset;
    private Model model;

    public GroceryItemListAdapter(Context context) {
        model = Model.getInstance(context);
        mDataset = model.getGroceryItems();
        model.addObserver(this);
//        update(model, null);
    }

    @Override
    public void update(Observable observable, Object data) {
        ArrayList<Item> updatedDataset = model.getGroceryItems();

        // only notify changes if changes exist (makes UI look better)
        COMPARE_NEW:
        {
            if (mDataset.size() != updatedDataset.size()) break COMPARE_NEW;
            int size = mDataset.size();
            for (int i = 0; i < size; i++) {
                if (!(mDataset.get(i).equals(updatedDataset.get(i))))
                    break COMPARE_NEW;
            }
            return;
        }
        mDataset = model.getGroceryItems();
        notifyDataSetChanged();
    }

    @Override
    public GroceryItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grocery_list_item, parent, false);
        ViewHolder vh = new ViewHolder(this, view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mDataset.get(position);
        holder.mTextView.setText(item.getName() + " (" + item.getNumItems() + ")");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Item newItem) {
        mDataset.add(newItem);
        notifyItemInserted(mDataset.size() - 1);
    }

    @Override
    public void onItemDismiss(int position) {
        Item itemToRemove = mDataset.get(position);
        remove(position);
        model.deleteItem(itemToRemove);
    }

    public ArrayList<Item> getDataset() {
        return mDataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnLongClickListener*/ {

        public TextView mTextView;
        public GroceryItemListAdapter mAdapter;

        public ViewHolder(GroceryItemListAdapter adapter, View v) {
            super(v);
            mAdapter = adapter;
            mTextView = (TextView) v.findViewById(R.id.info_text);
//            mTextView.setOnLongClickListener(this);
        }

//        @Override
//        public boolean onLongClick(View view) {
//            int position = getLayoutPosition();
////            mAdapter.remove(position);
//
//            mAdapter.model.deleteItem(mAdapter.getDataset().get(position));
//            return true;
//        }
    }
}
