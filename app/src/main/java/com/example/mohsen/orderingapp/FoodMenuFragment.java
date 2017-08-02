package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-01.
 */

public class FoodMenuFragment extends Fragment {

    RecyclerView rvv;
    RecyclerView.LayoutManager rvlm;
    RecyclerView.Adapter rva;
    Context mContext;
    int mPosition;
    FragmentManager mFragmentManager;
    int mFoodsCategoryCode;

    public FoodMenuFragment(Context context, int position, FragmentManager fragmentManager, String foodsCategoryCode) {
        mContext = context;
        mPosition = position;
        mFragmentManager = fragmentManager;
        mFoodsCategoryCode = Integer.parseInt(foodsCategoryCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.food_menu_layout, container, false);
        rvv = v.findViewById(R.id.food_menu_recyclerView);

        rvv.setHasFixedSize(true);

        rvlm = new GridLayoutManager(mContext,2);
        rvv.setLayoutManager(rvlm);

        SQLiteDatabase mydb = new MyDatabase(mContext).getReadableDatabase();

        Cursor cur = mydb.query(MyDatabase.FOOD_TABLE,new String[]{MyDatabase.NAME,MyDatabase.IMAGE,MyDatabase.CODE},MyDatabase.CATEGORY_CODE + " = ?",new String[]{mFoodsCategoryCode+""},null,null,null);
        ArrayList<String> foodsNames = new ArrayList<>();
        ArrayList<String> foodsImages = new ArrayList<>();
        ArrayList<String> foodsCodes = new ArrayList<>();
        if(cur.moveToFirst()) {
            do {
                foodsNames.add(cur.getString(0));
                foodsImages.add(cur.getString(1));
                foodsCodes.add(cur.getString(2));
            } while (cur.moveToNext());
        }


        rva = new FoodMenuAdapter(mContext,foodsImages,foodsNames,mFragmentManager,foodsCodes);

        rvv.setAdapter(rva);
        return v;
    }


}
