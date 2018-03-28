package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    EditText etIPFirstRun1, etIPFirstRun2, etIPFirstRun3, etIPFirstRun4, etTitleFirstRun;
    LinearLayout linearLayout, llLoadingSplash;
    TableRow trMainSplash;
    TextView button;
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

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//
//        Toast.makeText(this, height+"-"+width, Toast.LENGTH_SHORT).show();

        SQLiteDatabase db = new MyDatabase(this).getWritableDatabase();
        db.delete(MyDatabase.ORDERS_TABLE, null, null);
        db.close();

        FoodOrdersAdapter.mList.clear();

        etIPFirstRun1 = (EditText) findViewById(R.id.editText_ip_firstrun1);
        etIPFirstRun2 = (EditText) findViewById(R.id.editText_ip_firstrun2);
        etIPFirstRun3 = (EditText) findViewById(R.id.editText_ip_firstrun3);
        etIPFirstRun4 = (EditText) findViewById(R.id.editText_ip_firstrun4);
        etTitleFirstRun = (EditText) findViewById(R.id.editText_title_firstrun);
        linearLayout = (LinearLayout) findViewById(R.id.ll_settings_splash);
        llLoadingSplash = (LinearLayout) findViewById(R.id.llLoading_splash);
        button = (TextView) findViewById(R.id.button_save_firstRun);
        trMainSplash = (TableRow) findViewById(R.id.logo_splash);

        etIPFirstRun1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun1.setText(255 + "");
                    etIPFirstRun1.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIPFirstRun2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun2.setText(255 + "");
                    etIPFirstRun2.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIPFirstRun3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun3.setText(255 + "");
                    etIPFirstRun3.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIPFirstRun4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun4.setText(255 + "");
                    etIPFirstRun4.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
                    Intent i = new Intent(SplashActivity.this, SettingsActivity.class);
                    i.putExtra("status","fromSplash");
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