package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class OrdersMenuActivity extends MainActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;
    public static LinearLayout ll,ll2;
    public static FloatingActionButton fabToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.orders_menu_layout);

        fabToggle = (FloatingActionButton)findViewById(R.id.fab_toggle);
        fabToggle.setVisibility(View.GONE);



        ll = (LinearLayout)findViewById(R.id.food_orders_fragment);
        ll.setVisibility(View.GONE);
        ll2 = (LinearLayout)findViewById(R.id.food_menu_fragment);



        fabToggle.setOnClickListener(ocl);

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

    final View.OnClickListener ocl2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f));
            fabToggle.setImageResource(R.drawable.icon_up);
            ll2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,3f));
            fabToggle.setOnClickListener(ocl);
        }
    };


    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,3f));
            ll.startAnimation(AnimationUtils.loadAnimation(OrdersMenuActivity.this,R.anim.my_animation));
            fabToggle.setImageResource(R.drawable.icon_down);
//            ll2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f));
            fabToggle.setOnClickListener(ocl2);
        }
    };

}
