package com.example.mohsen.orderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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


    Button bt_update;
    EditText et_ip;
    String ip;
    String ip_regex = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
            + "|[1-9][0-9]|[0-9]))";

    public static String json,json2;
    public static JSONArray jsonArray,jsonArray2;
    public static long id, id2;

    public SettingsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.settings_layout);


        bt_update = (Button)findViewById(R.id.button_update);
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMenu(SettingsActivity.this,ll_loading);
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

        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.save);
        fab.setOnClickListener(new View.OnClickListener() {

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
                        Snackbar.make(view, "عملیات ذخیره با موفقیت انجام شد.", Snackbar.LENGTH_LONG)
                                .setAction("صفحه اصلی", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }).show();

                    } else if (u == 0) {
                        Snackbar.make(view, "عملیات ذخیره ناموفق بود.", Snackbar.LENGTH_LONG)
                                .setAction("مشاهده خطا", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar.make(view, "خطای درج اطلاعات در پایگاه داده.", Snackbar.LENGTH_LONG)
                                                .setAction("مشاهده خطا", null).show();
                                    }
                                }).show();
                    } else {
                        Snackbar.make(view, "خطای نامشخص.", Snackbar.LENGTH_LONG)
                                .setAction("مشاهده خطا", null).show();
                    }
                }
            }
        });


    }

    public void updateMenu(final Context context, final LinearLayout ll){

        ll.setVisibility(View.VISIBLE);
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
                        ll.setVisibility(View.GONE);

                    }
                });



            }
        }).start();

    }
}
