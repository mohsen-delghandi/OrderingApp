package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Models.VersionModel;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProviderOnline;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-06-18.
 */

public class SplashActivity extends AppCompatActivity {

    int firstRun;
    WebService mTService;
    private TextView tvUpdate,tvNoUpdate;
    String versionCode;
    LinearLayout llMain;

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

        llMain = (LinearLayout) findViewById(R.id.linearLayout_main);

        tvUpdate = (TextView) findViewById(R.id.textView_bt_update);
        tvNoUpdate = (TextView) findViewById(R.id.textView_bt_no_update);

        WebProviderOnline webProviderOnline = new WebProviderOnline();
        mTService = webProviderOnline.getTService();

        SQLiteDatabase db = new MyDatabase(this).getWritableDatabase();
        db.delete(MyDatabase.ORDERS_TABLE, null, null);
        db.close();

        FoodOrdersAdapter.mList.clear();

        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        checkVersion(versionCode);









        SQLiteDatabase dbb = new MyDatabase(SplashActivity.this).getReadableDatabase();
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

    private void checkVersion(final String versionCode) {
        Call<VersionModel> call = mTService.checkVersion(versionCode);
        call.enqueue(new Callback<VersionModel>() {
            @Override
            public void onResponse(Call<VersionModel> call, final Response<VersionModel> response) {

                if (!response.body().force_dl) {
                    if (!response.body().new_version) {
                        SQLiteDatabase dbb = new MyDatabase(SplashActivity.this).getReadableDatabase();
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
                    }else{
                        llMain.setVisibility(View.VISIBLE);
                        tvUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = response.body().download_optional_url;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                finish();
                            }
                        });
                        tvNoUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
                        });
                    }
                } else {
                    llMain.setVisibility(View.VISIBLE);
                    tvUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = response.body().download_url;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<VersionModel> call, Throwable t) {
                checkVersion(versionCode);
            }
        });
    }
}