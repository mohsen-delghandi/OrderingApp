package com.example.mohsen.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

public class OrdersMenuActivity extends MainActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;
    public static LinearLayout ll,ll2;
    public static FloatingActionButton fabToggle;
    public static TextView tvTayid;
    int firstRun;
    public static LinearLayout linearLayout;
    CustomDialogClass cdd;

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

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_happy);
//        linearLayout.setVisibility(View.GONE);

        ll = (LinearLayout)findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0f));
        ll2 = (LinearLayout)findViewById(R.id.food_menu_fragment);
        tvTayid = (TextView)findViewById(R.id.textView_tayid);
        tvTayid.setVisibility(View.GONE);
        tvTayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd=new CustomDialogClass(OrdersMenuActivity.this);
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
        FoodOrdersFragment foodOrdersFragment = new FoodOrdersFragment(this,height);
        fragmentTransaction.add(R.id.food_orders_fragment,foodOrdersFragment);
        fragmentTransaction.commit();

        //Navigation recycler

        mNavigationRecycler = (RecyclerView)findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);
        mNavigationRecycler.setNestedScrollingEnabled(false);

        mRecyclerManager = new LinearLayoutManager(this);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this,width,food.getFoodCategoryImages(),food.getFoodCategoryNames(),fragmentManager,drawer,food.getFoodCategoryCodes(),height);
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

    @Override
    public void startVideo(VideoView videoView, List<String> videoAddressList, int i) {
        if(cdd!=null && cdd.isShowing())  cdd.dismiss();
        super.startVideo(videoView, videoAddressList, i);
    }

    public static View.OnClickListener ocl2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(ll);
//            ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,"height",animationWrapper.getHeight(),height/3);
//            anim.setDuration(300);
////            anim.setInterpolator(new FastOutLinearInInterpolator());
//            anim.start();

//            OrdersMenuActivity.ll.animate().y((float)height/4).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    OrdersMenuActivity.ll.setVisibility(View.GONE);
//                }
//            });

            ValueAnimator va = ValueAnimator.ofInt(height*2/3,height/5);
            va.setDuration(300);
            va.setInterpolator(new FastOutLinearInInterpolator());
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    OrdersMenuActivity.ll.getLayoutParams().height = value.intValue();
                    OrdersMenuActivity.ll.requestLayout();
                }
            });
            va.start();

            fabToggle.setImageResource(R.drawable.icon_up);
            fabToggle.setOnClickListener(ocl);
        }
    };




    public static View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(ll);
//            ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,"height",animationWrapper.getHeight(),height*2/3);
//            anim.setDuration(500);
////            anim.setInterpolator(new OvershootInterpolator());
//            anim.start();

//            OrdersMenuActivity.ll.animate().y((float)height*2/3).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//
//                }
//            });

            ValueAnimator va = ValueAnimator.ofInt(height/5,height*2/3);
            va.setDuration(500);
            va.setInterpolator(new OvershootInterpolator());
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    OrdersMenuActivity.ll.getLayoutParams().height = value.intValue();
                    OrdersMenuActivity.ll.requestLayout();
                }
            });
            va.start();

            fabToggle.setImageResource(R.drawable.icon_down);
            fabToggle.setOnClickListener(ocl2);
        }
    };

}
