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

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Context mContext;
    static FoodOrdersAdapter foodOrdersAdapter;
    int mHeight;
    List<OrderedItem> mList = new ArrayList<>();

    public FoodOrdersFragment(Context context,int height,List<OrderedItem> mList) {
        mContext = context;
        mHeight = height;
        this.mList = mList;
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
        recyclerView = v.findViewById(R.id.food_orders_recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);




        linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        foodOrdersAdapter = new FoodOrdersAdapter(mContext,mList, recyclerView, linearLayoutManager,mHeight);



        recyclerView.setAdapter(foodOrdersAdapter);
//        recyclerView.scrollToPosition(mList.size());


        return v;
    }



    public static void insert(OrderedItem data,int number){
        foodOrdersAdapter.insert(data);
    }

}
