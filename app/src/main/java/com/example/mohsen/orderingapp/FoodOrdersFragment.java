package com.example.mohsen.orderingapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersFragment extends Fragment{

    RecyclerView rvv;
    RecyclerView.LayoutManager rvlm;
    RecyclerView.Adapter rva;
    Context mContext;
    int mPosition;
    int mFoodCode;

    public FoodOrdersFragment(Context context,int foodCode) {
        mContext = context;
        mFoodCode =foodCode;
    }

    public FoodOrdersFragment(Context context) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.food_orders_layout, container, false);
        rvv = v.findViewById(R.id.food_orders_recyclerView);

        rvv.setHasFixedSize(true);

        rvlm = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        rvv.setLayoutManager(rvlm);



        rva = new FoodOrdersAdapter(mContext,mFoodCode,rvv);

        rvv.setAdapter(rva);
        rvv.scrollToPosition(rva.getItemCount()-1);
        return v;
    }
}
