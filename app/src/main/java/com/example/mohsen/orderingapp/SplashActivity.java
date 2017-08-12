package com.example.mohsen.orderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-06-18.
 */

public class SplashActivity extends AppCompatActivity {

    int firstRun;
    EditText etIPFirstRun;
    LinearLayout linearLayout,llLoadingSplash;
    Button button;
    String ip_regex = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
            + "|[1-9][0-9]|[0-9]))";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        SQLiteDatabase db = new MyDatabase(this).getWritableDatabase();
        db.delete(MyDatabase.ORDERS_TABLE, null, null);
        db.close();

        FoodOrdersAdapter.mList.clear();

        etIPFirstRun = (EditText)findViewById(R.id.editText_ip_firstrun);
        linearLayout = (LinearLayout) findViewById(R.id.ll_settings_splash);
        llLoadingSplash = (LinearLayout) findViewById(R.id.llLoading_splash);
        button = (Button) findViewById(R.id.button_save_firstRun);

        SQLiteDatabase dbb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = dbb.query(MyDatabase.SETTINGS_TABLE,new String[]{MyDatabase.FIRST_RUN},null,null,null,null,null,null);
        cursor.moveToFirst();
        firstRun = cursor.getInt(0);
        cursor.close();
        dbb.close();

        if (firstRun == 1){

            linearLayout.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!Pattern.matches(ip_regex,etIPFirstRun.getText().toString().trim())){
                        Toast.makeText(SplashActivity.this, "آی پی به صورت صحیح واد نشده است.", Toast.LENGTH_SHORT).show();
                    }else {
                        SQLiteDatabase db = new MyDatabase(SplashActivity.this).getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.IP, etIPFirstRun.getText().toString());
                        int u = db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                        db.close();
                        linearLayout.setVisibility(View.GONE);
                    }

                    SettingsActivity sa = new SettingsActivity();
                    sa.updateMenu(SplashActivity.this,llLoadingSplash);
                    SQLiteDatabase db2 = new MyDatabase(SplashActivity.this).getWritableDatabase();
                    ContentValues cv2 = new ContentValues();
                    cv2.put(MyDatabase.FIRST_RUN, 0);
                    db2.update(MyDatabase.SETTINGS_TABLE, cv2, MyDatabase.ID + " = ?", new String[]{"1"});
                    db2.close();

                    new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                        @Override
                        public void run() {
                            Intent i = new Intent(SplashActivity.this, OrdersMenuActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }, 2000);
                }
            });


        }else{
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, OrdersMenuActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }, 2000);
        }



//            new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//                @Override
//                public void run() {
//                    Intent i = new Intent(SplashActivity.this, SettingsActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    finish();
//                }
//            }, 2000);


    }
}