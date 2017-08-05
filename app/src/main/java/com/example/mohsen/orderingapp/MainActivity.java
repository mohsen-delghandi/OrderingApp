package com.example.mohsen.orderingapp;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-07-11.
 */

public class MainActivity extends AppCompatActivity {

    LayoutInflater inflater;
    LinearLayout ns;
    Toolbar toolbar;
    DrawerLayout drawer;
    int width;
    FloatingActionButton fab;
    TextView tvTitlebar;
    ImageView ivTitlebar;
    ActionBarDrawerToggle toggle;
    NavigationView nv;
    String json,json2;
    JSONArray jsonArray,jsonArray2;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AVLoadingIndicatorView avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.smoothToShow();




        final AlertDialog progressDialog;
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();



//        final ProgressDialog progress = new ProgressDialog(MainActivity.this);
//        progress.setTitle("Loading");
//        progress.setMessage("Wait while loading...");
//        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();



        new Thread(new Runnable() {
            @Override
            public void run() {



                CallWebService cws = new CallWebService(MainActivity.this,"GetGroupFood","test");
                json = cws.Call("test");

                CallWebService cws2 = new CallWebService(MainActivity.this,"GetFood","test");
                json2 = cws2.Call("test");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, json2, Toast.LENGTH_SHORT).show();
                    }
                });

                try {
                    jsonArray = new JSONArray(json);
                    jsonArray2 = new JSONArray(json2);

                    SQLiteDatabase db = new MyDatabase(MainActivity.this).getWritableDatabase();
                    db.delete(MyDatabase.FOOD_CATEGORY_TABLE,null,null);
                    db.delete(MyDatabase.FOOD_TABLE,null,null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(MainActivity.this, jsonArray.length()+"", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, jsonArray2.length()+"", Toast.LENGTH_SHORT).show();

                        }
                    });
                    for(int i = 0 ; i < jsonArray.length()-1 ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.CODE,jsonObject.get("ID")+"");
                        cv.put(MyDatabase.NAME,jsonObject.get("Name_Group")+"");
                        cv.put(MyDatabase.IMAGE,Base64.decode(jsonObject.get("Pic")+"", Base64.DEFAULT));
                        db.insert(MyDatabase.FOOD_CATEGORY_TABLE,null,cv);
                    }
                    for(int i = 0 ; i < jsonArray2.length()-1 ; i++){
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.CODE,jsonObject2.get("ID")+"");
                        cv.put(MyDatabase.NAME,jsonObject2.get("Name_Food")+"");
                        cv.put(MyDatabase.IMAGE,Base64.decode(jsonObject2.get("Pic_Food")+"", Base64.DEFAULT));
                        cv.put(MyDatabase.CATEGORY_CODE,jsonObject2.get("ID_Group")+"");
                        cv.put(MyDatabase.PRICE,jsonObject2.get("Price_Food")+"");
                        db.insert(MyDatabase.FOOD_TABLE,null,cv);
                    }
                    db.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                progress.dismiss();
                progressDialog.dismiss();
//                avi.hide();
            }
        }).start();

//        byte[] decodedString = Base64.decode(jsonObject.get("Pic"), Base64.DEFAULT);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        tvTitlebar = (TextView)findViewById(R.id.titleBar_title);

        ivTitlebar = (ImageView)findViewById(R.id.titleBar_icon);
        ivTitlebar.setVisibility(View.GONE);



        ns = (LinearLayout) findViewById(R.id.nestedscrollview);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Screen Size

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        //Navigation Size

        nv = (NavigationView)findViewById(R.id.nav_view);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nv.getLayoutParams();
        params.width = width/4;
        nv.setLayoutParams(params);
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

    protected View setInflater(Context context, @LayoutRes int resource){

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,ns);

        return view;
    }
}
