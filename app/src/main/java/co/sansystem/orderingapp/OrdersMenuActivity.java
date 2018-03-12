package co.sansystem.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
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
import android.widget.VideoView;

import com.sansystem.mohsen.orderingapp.R;

import java.util.List;

public class OrdersMenuActivity extends MainActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;
    public static LinearLayout ll, ll2;
    public static FloatingActionButton fabToggle;
    public static TextView tvTayid;
    int firstRun;
    public static LinearLayout linearLayout;
    CustomDialogClass cdd, cdd2;
    AppPreferenceTools appPreferenceTools;
    String costumerCode;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.orders_menu_layout);

        fabToggle = (FloatingActionButton) findViewById(R.id.fab_toggle);
        fabToggle.setVisibility(View.GONE);

        drawer.openDrawer(Gravity.START);
        appPreferenceTools = new AppPreferenceTools(this);
        costumerCode = appPreferenceTools.getDefaultCostumerCode();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_happy);
//        linearLayout.setVisibility(View.GONE);

        ll = (LinearLayout) findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
        ll2 = (LinearLayout) findViewById(R.id.food_menu_fragment);
        tvTayid = (TextView) findViewById(R.id.textView_tayid);
        tvTayid.setVisibility(View.GONE);
        tvTayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd = new CustomDialogClass(OrdersMenuActivity.this, costumerCode);
                cdd.show();
                cdd.setCancelable(false);
                Window window = cdd.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        fabToggle.setOnClickListener(ocl);

        tvTitlebar.setText(title + " - " + "کاربر " + appPreferenceTools.getUserName());

        ivTitlebar.setVisibility(View.VISIBLE);
        ivTitlebar.setImageResource(R.drawable.settings_icon);
        ivTitlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FoodOrdersAdapter.mList.size() == 0) {
                    Intent i = new Intent(OrdersMenuActivity.this, SettingsActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    cdd2 = new CustomDialogClass(OrdersMenuActivity.this, costumerCode);
                    cdd2.show();
                    Window window = cdd2.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cdd2.jameKol.setVisibility(View.GONE);
                    cdd2.tv.setText("هشدار");
                    cdd2.no.setText("خیر");
                    cdd2.text.setText("با ورود به تنظیمات لیست سفارش خالی می شود،آیا مطمئن هستید؟");
                    cdd2.trVaziat.setVisibility(View.GONE);
                    cdd2.text.setTextSize(25);
                    cdd2.text.setPadding(40, 40, 40, 40);
                    cdd2.etTable.setVisibility(View.GONE);
                    cdd2.llLoadingDialog.setVisibility(View.GONE);
                    cdd2.tvNameMoshtari.setVisibility(View.GONE);
                    cdd2.etName.setVisibility(View.GONE);
                    cdd2.tlMain.setAlpha(1f);
                    cdd2.yes.setText("بله");
                    cdd2.yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(OrdersMenuActivity.this, SettingsActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            }
        });


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FoodOrdersFragment foodOrdersFragment = new FoodOrdersFragment(this, height);
        fragmentTransaction.add(R.id.food_orders_fragment, foodOrdersFragment);
        fragmentTransaction.commit();

        //Navigation recycler

        mNavigationRecycler = (RecyclerView) findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);
        mNavigationRecycler.setNestedScrollingEnabled(false);

        mRecyclerManager = new LinearLayoutManager(this);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this, width, food.getFoodCategoryImages(), food.getFoodCategoryNames(), fragmentManager, drawer, food.getFoodCategoryCodes(), height);
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
        if (cdd != null && cdd.isShowing()) cdd.dismiss();
        super.startVideo(videoView, videoAddressList, i);
    }

    public static View.OnClickListener ocl2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                ValueAnimator va = ValueAnimator.ofInt(height * 2 / 3, height / 5);
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
            }else{
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
                params.height = height / 5;
                OrdersMenuActivity.ll.setLayoutParams(params);
            }



            fabToggle.setImageResource(R.drawable.icon_up);
            fabToggle.setOnClickListener(ocl);
        }
    };


    public static View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                ValueAnimator va = ValueAnimator.ofInt(height / 5, height * 2 / 3);
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
            }else{
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
                params.height = height * 2 / 3;
                OrdersMenuActivity.ll.setLayoutParams(params);
            }



            fabToggle.setImageResource(R.drawable.icon_down);
            fabToggle.setOnClickListener(ocl2);
        }
    };

}
