package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sansystem.orderingapp.R;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-06-18.
 */

public class SplashActivity extends AppCompatActivity {

    int firstRun;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        SQLiteDatabase db = new MyDatabase(this).getWritableDatabase();
        db.delete(MyDatabase.ORDERS_TABLE, null, null);
        db.close();

        FoodOrdersAdapter.mList.clear();

        SQLiteDatabase dbb = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = dbb.query(MyDatabase.SETTINGS_TABLE, new String[]{MyDatabase.FIRST_RUN}, null, null, null, null, null, null);
        cursor.moveToFirst();
        firstRun = cursor.getInt(0);
        cursor.close();
        dbb.close();

        if (firstRun == 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, IpSetActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }, 2000);
        } else {
            AppPreferenceTools appPreferenceTools = new AppPreferenceTools(SplashActivity.this);
            if (appPreferenceTools.isAuthorized()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, OrdersMenuActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }, 2000);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }, 2000);
            }
        }
    }
}