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

    public FoodMenuFragment(Context context, int position, FragmentManager fragmentManager) {
        mContext = context;
        mPosition = position;
        mFragmentManager = fragmentManager;
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

        Cursor cur = mydb.query(MyDatabase.FOOD_TABLE,new String[]{MyDatabase.NAME,MyDatabase.IMAGE,MyDatabase.CODE},MyDatabase.CATEGORY_CODE + " = ?",new String[]{"100"+(mPosition+1)},null,null,null);
        ArrayList<String> foodCategoryNames = new ArrayList<>();
        ArrayList<Integer> foodCategoryImages = new ArrayList<>();
        ArrayList<Integer> foodCodes = new ArrayList<>();
        if(cur.moveToFirst()) {
            do {
                foodCategoryNames.add(cur.getString(0));
                foodCategoryImages.add(cur.getInt(1));
                foodCodes.add(cur.getInt(2));
            } while (cur.moveToNext());
        }


        rva = new FoodMenuAdapter(mContext,foodCategoryImages,foodCategoryNames,mFragmentManager,foodCodes);

        rvv.setAdapter(rva);
        return v;
    }


}
