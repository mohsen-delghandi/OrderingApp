package co.sansystem.orderingapp.UI.Activities;

import android.animation.ValueAnimator;
import android.app.Fragment;
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
    public static LinearLayout ll, ll2;
    public static FloatingActionButton fabToggle;
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

        fabToggle = (FloatingActionButton) findViewById(R.id.fab_toggle);
        fabToggle.setVisibility(View.GONE);

        drawer.openDrawer(Gravity.START);
        appPreferenceTools = new AppPreferenceTools(this);
        costumerCode = appPreferenceTools.getDefaultCostumerCode();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_happy);

        ll = (LinearLayout) findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0f));
        ll2 = (LinearLayout) findViewById(R.id.food_menu_fragment);
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
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        fabToggle.setOnClickListener(ocl);

        tvTitlebar.setText(title + " - " + "کاربر " + appPreferenceTools.getUserName());

        ivTitlebar.setVisibility(View.VISIBLE);
        ivTitlebarList.setVisibility(View.VISIBLE);
        ivTitlebarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrdersMenuActivity.this, LastFactorsActivity.class);
                startActivity(i);
            }
        });
        ivTitlebarOfflineFactors.setVisibility(View.VISIBLE);
        ivTitlebarOfflineFactors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrdersMenuActivity.this, OfflineFactorsActivity.class);
                startActivity(i);
            }
        });

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
                    cdd2.tvJameKol.setText("هشدار");
                    cdd2.no.setText("خیر");
                    cdd2.text.setText("با ورود به تنظیمات لیست سفارش خالی می شود،آیا مطمئن هستید؟");
//                    cdd2.trVaziat.setVisibility(View.GONE);
                    cdd2.text.setTextSize(25);
                    cdd2.text.setPadding(40, 40, 40, 40);
                    cdd2.etTable.setVisibility(View.GONE);
                    cdd2.llLoadingDialog.setVisibility(View.GONE);
                    cdd2.tvNameMoshtari.setVisibility(View.GONE);
                    cdd2.textView.setVisibility(View.GONE);
                    cdd2.tvJameKol.setVisibility(View.GONE);
                    cdd2.tvMaliat.setVisibility(View.GONE);
                    cdd2.tvMaliatText.setVisibility(View.GONE);
                    cdd2.tvTakhfif.setVisibility(View.GONE);
                    cdd2.tvTakhfifText.setVisibility(View.GONE);
                    cdd2.tvService.setVisibility(View.GONE);
                    cdd2.tvServiceText.setVisibility(View.GONE);
                    cdd2.tvFactor.setVisibility(View.GONE);
                    cdd2.tvFactorText.setVisibility(View.GONE);
                    cdd2.tvTell.setVisibility(View.GONE);
                    cdd2.tvAddress.setVisibility(View.GONE);
                    cdd2.textViewTell.setVisibility(View.GONE);
                    cdd2.textViewAddress.setVisibility(View.GONE);
                    cdd2.tvVaziat.setVisibility(View.GONE);
                    cdd2.spVaziatSefaresh.setVisibility(View.GONE);
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

        Fragment foodOrdersFragment = null;
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getBoolean("editMode")){
                foodOrdersFragment = new FoodOrdersFragment(this, height,FoodOrdersAdapter.mList);

                OrdersMenuActivity.fabToggle.setImageResource(R.drawable.icon_up);
                OrdersMenuActivity.fabToggle.setVisibility(View.VISIBLE);

                OrdersMenuActivity.tvTayid.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
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

        mRecyclerManager = new LinearLayoutManager(this);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this, width, food.getFoodCategoryImages(), food.getFoodCategoryNames(), fragmentManager, drawer, food.getFoodCategoryCodes(), height);
        mNavigationRecycler.setAdapter(mRecyclerAdapter);




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
