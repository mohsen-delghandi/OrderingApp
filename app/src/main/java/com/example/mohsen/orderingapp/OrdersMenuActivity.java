package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class OrdersMenuActivity extends MainActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.orders_menu_layout);

        tvTitlebar.setText("منوی غذا");

        ivTitlebar.setVisibility(View.VISIBLE);
        ivTitlebar.setImageResource(R.drawable.shop_icon);
        ivTitlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrdersMenuActivity.this,OrdersBasketActivity.class);
                startActivity(i);

            }
        });



        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FoodOrdersFragment foodOrdersFragment = new FoodOrdersFragment(this);
        fragmentTransaction.add(R.id.food_orders_fragment,foodOrdersFragment);
        fragmentTransaction.commit();

        //Navigation recycler

        mNavigationRecycler = (RecyclerView)findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);

        mRecyclerManager = new LinearLayoutManager(this);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        mRecyclerAdapter = new NavigationAdapter(this,width,Food.foodCategoryImages,Food.foodCategoryNames,fragmentManager,drawer);
        mNavigationRecycler.setAdapter(mRecyclerAdapter);
    }
}
