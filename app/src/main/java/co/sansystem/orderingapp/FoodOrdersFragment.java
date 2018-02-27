package co.sansystem.orderingapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sansystem.mohsen.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersFragment extends Fragment{

    RecyclerView rvv;
    LinearLayoutManager rvlm;
    Context mContext;
    static FoodOrdersAdapter rva;
    int mPosition;
    int mFoodCode,mHeight;
    List<OrderedItem> mList = new ArrayList<>();

    public FoodOrdersFragment(Context context,int foodCode,int height) {
        mContext = context;
        mFoodCode = foodCode;
    }

    public FoodOrdersFragment(Context context,int height) {
        mContext = context;
        mHeight = height;
    }

    public FoodOrdersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.food_orders_layout, container, false);
        rvv = v.findViewById(R.id.food_orders_recyclerView);

        rvv.setHasFixedSize(true);
        rvv.setNestedScrollingEnabled(false);




        rvlm = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
//        rvlm.setStackFromEnd(true);
        rvv.setLayoutManager(rvlm);

        rva = new FoodOrdersAdapter(mContext,mList,rvv,rvlm,mHeight);



        rvv.setAdapter(rva);
//        rvv.scrollToPosition(mList.size());


        return v;
    }



    public static void insert(OrderedItem data,int number){
        rva.insert(data);
    }

}