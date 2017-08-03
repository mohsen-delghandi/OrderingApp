package com.example.mohsen.orderingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-07-11.
 */

public class OrdersBasketActivity extends MainActivity {

    long mPrice = 0;
    String mPriceFormatted = "";
    TextView tv_ID,tv_Name,tv_Pic;
    ImageView iv;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String json,json2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this,R.layout.food_basket_layout);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icon_back);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvTitlebar.setText("تایید سفارش");
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        fab.setVisibility(View.VISIBLE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        RecyclerView rv = (RecyclerView)findViewById(R.id.food_orders_list);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        rv.setAdapter(new BasketRecyclerAdapter(this));
//
//        for(int i = 0 ; i<FoodOrdersAdapter.mList.size() ; i++){
//            mPrice += FoodOrdersAdapter.mList.get(i).mNumber*1000;
//        }
//        TextView tv = (TextView)findViewById(R.id.textView_price);
//        int a =  (mPrice+"").length();
//        mPriceFormatted = (mPrice+"").substring(0,a%3);
//        for(int i = 0 ; i < (mPrice+"").length()/3 ; i++) {
//            if (!mPriceFormatted.equals("")) mPriceFormatted += ",";
//            mPriceFormatted += (mPrice+"").substring(a % 3 + 3 * i , a % 3 + 3 * (i+1));
//        }
//        tv.setText(mPriceFormatted+"");

        tv_ID = (TextView)findViewById(R.id.textView_ID);
        tv_Name = (TextView)findViewById(R.id.textView_Name_Group);
        tv_Pic = (TextView)findViewById(R.id.textView_Pic);
        iv = (ImageView)findViewById(R.id.image);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebService cws = new CallWebService(OrdersBasketActivity.this,"GetFood","test");
                json = cws.Call("test");

                try {
//                    Thread.sleep(2000);
                    jsonArray = new JSONArray(json);
                    jsonObject = jsonArray.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tv_ID.setText(jsonObject.get("ID")+"");
                                tv_Name.setText(jsonObject.get("Name_Food")+"");
                                tv_Pic.setText(jsonObject.get("Pic_Food")+"");

                                byte[] decodedString = Base64.decode(jsonObject.get("Pic_Food")+"", Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                iv.setImageBitmap(decodedByte);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }

            }

        }).start();









    }
}
