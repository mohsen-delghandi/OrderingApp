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

    static Context mContext;
    int mWidth;
    static int mHeight;
    ArrayList<byte[]> mFoodsCategoryImages;
    ArrayList<String> mFoodsCategoryNames;
    ArrayList<String> mFoodsCategoryCodes;
    View v;
    static FragmentManager mFragmentManager;
    static DrawerLayout mDrawer;
    public static boolean isFavorite;

    public NavigationAdapter(Context context, int width, ArrayList<byte[]> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, DrawerLayout drawer, ArrayList<String> foodCategoryCodes,int height) {
        mContext = context;
        mWidth = width;
        mFoodsCategoryImages = foodsImages;
        mFoodsCategoryNames = foodsNames;
        mFoodsCategoryCodes = foodCategoryCodes;
        mFragmentManager = fragmentManager;
        mDrawer = drawer;
        mHeight = height;
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
    public void onBindViewHolder(NavigationAdapter.ViewHolder holder, int position) {
        if (position == 0){
            holder.tv.setText("خواستنی ها");
            holder.setIsRecyclable(false);
            holder.iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
            holder.iv.getLayoutParams().width = mWidth / 5;
            holder.iv.getLayoutParams().height = mWidth / 5;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshFavorites();
                }
            });
        }else{
            position --;
            holder.tv.setText(mFoodsCategoryNames.get(position));
            holder.setIsRecyclable(false);
//        byte[] decodedString = Base64.decode(mFoodsCategoryImages.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(mFoodsCategoryImages.get(position), 0, mFoodsCategoryImages.get(position).length);
            holder.iv.setImageBitmap(decodedByte);
            holder.iv.getLayoutParams().width = mWidth / 5;
            holder.iv.getLayoutParams().height = mWidth / 5;

            final int finalPosition = position;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    FoodMenuFragment foodMenuFragment = new FoodMenuFragment(mContext, mFragmentManager, mFoodsCategoryCodes.get(finalPosition),mHeight);
                    fragmentTransaction.replace(R.id.food_menu_fragment, foodMenuFragment);
//                fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();
                    mDrawer.closeDrawer(Gravity.START);
                    isFavorite = false;
                    OrdersMenuActivity.linearLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    public static void refreshFavorites(){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        FoodMenuFragment foodMenuFragment = new FoodMenuFragment(mContext, mFragmentManager,"favorites",mHeight);
//        if(FoodMenuFragment.isFavoriteEmpty){
//            OrdersMenuActivity.linearLayout.setVisibility(View.VISIBLE);
//        }else{
//            OrdersMenuActivity.linearLayout.setVisibility(View.GONE);
//        }
        fragmentTransaction.replace(R.id.food_menu_fragment, foodMenuFragment);
//                fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        mDrawer.closeDrawer(Gravity.START);
        isFavorite = true;

    }

    @Override
    public int getItemCount() {
        return mFoodsCategoryImages.size()+1;
    }
}
