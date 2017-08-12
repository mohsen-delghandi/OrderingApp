package com.example.mohsen.orderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Mohsen on 2017-06-16.
 */

public class SettingsActivity extends MainActivity {


    Button bt_update,bt_save;
    EditText et_ip;
    String ip;
    String ip_regex = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
            + "|[1-9][0-9]|[0-9]))";

    public static String json = null,json2 = null;
    public static JSONArray jsonArray,jsonArray2;
    public static long id,id2;

    public SettingsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.settings_layout);

        tvTitlebar.setText(title + " - " + "تنظیمات");
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icon_back);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        bt_save = (Button)findViewById(R.id.button_save_settings);

        bt_update = (Button)findViewById(R.id.button_update);
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Pattern.matches(ip_regex,et_ip.getText().toString().trim())){
                    Toast.makeText(SettingsActivity.this, "آی پی به صورت صحیح واد نشده است.", Toast.LENGTH_SHORT).show();
                }else {

                    SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabase.IP, et_ip.getText().toString());
                    int u = db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                    db.close();
                    if (u == 1) {
                        Toast.makeText(SettingsActivity.this, "عملیات ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    } else if (u == 0) {
                        Toast.makeText(SettingsActivity.this, "عملیات ذخیره ناموفق بود.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "خطای نامشخص،با پشتیبانی تماس بگیرید.", Toast.LENGTH_SHORT).show();
                    }
                }
                updateMenu(SettingsActivity.this,ll_loading);
//                Intent i = new Intent(SettingsActivity.this,OrdersMenuActivity.class);
//                startActivity(i);
            }
        });






        SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.SETTINGS_TABLE,new String[]{MyDatabase.IP},null,null,null,null,null);
        cursor.moveToFirst();
        ip = cursor.getString(0);
        cursor.close();
        db.close();


        et_ip = (EditText) findViewById(R.id.editText_ip);
        et_ip.setText(ip);

//        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.save);
        bt_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(!Pattern.matches(ip_regex,et_ip.getText().toString().trim())){
                    Toast.makeText(SettingsActivity.this, "آی پی به صورت صحیح واد نشده است.", Toast.LENGTH_SHORT).show();
                }else {

                    SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabase.IP, et_ip.getText().toString());
                    int u = db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                    db.close();
                    if (u == 1) {
                        Toast.makeText(SettingsActivity.this, "عملیات ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    } else if (u == 0) {
                        Toast.makeText(SettingsActivity.this, "عملیات ذخیره ناموفق بود.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "خطای نامشخص،با پشتیبانی تماس بگیرید.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public void updateMenu(final Context context, final LinearLayout ll){

        if(ll!=null){
            ll.setVisibility(View.VISIBLE);
        }
        id = -1;
        id2 = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {



                CallWebService cws = new CallWebService(context,"GetGroupFood","test");
                json = cws.Call("test");

                CallWebService cws2 = new CallWebService(context,"GetFood","test");
                json2 = cws2.Call("test");

                try {
                    jsonArray = new JSONArray(json);
                    jsonArray2 = new JSONArray(json2);

                    SQLiteDatabase db = new MyDatabase(context).getWritableDatabase();
                    db.delete(MyDatabase.FOOD_CATEGORY_TABLE,null,null);
                    db.delete(MyDatabase.FOOD_TABLE,null,null);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                           // Toast.makeText(MainActivity.this, jsonArray.length()+"", Toast.LENGTH_SHORT).show();
////                            Toast.makeText(MainActivity.this, jsonArray2.length()+"", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                    for(int i = 0 ; i < jsonArray.length()-1 ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.CODE,jsonObject.get("ID")+"");
                        cv.put(MyDatabase.NAME,jsonObject.get("Name_Group")+"");
                        cv.put(MyDatabase.IMAGE,Base64.decode(jsonObject.get("Pic")+"", Base64.DEFAULT));
                        id = db.insert(MyDatabase.FOOD_CATEGORY_TABLE,null,cv);
                    }
                    for(int i = 0 ; i < jsonArray2.length()-1 ; i++){
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.CODE,jsonObject2.get("ID")+"");
                        cv.put(MyDatabase.NAME,jsonObject2.get("Name_Food")+"");
                        cv.put(MyDatabase.IMAGE,Base64.decode(jsonObject2.get("Pic_Food")+"", Base64.DEFAULT));
                        cv.put(MyDatabase.CATEGORY_CODE,jsonObject2.get("ID_Group")+"");
                        cv.put(MyDatabase.PRICE,jsonObject2.get("Price_Food")+"");
                        id2 = db.insert(MyDatabase.FOOD_TABLE,null,cv);
                    }
                    db.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((id!=-1) && (id2!=-1)){
                            Toast.makeText(context, "به روزرسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "خطا در بروزرسانی.", Toast.LENGTH_SHORT).show();
                        }
                        if(ll!=null){
                            ll.setVisibility(View.GONE);
                        }

                    }
                });



            }
        }).start();

    }

}
