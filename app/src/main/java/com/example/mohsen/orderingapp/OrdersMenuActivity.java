package com.example.mohsen.orderingapp;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrdersMenuActivity extends MainActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;
    public static LinearLayout ll,ll2;
    public static FloatingActionButton fabToggle;
    public static TextView tvTayid;
    int firstRun;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.orders_menu_layout);

        fabToggle = (FloatingActionButton)findViewById(R.id.fab_toggle);
        fabToggle.setVisibility(View.GONE);

        drawer.openDrawer(Gravity.START);

        ll = (LinearLayout)findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0f));
        ll2 = (LinearLayout)findViewById(R.id.food_menu_fragment);
        tvTayid = (TextView)findViewById(R.id.textView_tayid);
        tvTayid.setVisibility(View.GONE);
        tvTayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClass cdd=new CustomDialogClass(OrdersMenuActivity.this);
                cdd.show();
                Window window = cdd.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        fabToggle.setOnClickListener(ocl);

        tvTitlebar.setText(title + " - " + "منو");

        ivTitlebar.setVisibility(View.VISIBLE);
        ivTitlebar.setImageResource(R.drawable.settings_icon);
        ivTitlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrdersMenuActivity.this,SettingsActivity2.class);
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
        mNavigationRecycler.setNestedScrollingEnabled(false);

        mRecyclerManager = new LinearLayoutManager(this);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this,width,food.getFoodCategoryImages(),food.getFoodCategoryNames(),fragmentManager,drawer,food.getFoodCategoryCodes());
        mNavigationRecycler.setAdapter(mRecyclerAdapter);






//        if(SettingsActivity.isUpdated) {
//            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
//            FoodMenuFragment foodMenuFragment = new FoodMenuFragment(this, fragmentManager, food.getFoodCategoryCodes().get(1));
//            fragmentTransaction2.replace(R.id.food_menu_fragment, foodMenuFragment);
//            fragmentTransaction2.addToBackStack(null);
//            fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            fragmentTransaction2.commit();
//            drawer.closeDrawer(Gravity.START);
//        }

        NavigationAdapter.refreshFavorites();





    }

    public static View.OnClickListener ocl2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewWeightAnimationWrapper animationWrapper = new ViewWeightAnimationWrapper(ll);
            ObjectAnimator anim = ObjectAnimator.ofFloat(animationWrapper,
                    "weight",
                    animationWrapper.getWeight(),
                    1f);
            anim.setDuration(300);
            anim.setInterpolator(new FastOutLinearInInterpolator());
            anim.start();
            fabToggle.setImageResource(R.drawable.icon_up);
            fabToggle.setOnClickListener(ocl);
        }
    };


    public static View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ViewWeightAnimationWrapper animationWrapper = new ViewWeightAnimationWrapper(ll);
            ObjectAnimator anim = ObjectAnimator.ofFloat(animationWrapper,
                    "weight",
                    animationWrapper.getWeight(),
                    9f);
            anim.setDuration(500);
            anim.setInterpolator(new OvershootInterpolator());
            anim.start();
            fabToggle.setImageResource(R.drawable.icon_down);
            fabToggle.setOnClickListener(ocl2);
        }
    };

}
