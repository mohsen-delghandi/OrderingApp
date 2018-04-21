package co.sansystem.orderingapp.UI.Activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sansystem.orderingapp.R;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Models.Food;
import co.sansystem.orderingapp.UI.Dialogs.CustomDialogClass;
import co.sansystem.orderingapp.UI.Fragments.FoodOrdersFragment;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;

public class OrdersMenuActivity extends MainActivity {

    private RecyclerView mNavigationRecycler;
    private RecyclerView.LayoutManager mRecyclerManager;
    private RecyclerView.Adapter mRecyclerAdapter;
    public static LinearLayout ll;
    public static FrameLayout frOrders;
    public static TextView tvTayid;
    private static TextView tvTour;
    public static LinearLayout linearLayout;
    private CustomDialogClass cdd;
    private AppPreferenceTools appPreferenceTools;
    private String costumerCode;
    private int h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.orders_menu_layout);
        frOrders = findViewById(R.id.frameLayout_order);
        frOrders.setVisibility(View.GONE);

        tvTour = findViewById(R.id.textView_tour);
        drawer.openDrawer(Gravity.START);
        appPreferenceTools = new AppPreferenceTools(this);
        costumerCode = appPreferenceTools.getDefaultCostumerCode();

        linearLayout = findViewById(R.id.linearLayout_happy);

        ll = findViewById(R.id.food_orders_fragment);
        ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0));
        tvTayid = findViewById(R.id.textView_tayid);
        tvTayid.setVisibility(View.GONE);
        tvTayid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getExtras() != null) {
                    cdd = new CustomDialogClass(OrdersMenuActivity.this, costumerCode,
                            getIntent().getExtras().getString("tableNumber"), getIntent().getExtras().getString("Costumer_Name"),
                            getIntent().getExtras().getString("Vaziat_Sefaresh"), getIntent().getExtras().getString("Factor_Number"),
                            getIntent().getExtras().getString("Fish_Number"));
                } else {
                    cdd = new CustomDialogClass(OrdersMenuActivity.this, costumerCode);
                }
                cdd.show();
                Window window = cdd.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            }
        });

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
                    Toast.makeText(OrdersMenuActivity.this, "با ورود به منوی تنظیمات، سبد خرید خالی می شود.", Toast.LENGTH_LONG).show();
                    ivTitlebar.setOnClickListener(new View.OnClickListener() {
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
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean("editMode")) {
                foodOrdersFragment = new FoodOrdersFragment(this, height, FoodOrdersAdapter.mList);

                OrdersMenuActivity.tvTayid.setVisibility(View.VISIBLE);


                if (FoodOrdersAdapter.mList.size() == 0) {

                    OrdersMenuActivity.tvTayid.setAlpha(1f);
                }

                OrdersMenuActivity.ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                OrdersMenuActivity.frOrders.setVisibility(View.VISIBLE);
            }
        } else {
            foodOrdersFragment = new FoodOrdersFragment(this, height);
        }
        fragmentTransaction.add(R.id.food_orders_fragment, foodOrdersFragment);
        fragmentTransaction.commit();

        mNavigationRecycler = findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);
        mNavigationRecycler.setNestedScrollingEnabled(false);

        mRecyclerManager = new GridLayoutManager(this, 3);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this, width, food.getFoodCategoryImages(), food.getFoodCategoryNames(), fragmentManager, drawer, food.getFoodCategoryCodes(), height);
        mNavigationRecycler.setAdapter(mRecyclerAdapter);


        NavigationAdapter.refreshFavorites();

        if(SplashActivity.tourNumber == 6) {
            SplashActivity.tourNumber++;
            TapTargetView.showFor(OrdersMenuActivity.this,                 // `this` is an Activity
                    TapTarget.forView(tvTour, "انتخاب غذا", "")
                            .textTypeface(Typeface.createFromAsset(
                                    getAssets(),
                                    "fonts/IRANSansWeb_Bold.ttf"))
                            // All options below are optional
                            .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                            .titleTextSize(25)                  // Specify the size (in sp) of the title text
                            .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                            .textColor(R.color.accent)            // Specify a color for both the title and description text
                            .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(false)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(100),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);

                        }
                    });
        }

        if(SplashActivity.tourNumber == 1) {
            SplashActivity.tourNumber++;
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {
                    h = toolbar.getMeasuredHeight();

                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) textView.getLayoutParams();
                    params.topMargin = getStatusBarHeight() + h / 2;
                    params.rightMargin = h / 2;

                    textView.setLayoutParams(params);


                    TapTargetView.showFor(OrdersMenuActivity.this,                 // `this` is an Activity
                            TapTarget.forView(findViewById(R.id.textView), "لیست های غذایی", "با لمس اینجا می توانید لیست های مختلف غذایی را مشاهده کنید.")
                                    .textTypeface(Typeface.createFromAsset(
                                            getAssets(),
                                            "fonts/IRANSansWeb_Bold.ttf"))
                                    // All options below are optional
                                    .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                                    .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                                    .titleTextSize(25)                  // Specify the size (in sp) of the title text
                                    .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                    .textColor(R.color.accent)            // Specify a color for both the title and description text
                                    .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                    .tintTarget(false)                   // Whether to tint the target view's color
                                    .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                    .targetRadius(25),                  // Specify the target radius (in dp)
                            new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                @Override
                                public void onTargetClick(TapTargetView view) {
                                    super.onTargetClick(view);      // This call is optional
                                    drawer.openDrawer(Gravity.START);
                                }
                            });

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_tab_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lastFactors:
                startActivity(new Intent(this, LastFactorsActivity.class));
                break;
            case R.id.report:
                startActivity(new Intent(this, ReportActivity.class));
                break;
            case R.id.offlineFactors:
                startActivity(new Intent(this, OfflineFactorsActivity.class));
                break;
        }
        return true;
    }
}
