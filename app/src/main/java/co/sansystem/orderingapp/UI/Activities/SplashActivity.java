package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Models.VersionModel;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
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
    WebService mTService2;
    EditText etIP1, etIP2, etIP3, etIP4;
    private TextView tvUpdate,tvNoUpdate;
    String versionCode,ip;
    LinearLayout llMain,llIp;
    public static int tourNumber;
    TextView tvPermission,btConnect;

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

        tvPermission = (TextView) findViewById(R.id.textView_permission);

        etIP1 = (EditText) findViewById(R.id.editText_ip_1);
        etIP2 = (EditText) findViewById(R.id.editText_ip_2);
        etIP3 = (EditText) findViewById(R.id.editText_ip_3);
        etIP4 = (EditText) findViewById(R.id.editText_ip_4);
        etIP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIP1.setText(255 + "");
                    etIP1.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIP2.setText(255 + "");
                    etIP2.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIP3.setText(255 + "");
                    etIP3.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIP4.setText(255 + "");
                    etIP4.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SQLiteDatabase db = new MyDatabase(SplashActivity.this).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.SETTINGS_TABLE, new String[]{MyDatabase.IP}, null, null, null, null, null);
        cursor.moveToFirst();
        ip = cursor.getString(0);
        cursor.close();
        db.close();

        try {
            String[] ipArray = ip.split("\\.");
            etIP1.setText(ipArray[0]);
            etIP2.setText(ipArray[1]);
            etIP3.setText(ipArray[2]);
            etIP4.setText(ipArray[3]);
        } catch (Exception e) {

        }


        btConnect = (TextView) findViewById(R.id.textView_connect);
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = new MyDatabase(SplashActivity.this).getWritableDatabase();
                ContentValues cv = new ContentValues();
                String ip = etIP1.getText().toString().trim() + "." +
                        etIP2.getText().toString().trim() + "." +
                        etIP3.getText().toString().trim() + "." +
                        etIP4.getText().toString().trim();
                cv.put(MyDatabase.IP, ip);
                db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                db.close();

                startActivity(new Intent(SplashActivity.this,SplashActivity.class));
                finish();
            }
        });

        tourNumber = 1;

        llMain = (LinearLayout) findViewById(R.id.linearLayout_main);
        llIp = (LinearLayout) findViewById(R.id.linearLayout_ip);

        tvUpdate = (TextView) findViewById(R.id.textView_bt_update);
        tvNoUpdate = (TextView) findViewById(R.id.textView_bt_no_update);

        WebProviderOnline webProviderOnline = new WebProviderOnline();
        mTService = webProviderOnline.getTService();

        WebProvider webProvider = new WebProvider();
        mTService2 = webProvider.getTService();

        SQLiteDatabase db2 = new MyDatabase(this).getWritableDatabase();
        db2.delete(MyDatabase.ORDERS_TABLE, null, null);
        db2.close();

        FoodOrdersAdapter.mList.clear();

        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkVersion(versionCode);

//        SQLiteDatabase dbb = new MyDatabase(SplashActivity.this).getReadableDatabase();
//        Cursor cursor = dbb.query(MyDatabase.SETTINGS_TABLE, new String[]{MyDatabase.FIRST_RUN}, null, null, null, null, null, null);
//        cursor.moveToFirst();
//        firstRun = cursor.getInt(0);
//        cursor.close();
//        dbb.close();
//
//        if (firstRun == 1) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i = new Intent(SplashActivity.this, IpSetActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    finish();
//                }
//            }, 2000);
//        } else {
//
//            Call<Boolean> call2 = mTService2.deviceRegister(Secure.getString(getContentResolver(), Secure.ANDROID_ID), Build.MANUFACTURER + "-" + Build.MODEL);
//            call2.enqueue(new Callback<Boolean>() {
//
//                @Override
//                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//
//                    if (response.isSuccessful()) {
//                        if (response.body()) {
//
//                            AppPreferenceTools appPreferenceTools = new AppPreferenceTools(SplashActivity.this);
//                            if (appPreferenceTools.isAuthorized()) {
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent i = new Intent(SplashActivity.this, OrdersMenuActivity.class);
//                                        startActivity(i);
//                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                        finish();
//                                    }
//                                }, 2000);
//                            } else {
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                                        startActivity(i);
//                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                        finish();
//                                    }
//                                }, 2000);
//                            }
//
//                        } else {
//                            tvPermission.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        Toast.makeText(SplashActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Boolean> call, Throwable t) {
//                    Toast.makeText(SplashActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            });

//        }
    }

    private void checkVersion(final String versionCode) {
        Call<VersionModel> call = mTService.checkVersion(versionCode);
        call.enqueue(new Callback<VersionModel>() {
            @Override
            public void onResponse(Call<VersionModel> call, final Response<VersionModel> response) {

                if(response.isSuccessful()) {

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
                                Call<Boolean> call2 = mTService2.deviceRegister(Secure.getString(getContentResolver(), Secure.ANDROID_ID), Build.MANUFACTURER + "-" + Build.MODEL);
                                call2.enqueue(new Callback<Boolean>() {

                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                                        if (response.isSuccessful()) {
                                            if (response.body()) {

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

                                            } else {
                                                tvPermission.setVisibility(View.VISIBLE);
                                                llIp.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            tvPermission.setVisibility(View.VISIBLE);
                                            tvPermission.setText("عدم ارتباط با سرور.");
                                            llIp.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        tvPermission.setVisibility(View.VISIBLE);
                                        tvPermission.setText("عدم ارتباط با سرور.");
                                        llIp.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } else {
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
                                        Call<Boolean> call2 = mTService2.deviceRegister(Secure.getString(getContentResolver(), Secure.ANDROID_ID), Build.MANUFACTURER + "-" + Build.MODEL);
                                        call2.enqueue(new Callback<Boolean>() {

                                            @Override
                                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                                                if (response.isSuccessful()) {
                                                    if (response.body()) {

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

                                                    } else {
                                                        tvPermission.setVisibility(View.VISIBLE);
                                                        llIp.setVisibility(View.VISIBLE);
                                                    }
                                                } else {
                                                    tvPermission.setVisibility(View.VISIBLE);
                                                    tvPermission.setText("عدم ارتباط با سرور.");
                                                    llIp.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Boolean> call, Throwable t) {
                                                tvPermission.setVisibility(View.VISIBLE);
                                                tvPermission.setText("عدم ارتباط با سرور.");
                                                llIp.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {
                        llMain.setVisibility(View.VISIBLE);
                        tvNoUpdate.setVisibility(View.GONE);
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
                }else{
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
                        Call<Boolean> call2 = mTService2.deviceRegister(Secure.getString(getContentResolver(), Secure.ANDROID_ID), Build.MANUFACTURER + "-" + Build.MODEL);
                        call2.enqueue(new Callback<Boolean>() {

                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                                if (response.isSuccessful()) {
                                    if (response.body()) {

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

                                    } else {
                                        tvPermission.setVisibility(View.VISIBLE);
                                        llIp.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    tvPermission.setVisibility(View.VISIBLE);
                                    tvPermission.setText("عدم ارتباط با سرور.");
                                    llIp.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                tvPermission.setVisibility(View.VISIBLE);
                                tvPermission.setText("عدم ارتباط با سرور.");
                                llIp.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<VersionModel> call, Throwable t) {
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
                    Call<Boolean> call2 = mTService2.deviceRegister(Secure.getString(getContentResolver(), Secure.ANDROID_ID), Build.MANUFACTURER + "-" + Build.MODEL);
                    call2.enqueue(new Callback<Boolean>() {

                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                            if (response.isSuccessful()) {
                                if (response.body()) {

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

                                } else {
                                    tvPermission.setVisibility(View.VISIBLE);
                                    llIp.setVisibility(View.VISIBLE);
                                }
                            } else {
                                tvPermission.setVisibility(View.VISIBLE);
                                tvPermission.setText("عدم ارتباط با سرور.");
                                llIp.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            tvPermission.setVisibility(View.VISIBLE);
                            tvPermission.setText("عدم ارتباط با سرور.");
                            llIp.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}