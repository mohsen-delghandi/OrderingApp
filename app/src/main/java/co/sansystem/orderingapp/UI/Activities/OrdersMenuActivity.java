package co.sansystem.orderingapp.UI.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    public static FrameLayout frOrders;
    public static TextView tvTayid;
    public static LinearLayout linearLayout;
    CustomDialogClass cdd;
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

        mNavigationRecycler = (RecyclerView) findViewById(R.id.nav_recyclerView);
        mNavigationRecycler.setHasFixedSize(true);
        mNavigationRecycler.setNestedScrollingEnabled(false);

        mRecyclerManager = new GridLayoutManager(this, 3);
        mNavigationRecycler.setLayoutManager(mRecyclerManager);

        Food food = new Food(this);

        mRecyclerAdapter = new NavigationAdapter(this, width, food.getFoodCategoryImages(), food.getFoodCategoryNames(), fragmentManager, drawer, food.getFoodCategoryCodes(), height);
        mNavigationRecycler.setAdapter(mRecyclerAdapter);


        NavigationAdapter.refreshFavorites();

//        new ShowcaseView.Builder(this)
//                .setTarget(new ActionViewTarget(this, ActionViewTarget.Type.HOME))
//                .setContentTitle("ShowcaseView")
//                .setContentText("This is highlighting the Home button")
//                .hideOnTouchOutside()
//                .build();
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

    @Override
    public void startVideo(VideoView videoView, List<String> videoAddressList, int i) {
        if (cdd != null && cdd.isShowing()) cdd.dismiss();
        super.startVideo(videoView, videoAddressList, i);
    }


}
