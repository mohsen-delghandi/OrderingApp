package com.example.mohsen.orderingapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.orders_menu_layout);

        fabToggle = (FloatingActionButton)findViewById(R.id.fab_toggle);
        fabToggle.setVisibility(View.GONE);



        ll = (LinearLayout)findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0f));
//        ll.setVisibility(View.GONE);
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

        tvTitlebar.setText("منوی غذا");

//        ivTitlebar.setVisibility(View.VISIBLE);
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








        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
        FoodMenuFragment foodMenuFragment = new FoodMenuFragment(this,0,fragmentManager);
        fragmentTransaction2.replace(R.id.food_menu_fragment,foodMenuFragment);
        fragmentTransaction2.addToBackStack(null);
        fragmentTransaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction2.commit();
        drawer.closeDrawer(Gravity.START);





    }

    public static View.OnClickListener ocl2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f));
            ViewWeightAnimationWrapper animationWrapper = new ViewWeightAnimationWrapper(ll);
            ObjectAnimator anim = ObjectAnimator.ofFloat(animationWrapper,
                    "weight",
                    animationWrapper.getWeight(),
                    1f);
            anim.setDuration(300);
            anim.setInterpolator(new FastOutLinearInInterpolator());
            anim.start();
            fabToggle.setImageResource(R.drawable.icon_up);
//            ll2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,3f));
//            ViewWeightAnimationWrapper animationWrapper2 = new ViewWeightAnimationWrapper(ll2);
//            ObjectAnimator anim2 = ObjectAnimator.ofFloat(animationWrapper2,
//                    "weight",
//                    animationWrapper.getWeight(),
//                    3f);
//            anim.setDuration(2500);
//            anim.start();
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

//            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,3f));
            fabToggle.setImageResource(R.drawable.icon_down);

//            ViewWeightAnimationWrapper animationWrapper2 = new ViewWeightAnimationWrapper(ll2);
//            ObjectAnimator anim2 = ObjectAnimator.ofFloat(animationWrapper2,
//                    "weight",
//                    animationWrapper.getWeight(),
//                    1f);
//            anim.setDuration(2500);
//            anim.start();

//            ll2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f));
            fabToggle.setOnClickListener(ocl2);
        }
    };

}
