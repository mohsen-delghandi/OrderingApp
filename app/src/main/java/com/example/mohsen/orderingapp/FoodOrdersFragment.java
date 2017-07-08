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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersFragment extends Fragment{

    RecyclerView rvv;
    RecyclerView.LayoutManager rvlm;
    Context mContext;
    static FoodOrdersAdapter rva;
    int mPosition;
    int mFoodCode;
    List<OrderedItem> mList = new ArrayList<>();

    public FoodOrdersFragment(Context context,int foodCode) {
        mContext = context;
        mFoodCode = foodCode;
    }

    public FoodOrdersFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.food_orders_layout, container, false);
        rvv = v.findViewById(R.id.food_orders_recyclerView);

        rvv.setHasFixedSize(true);




        rvlm = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        rvv.setLayoutManager(rvlm);

        rva = new FoodOrdersAdapter(mContext,mList);



        rvv.setAdapter(rva);



        return v;
    }

    public static void insert(String name,int number){
        OrderedItem data = new OrderedItem(name,number);
        if (number > 1){
            rva.remove(data);
        }
        rva.insert(new OrderedItem(name,number));
    }

}
