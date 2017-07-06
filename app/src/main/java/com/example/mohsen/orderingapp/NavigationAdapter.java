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
 * Created by Mohsen on 2017-06-29.
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    Context mContext;
    int mWidth;
    int[] mFoodsImages;
    String[] mFoodsNames;
    View v;
    FragmentManager mFragmentManager;
    DrawerLayout mDrawer;

    public NavigationAdapter(Context context, int width, int[] foodsImages, String[] foodsNames, FragmentManager fragmentManager, DrawerLayout drawer) {
        mContext = context;
        mWidth = width;
        mFoodsImages = foodsImages;
        mFoodsNames = foodsNames;
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
        holder.tv.setText(mFoodsNames[position]);
        holder.iv.setImageResource(mFoodsImages[position]);
        holder.iv.getLayoutParams().width = mWidth/5;
        holder.iv.getLayoutParams().height = mWidth/5;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                FoodMenuFragment foodMenuFragment = new FoodMenuFragment(mContext,position,mFragmentManager);
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
        return mFoodsImages.length;
    }
}
