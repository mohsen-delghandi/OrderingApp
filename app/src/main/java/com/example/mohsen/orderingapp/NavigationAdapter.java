package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    Context mContext;
    int mWidth;
    ArrayList<String> mFoodsCategoryImages;
    ArrayList<String> mFoodsCategoryNames;
    ArrayList<String> mFoodsCategoryCodes;
    View v;
    FragmentManager mFragmentManager;
    DrawerLayout mDrawer;

    public NavigationAdapter(Context context, int width, ArrayList<String> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, DrawerLayout drawer, ArrayList<String> foodCategoryCodes) {
        mContext = context;
        mWidth = width;
        mFoodsCategoryImages = foodsImages;
        mFoodsCategoryNames = foodsNames;
        mFoodsCategoryCodes = foodCategoryCodes;
        mFragmentManager = fragmentManager;
        mDrawer = drawer;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ImageView iv;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.nav_textView);
            iv = (ImageView)v.findViewById(R.id.nav_imageView);
        }
    }

    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(mFoodsCategoryNames.get(position));

        byte[] decodedString = Base64.decode(mFoodsCategoryImages.get(position), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.iv.setImageBitmap(decodedByte);
        holder.iv.getLayoutParams().width = mWidth/5;
        holder.iv.getLayoutParams().height = mWidth/5;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                FoodMenuFragment foodMenuFragment = new FoodMenuFragment(mContext,position,mFragmentManager,mFoodsCategoryCodes.get(position));
                fragmentTransaction.replace(R.id.food_menu_fragment,foodMenuFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                mDrawer.closeDrawer(Gravity.START);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodsCategoryImages.size();
    }
}
