package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.gson.Gson;
import com.sansystem.orderingapp.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Adapters.ResponcesListAdapter;
import co.sansystem.orderingapp.Models.ContactModel;
import co.sansystem.orderingapp.Models.FavoriteModel;
import co.sansystem.orderingapp.Models.FoodModel;
import co.sansystem.orderingapp.Models.GroupFoodModel;
import co.sansystem.orderingapp.Models.SettingModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 2017-06-16.
 */

public class SettingsActivity extends MainActivity {


    private TextView btSave;
    private TextView btConnect;
    private TextView btOk;
    private TextView btUpdate;
    private EditText et_title;
    private EditText etIP1;
    private EditText etIP2;
    private EditText etIP3;
    private EditText etIP4;
    private String ip;
    private String status = null;
    private CheckBox checkBox;
    private int vaziatSefaresh;

    public static String json = null, json2 = null;
    public static JSONArray jsonArray, jsonArray2;
    //    public static long id, id2;
    private static boolean isUpdated;
    private boolean isSettingsUpdate = false;
    private Spinner spCostumerCode;
    private Spinner spVaziatSefaresh;
    private Intent intent;
    private WebService mTService;
    private WebService mTService2;
    private WebService mTService3;
    private WebService mTService4;
    private AppPreferenceTools appPreferenceTools;
    private String costumerCode;

    private ImageView ivBack;

    public SettingsActivity() {
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrdersMenuActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_layout);

        ivBack = findViewById(R.id.imageView_nav_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btSave = findViewById(R.id.textView_save);
        btConnect = findViewById(R.id.textView_connect);
        btOk = findViewById(R.id.textView_ok);
        btUpdate = findViewById(R.id.textView_update);

        appPreferenceTools = new AppPreferenceTools(this);

        SQLiteDatabase db2 = new MyDatabase(this).getReadableDatabase();
        Cursor cursor2 = db2.query(MyDatabase.SETTINGS_TABLE, new String[]{MyDatabase.IP}, null, null, null, null, null);
        cursor2.moveToFirst();
        appPreferenceTools.saveDomainName(cursor2.getString(0));
        cursor2.close();
        db2.close();

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        WebProvider provider2 = new WebProvider();
        mTService2 = provider2.getTService();

        WebProvider provider3 = new WebProvider();
        mTService3 = provider3.getTService();

        WebProvider provider4 = new WebProvider();
        mTService4 = provider4.getTService();


        if (getIntent().getExtras() != null) {
            intent = getIntent();
            status = intent.getExtras().getString("status");
        }

        if (status != null && status.equals("fromSplash")) {
        } else {
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        spCostumerCode = findViewById(R.id.spinner_default_costumer);
        spVaziatSefaresh = findViewById(R.id.spinner_vaziat_sefaresh);

        checkBox = findViewById(R.id.checkbox_print);
        final AppPreferenceTools appPreferenceTools = new AppPreferenceTools(this);
        checkBox.setChecked(appPreferenceTools.getprintAfterConfirm());

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        etIP1 = findViewById(R.id.editText_ip_1);
        etIP2 = findViewById(R.id.editText_ip_2);
        etIP3 = findViewById(R.id.editText_ip_3);
        etIP4 = findViewById(R.id.editText_ip_4);

        ArrayAdapter<String> adapterVaziatSefaresh =
                new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.vaziat_sefaresh));
        spVaziatSefaresh.setAdapter(adapterVaziatSefaresh);

        spVaziatSefaresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vaziatSefaresh = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vaziatSefaresh = 0;
            }
        });

        if (!appPreferenceTools.getVaziatSefaresh().equals("")) {
            for (int i = 0; i < getResources().getStringArray(R.array.vaziat_sefaresh).length; i++) {
                if (getResources().getStringArray(R.array.vaziat_sefaresh)[i].equals(appPreferenceTools.getVaziatSefaresh())) {
                    spVaziatSefaresh.setSelection(i);
                }
            }
        } else {
            spVaziatSefaresh.setSelection(0);
        }

        if (SplashActivity.tourNumber == 5) {
            SplashActivity.tourNumber++;
            TapTargetView.showFor(SettingsActivity.this,                 // `this` is an Activity
                    TapTarget.forView(btUpdate, "به روز رسانی لیست غذایی", "با لمس اینجا لیست غذایی با سرور همگام می شود.")
                            .textTypeface(Typeface.createFromAsset(
                                    getAssets(),
                                    "fonts/IRANSansWeb_Bold.ttf"))
                            // All options below are optional
                            .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                            .titleTextSize(25)                  // Specify the size (in sp) of the title text
                            .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                            .textColor(R.color.accent)            // Specify a color for both the title and description text
                            .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(false)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            onBackPressed();
                        }
                    });
        }


        Call<List<ContactModel>> call = mTService.getListContact();
        call.enqueue(new Callback<List<ContactModel>>() {
            @Override
            public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {

                if (response.isSuccessful()) {

                    SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                    db.delete(MyDatabase.CONTACTS_INFORMATION, null, null);

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MyDatabase.LIST_CONTACT_JSON, json);
                    db.insert(MyDatabase.CONTACTS_INFORMATION, null, contentValues);
                    db.close();

                    final ArrayList<String> costumerNames = new ArrayList<>();
                    final ArrayList<String> costumerCodes = new ArrayList<>();

                    for (ContactModel contactModel :
                            response.body()) {
                        costumerNames.add(contactModel.getFullName());
                        costumerCodes.add(contactModel.getTafziliID());
                    }


                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_item, costumerNames.toArray(new String[costumerNames.size()]));
                    spCostumerCode.setAdapter(adapter);


                    spCostumerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            costumerCode = costumerCodes.get(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            costumerCode = costumerCodes.get(0);
                        }
                    });

                    for (int i = 0; i < costumerNames.size(); i++) {
                        if (appPreferenceTools.getDefaultCostumerCode().equals(costumerCodes.get(i))) {
                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    spCostumerCode.setSelection(finalI);
                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<ContactModel>> call, Throwable t) {

            }
        });

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

        SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getReadableDatabase();
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

        et_title = findViewById(R.id.editText_title);
        et_title.setText(title);

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                ContentValues cv = new ContentValues();
                String ip = etIP1.getText().toString().trim() + "." +
                        etIP2.getText().toString().trim() + "." +
                        etIP3.getText().toString().trim() + "." +
                        etIP4.getText().toString().trim();
                cv.put(MyDatabase.IP, ip);
                db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                db.close();

                startActivity(getIntent());
                finish();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(MyDatabase.TITLE, et_title.getText().toString());
                db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                db.close();

                Toast.makeText(SettingsActivity.this, "انجام شد.", Toast.LENGTH_SHORT).show();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isSettingsUpdate = true;
                updateMenu(SettingsActivity.this);
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPreferenceTools.printAfterConfirm(checkBox.isChecked());
                appPreferenceTools.saveVaziateSefaresh(getResources().getStringArray(R.array.vaziat_sefaresh)[vaziatSefaresh]);
                appPreferenceTools.saveDefaultCostumerCode(costumerCode);
                Toast.makeText(SettingsActivity.this, "انجام شد.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvResponces = findViewById(R.id.textView_responces);
        tvResponces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbResponce = new MyDatabase(SettingsActivity.this).getReadableDatabase();
                Cursor cursorResponce = dbResponce.query(MyDatabase.RESPONCES_TABLE, new String[]{MyDatabase.ID, MyDatabase.RESPONCE}, null, null, null, null, null, null);
                ArrayList<String> responces = new ArrayList<>();
                if (cursorResponce.moveToFirst()) {
                    do {
                        responces.add(cursorResponce.getInt(0) + " --- " + cursorResponce.getString(1));
                    } while (cursorResponce.moveToNext());
                } else {
                    responces = null;
                }

                if (responces != null) {
                    final RecyclerView rvResponces = findViewById(R.id.responces_recyclerView);

                    ivTitlebar.setVisibility(View.VISIBLE);
                    ivTitlebar.setImageResource(R.drawable.image_delete);
                    ivTitlebar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SQLiteDatabase dbbb = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                            dbbb.delete(MyDatabase.RESPONCES_TABLE, null, null);
                            dbbb.close();
                            ivTitlebar.setVisibility(View.GONE);
                            rvResponces.setVisibility(View.GONE);

                        }
                    });
                    rvResponces.setVisibility(View.VISIBLE);
                    rvResponces.setHasFixedSize(true);
                    RecyclerView.LayoutManager rvlm = new LinearLayoutManager(SettingsActivity.this);
                    rvResponces.setLayoutManager(rvlm);
                    RecyclerView.Adapter rvAdapter = new ResponcesListAdapter(SettingsActivity.this, responces);
                    rvResponces.setAdapter(rvAdapter);
                    rvResponces.scrollToPosition(rvResponces.getAdapter().getItemCount() - 1);
                } else {
                    Toast.makeText(SettingsActivity.this, "تاریخچه ای موجود نیست.", Toast.LENGTH_SHORT).show();
                }
                cursorResponce.close();
                dbResponce.close();
            }
        });


    }

    private void updateMenu(final Context context) {


        id = -1;
        id2 = -1;
        final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(this);
        loadingDialogClass.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<List<GroupFoodModel>> call = mTService.getGroupFood();
                call.enqueue(new Callback<List<GroupFoodModel>>() {
                    @Override
                    public void onResponse(Call<List<GroupFoodModel>> call, Response<List<GroupFoodModel>> response) {

                        if (response.isSuccessful()) {

                            SQLiteDatabase db = new MyDatabase(context).getWritableDatabase();
                            db.delete(MyDatabase.FOOD_CATEGORY_TABLE, null, null);

                            for (GroupFoodModel groupFoodModel :
                                    response.body()) {
                                ContentValues cv = new ContentValues();
                                cv.put(MyDatabase.CODE, groupFoodModel.getIDGroup());
                                cv.put(MyDatabase.NAME, groupFoodModel.getNameGroup());
                                cv.put(MyDatabase.IMAGE, Base64.decode(groupFoodModel.getImageGroup(), Base64.DEFAULT));
                                db.insert(MyDatabase.FOOD_CATEGORY_TABLE, null, cv);
                            }

                            Call<List<FoodModel>> call2 = mTService2.getFood();
                            call2.enqueue(new Callback<List<FoodModel>>() {
                                @Override
                                public void onResponse(Call<List<FoodModel>> call2, Response<List<FoodModel>> response2) {

                                    if (response2.isSuccessful()) {


                                        SQLiteDatabase db = new MyDatabase(context).getWritableDatabase();
                                        db.delete(MyDatabase.FOOD_TABLE, null, null);

                                        for (FoodModel foodModel :
                                                response2.body()) {

                                            ContentValues cv = new ContentValues();
                                            cv.put(MyDatabase.CODE, foodModel.getIDKala());
                                            cv.put(MyDatabase.NAME, foodModel.getNameKala());
                                            try {
                                                cv.put(MyDatabase.IMAGE, Base64.decode(foodModel.getPicture(), Base64.DEFAULT));
                                            } catch (Exception e) {

                                            }
                                            cv.put(MyDatabase.CATEGORY_CODE, foodModel.getFkGroupKala());
                                            cv.put(MyDatabase.PRICE, foodModel.getGheymatForoshAsli());
                                            id2 = db.insert(MyDatabase.FOOD_TABLE, null, cv);
                                        }


                                        db.close();

                                        Call<List<FavoriteModel>> call = mTService3.getFoodFavorite();
                                        call.enqueue(new Callback<List<FavoriteModel>>() {

                                            @Override
                                            public void onResponse
                                                    (Call<List<FavoriteModel>> call, Response<List<FavoriteModel>> response) {

                                                if (response.isSuccessful()) {

                                                    SQLiteDatabase dbFavorite = new MyDatabase(SettingsActivity.this).getWritableDatabase();

//                            dbFavorite.execSQL("UPDATE " + MyDatabase.FOOD_TABLE + " SET " + MyDatabase.FAVORITE + " = 0 ;" +
//                                    " UPDATE " + MyDatabase.FOOD_TABLE + " SET " + MyDatabase.FAVORITE + " = 1 WHERE " + MyDatabase.CODE + " IN (1,2,3,4)");

                                                    dbFavorite.execSQL("UPDATE " + MyDatabase.FOOD_TABLE + " SET " + MyDatabase.FAVORITE + " = '0' ;");

                                                    for (FavoriteModel favoriteModel :
                                                            response.body()) {
                                                        dbFavorite.execSQL(" UPDATE " + MyDatabase.FOOD_TABLE + " SET " + MyDatabase.FAVORITE + " = '1' WHERE " + MyDatabase.CODE + " = " + favoriteModel.getIDKala());
                                                    }


                                                    dbFavorite.close();

                                                    NavigationAdapter.refreshFavorites();

                                                    Call<List<SettingModel>> call22 = mTService4.getSettingDarsad();
                                                    call22.enqueue(new Callback<List<SettingModel>>() {

                                                        @Override
                                                        public void onResponse(Call<List<SettingModel>> call, Response<List<SettingModel>> response) {

                                                            if (response.isSuccessful()) {
                                                                appPreferenceTools.saveSettings(response.body().get(0).getSettingDardadTakhfif(), response.body().get(0).getSettingMablaghTakhfif()
                                                                        , response.body().get(0).getSettingDarsadService(), response.body().get(0).getSettingMablaghService(), response.body().get(0).getSettingDarsadMaliyat());

                                                                Toast.makeText(context, "به روزرسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                                                                isUpdated = true;

                                                                SQLiteDatabase db2 = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                                                                ContentValues cv2 = new ContentValues();
                                                                cv2.put(MyDatabase.FIRST_RUN, 0);
                                                                db2.update(MyDatabase.SETTINGS_TABLE, cv2, MyDatabase.ID + " = ?", new String[]{"1"});
                                                                db2.close();


                                                                loadingDialogClass.dismiss();

                                                                if (isSettingsUpdate) {
                                                                    Intent i2 = new Intent(SettingsActivity.this, OrdersMenuActivity.class);
                                                                    startActivity(i2);
                                                                    SettingsActivity.this.finish();
                                                                }

                                                                if (status != null && status.equals("fromSplash")) {

                                                                    Intent ii = new Intent(SettingsActivity.this, LoginActivity.class);
                                                                    startActivity(ii);
                                                                    SettingsActivity.this.finish();
                                                                }
                                                            } else {
                                                                loadingDialogClass.dismiss();
                                                            }
                                                            loadingDialogClass.dismiss();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<List<SettingModel>> call, Throwable t) {

                                                            loadingDialogClass.dismiss();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<FavoriteModel>> call, Throwable t) {
                                            }
                                        });


                                    } else

                                    {
                                        Toast.makeText(SettingsActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<FoodModel>> call, Throwable t) {

                                }
                            });
                        } else {
                            loadingDialogClass.dismiss();
                            Toast.makeText(SettingsActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<GroupFoodModel>> call, Throwable t) {

                        loadingDialogClass.dismiss();

                    }
                });
            }
        }, 1234);


    }
}

