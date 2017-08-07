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
import android.widget.LinearLayout;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-06-18.
 */

public class SplashActivity extends AppCompatActivity {

    int firstRun;
    LinearLayout ll_loading_splash;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ll_loading_splash = (LinearLayout)findViewById(R.id.llLoading_splash);

        SQLiteDatabase dbb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = dbb.query(MyDatabase.SETTINGS_TABLE,new String[]{MyDatabase.FIRST_RUN},null,null,null,null,null,null);
        cursor.moveToFirst();
        firstRun = cursor.getInt(0);
        cursor.close();
        dbb.close();

        if (firstRun == 1){
            SettingsActivity sa = new SettingsActivity();
            sa.updateMenu(this,ll_loading_splash);
            SQLiteDatabase db2 = new MyDatabase(this).getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(MyDatabase.FIRST_RUN,0);
            db2.update(MyDatabase.SETTINGS_TABLE,cv,MyDatabase.ID + " = ?",new String[]{"1"});
            db2.close();
        }



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


//        ImageView iv = (ImageView)findViewById(R.id.imgLogo);
//        ViewGroup.LayoutParams lp = iv.getLayoutParams();
//        lp.width = width/5;
//        lp.height = width/5;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, OrdersMenuActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                // close this activity
                finish();
            }
        }, 2000);
    }
}