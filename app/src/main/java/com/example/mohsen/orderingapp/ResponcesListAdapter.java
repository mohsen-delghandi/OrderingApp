package com.example.mohsen.orderingapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class ResponcesListAdapter extends RecyclerView.Adapter<ResponcesListAdapter.ViewHolder> {

    Context mContext;
    View v;
    ArrayList<String> mResponces;

    public ResponcesListAdapter(Context context, ArrayList<String> responces) {
        mContext = context;
        mResponces = responces;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        LinearLayout ll;
        public ViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.responce_item_textView);
            ll = v.findViewById(R.id.linearLayout_responce_cardView);
        }
    }

    @Override
    public ResponcesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.responce_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(ResponcesListAdapter.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.ll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,holder.tv.getLayoutParams().width));
        holder.tv.setText(mResponces.get(position));
    }

    @Override
    public int getItemCount() {
        return mResponces.size();
    }

}

