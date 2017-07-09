package com.example.mohsen.orderingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersAdapter extends RecyclerView.Adapter<FoodOrdersAdapter.ViewHolder> {

    Context mContext;
    public static List<OrderedItem> mList = new ArrayList<>();
    RecyclerView mRvv;
    LinearLayoutManager mRvlm;


    public FoodOrdersAdapter(Context context, List<OrderedItem> list, RecyclerView rvv, LinearLayoutManager rvlm) {
        mContext = context;
        mList = list;
        mRvv = rvv;
        mRvlm = rvlm;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv,tv2;
        public CardView cv;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.food_order_item_name);
            tv2 = (TextView) v.findViewById(R.id.food_order_item_tedad);
            cv = (CardView) v.findViewById(R.id.food_item_cardView);
        }
    }

    @Override
    public FoodOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_order_item_layout, parent, false);
        FoodOrdersAdapter.ViewHolder holder = new FoodOrdersAdapter.ViewHolder(v);
        return holder;
    }




    @Override
    public void onBindViewHolder(FoodOrdersAdapter.ViewHolder holder, int position) {

        holder.tv.setText(mList.get(position).mName);
        holder.tv2.setText(mList.get(position).mNumber+"");
        mRvv.smoothScrollToPosition(mRvv.getAdapter().getItemCount());
        setAnimation(holder.cv,position);
//        holder.cv.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
//        holder.tv.getParent()..setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
//        notifyItemInserted(position);
//        mRvv.scrollToPosition(position);
//        mRvlm.scrollToPosition(position);
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position == mList.size()-1)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(OrderedItem data) {
//        mList.add(data);
        notifyDataSetChanged();
    }


//    // Remove a RecyclerView item containing a specified Data object
//    public void remove(OrderedItem data) {
//        int position = mList.indexOf(data);
//        mList.remove(position);
//        notifyItemRemoved(position);
//    }


}
