package co.sansystem.orderingapp.UI.Activities;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.sansystem.orderingapp.R;

import java.util.List;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Models.Food;
import co.sansystem.orderingapp.UI.Dialogs.CustomDialogClass;
import co.sansystem.orderingapp.UI.Fragments.FoodOrdersFragment;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;

public class OrdersMenuActivity extends MainActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;
    public static LinearLayout ll;
    public static ImageView fabToggle;
    public static FrameLayout frOrders;
    public static TextView tvTayid;
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

        fabToggle = (ImageView) findViewById(R.id.fab_toggle);
        fabToggle.setVisibility(View.GONE);

        frOrders = (FrameLayout) findViewById(R.id.frameLayout_order);
        frOrders.setVisibility(View.GONE);

        drawer.openDrawer(Gravity.START);
        appPreferenceTools = new AppPreferenceTools(this);
        costumerCode = appPreferenceTools.getDefaultCostumerCode();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_happy);

        ll = (LinearLayout) findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0));
        tvTayid = (TextView) findViewById(R.id.textView_tayid);
        tvTayid.setVisibility(View.GONE);
        tvTayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getExtras() != null){
                    cdd = new CustomDialogClass(OrdersMenuActivity.this, costumerCode,
                            getIntent().getExtras().getString("tableNumber"),getIntent().getExtras().getString("Costumer_Name"),
                            getIntent().getExtras().getString("Vaziat_Sefaresh"),getIntent().getExtras().getString("Factor_Number"),
                            getIntent().getExtras().getString("Fish_Number"));
                }else {
                    cdd = new CustomDialogClass(OrdersMenuActivity.this, costumerCode);
                }
                cdd.show();
                cdd.setCancelable(false);
                Window window = cdd.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            }
        });

        fabToggle.setOnClickListener(ocl);

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

//                    cdd2.yes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent i = new Intent(OrdersMenuActivity.this, SettingsActivity.class);
//                            startActivity(i);
//                            finish();
//                        }
//                    });
                }
            }
        });


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment foodOrdersFragment = null;
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getBoolean("editMode")){
                foodOrdersFragment = new FoodOrdersFragment(this, height,FoodOrdersAdapter.mList);

                OrdersMenuActivity.fabToggle.setImageResource(R.drawable.icon_up);
                OrdersMenuActivity.fabToggle.setVisibility(View.VISIBLE);

                OrdersMenuActivity.tvTayid.setVisibility(View.VISIBLE);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
                    params.height = MainActivity.height / 3;
                    OrdersMenuActivity.ll.setLayoutParams(params);
//                }
            }
        }else {
            foodOrdersFragment = new FoodOrdersFragment(this, height);
        }
        fragmentTransaction.add(R.id.food_orders_fragment, foodOrdersFragment);
        fragmentTransaction.commit();

        //Navigation recycler

        mNavigationRecycler = (RecyclerView) findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);
        mNavigationRecycler.setNestedScrollingEnabled(false);

        mRecyclerManager = new GridLayoutManager(this,3);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this, width, food.getFoodCategoryImages(), food.getFoodCategoryNames(), fragmentManager, drawer, food.getFoodCategoryCodes(), height);
        mNavigationRecycler.setAdapter(mRecyclerAdapter);




        NavigationAdapter.refreshFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.more_tab_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.lastFactors:
                startActivity(new Intent(this,LastFactorsActivity.class));
                break;
            case R.id.report:
                startActivity(new Intent(this,ReportActivity.class));
                break;
        }
        return true;
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
