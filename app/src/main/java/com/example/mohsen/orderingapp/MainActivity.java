package com.example.mohsen.orderingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    RecyclerView mNavigationRecycler;
    RecyclerView.LayoutManager mRecyclerManager;
    RecyclerView.Adapter mRecyclerAdapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Screen Size

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        //Navigation Size

        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nv.getLayoutParams();
        params.width = width/4;
        nv.setLayoutParams(params);


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
