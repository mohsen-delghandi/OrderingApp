package com.example.mohsen.orderingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-07-11.
 */

public class OrdersBasketActivity extends MainActivity {

    TextView tv_ID, tv_Name, tv_Pic;
    ImageView iv;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String json, json2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.food_basket_layout);

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

        tv_ID = (TextView) findViewById(R.id.textView_ID);
        tv_Name = (TextView) findViewById(R.id.textView_Name_Group);
        tv_Pic = (TextView) findViewById(R.id.textView_Pic);
        iv = (ImageView) findViewById(R.id.image);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebService cws = new CallWebService(OrdersBasketActivity.this, "GetFood", "test");
                json = cws.Call("test");

                try {
                    jsonArray = new JSONArray(json);
                    jsonObject = jsonArray.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tv_ID.setText(jsonObject.get("ID") + "");
                                tv_Name.setText(jsonObject.get("Name_Food") + "");
                                tv_Pic.setText(jsonObject.get("Pic_Food") + "");

                                byte[] decodedString = Base64.decode(jsonObject.get("Pic_Food") + "", Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                iv.setImageBitmap(decodedByte);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }).start();


    }
}
