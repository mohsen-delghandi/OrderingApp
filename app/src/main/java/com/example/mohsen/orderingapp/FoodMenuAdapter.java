package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Integer> mFoodsImages,mFoodCodes;
    ArrayList<String> mFoodsNames;
    View v;
    FragmentManager mFragmentManager;

    public FoodMenuAdapter(Context context, ArrayList<Integer> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, ArrayList<Integer> foodCodes) {
        mContext = context;
        mFoodsImages = foodsImages;
        mFoodsNames = foodsNames;
        mFragmentManager = fragmentManager;
        mFoodCodes = foodCodes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ImageView iv;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.food_item_textView);
            iv = (ImageView)v.findViewById(R.id.food_item_imageView);
        }
    }

    @Override
    public FoodMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(FoodMenuAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(mFoodsNames.get(position));
        holder.iv.setImageResource(mFoodsImages.get(position));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                FoodOrdersFragment foodOrdersFragment = new FoodOrdersFragment(mContext,mFoodCodes.get(position));
                fragmentTransaction.replace(R.id.food_orders_fragment,foodOrdersFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodsImages.size();
    }

}

