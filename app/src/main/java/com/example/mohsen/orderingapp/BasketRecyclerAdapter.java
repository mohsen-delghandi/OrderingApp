package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mohsen on 2017-07-12.
 */

public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder> {

    Context mContext;
    String mPrice,mPriceFormatted = "";

    public BasketRecyclerAdapter(Context context) {
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv,tv2,tv3;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.textView_foodName);
            tv2 = (TextView)v.findViewById(R.id.textView_foodNumber);
            tv3 = (TextView)v.findViewById(R.id.textView_foodPrice);
        }
    }

    @Override
    public BasketRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_basket_items, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BasketRecyclerAdapter.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.tv.setText(FoodOrdersAdapter.mList.get(position).mName);
        holder.tv2.setText(FoodOrdersAdapter.mList.get(position).mNumber+"");
//        mPrice = (FoodOrdersAdapter.mList.get(position).mNumber*1000)+"";
        mPrice = FoodOrdersAdapter.mList.get(position).mNumber * 1000 +"";
        int a =  mPrice.length();
        mPriceFormatted = mPrice.substring(0,a%3);
        for(int i = 0 ; i < mPrice.length()/3 ; i++) {
            if (!mPriceFormatted.equals("")) mPriceFormatted += ",";
            mPriceFormatted += mPrice.substring(a % 3 + 3 * i , a % 3 + 3 * (i+1));
        }
        holder.tv3.setText(mPriceFormatted+"");
    }

    @Override
    public int getItemCount() {
        return FoodOrdersAdapter.mList.size();
    }
}