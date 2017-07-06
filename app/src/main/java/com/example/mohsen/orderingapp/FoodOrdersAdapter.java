package com.example.mohsen.orderingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersAdapter extends RecyclerView.Adapter<FoodOrdersAdapter.ViewHolder> {

    Context mContext;
    int mFoodCode,mFoodCount;
    public static ArrayList<Integer> mFoodCounts = new ArrayList<>();
    public static ArrayList<Integer> mFoodCodes = new ArrayList<>();
    RecyclerView mRvv;
    boolean doAnimate;
    int mPosition;

    public FoodOrdersAdapter(Context context, int foodCode, RecyclerView rvv) {
        mContext = context;
        mFoodCode = foodCode;
        mRvv = rvv;
        doAnimate = false;
        int i;
        for (i = 0 ; i < mFoodCodes.size() ; i++){
            if (mFoodCodes.get(i) == mFoodCode){
                mFoodCodes.add(mFoodCode);
                mFoodCount = mFoodCounts.get(i);
                mFoodCounts.add(mFoodCount+1);
                mFoodCounts.remove(i);
                mFoodCodes.remove(i);
//                mFoodCount = mFoodCounts.get(i);
//                mPosition = i;
                i = mFoodCodes.size()+1;
                doAnimate = true;
            }
        }
        if (i == mFoodCodes.size()){
            mFoodCodes.add(foodCode);
            mFoodCounts.add(1);
            mFoodCount = 1;
            doAnimate = true;
//            mPosition = mFoodCounts.size()-1;
        }else{
            doAnimate = false;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv,tv2;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.food_order_item_name);
            tv2 = (TextView) v.findViewById(R.id.food_order_item_tedad);
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

        SQLiteDatabase mydb = new MyDatabase(mContext).getReadableDatabase();
        Cursor cur = mydb.query(MyDatabase.FOOD_TABLE,new String[]{MyDatabase.NAME},MyDatabase.CODE + " = ?",new String[]{mFoodCodes.get(position)+""},null,null,null);
        cur.moveToFirst();
        holder.tv.setText(cur.getString(0));
        holder.tv2.setText(mFoodCounts.get(position)+"");
        cur.close();
        mydb.close();
        mRvv.scrollToPosition(position);
        if(doAnimate){
            setAnimation(holder.itemView, position);
        }
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position == mFoodCounts.size()-1)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
        }
    }


    @Override
    public int getItemCount() {
        return mFoodCounts.size();
    }

}
