package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Adapters.ResponcesListAdapter;
import co.sansystem.orderingapp.Models.ContactModel;
import co.sansystem.orderingapp.Models.FavoriteModel;
import co.sansystem.orderingapp.Models.FoodModel;
import co.sansystem.orderingapp.Models.GroupFoodModel;
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


    TextView bt_save, btSaveIP, bt_logout, tvStatus;
    EditText et_title;
    EditText etIP1, etIP2, etIP3, etIP4;
    String ip, status = null;
    CheckBox checkBox;
    LinearLayout llLoading;

    public static String json = null, json2 = null;
    public static JSONArray jsonArray, jsonArray2;
    //    public static long id, id2;
    public static boolean isUpdated;
    public boolean isSettingsUpdate = false;
    Spinner spCostumerCode;
    Spinner spVaziatSefaresh;
    Intent intent;
    private WebService mTService;
    private WebService mTService2;
    private WebService mTService3;
    AppPreferenceTools appPreferenceTools;

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
        setInflater(this, R.layout.settings_layout);

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

        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        tvStatus = (TextView) findViewById(R.id.textView_status);

        if (getIntent().getExtras() != null) {
            intent = getIntent();
            status = intent.getExtras().getString("status");
        }

        if (status != null && status.equals("fromSplash")) {
            tvTitlebar.setText("تنظیمات اولین ورود به برنامه");
        } else {
            tvTitlebar.setText(title + " - " + "تنظیمات");
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        spCostumerCode = (Spinner) findViewById(R.id.spinner_default_costumer);
        spVaziatSefaresh = (Spinner) findViewById(R.id.spinner_vaziat_sefaresh);

        checkBox = (CheckBox) findViewById(R.id.checkbox_print);
        final AppPreferenceTools appPreferenceTools = new AppPreferenceTools(this);
        checkBox.setChecked(appPreferenceTools.getprintAfterConfirm());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appPreferenceTools.printAfterConfirm(b);
            }
        });

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        bt_save = (TextView) findViewById(R.id.button_save);
        btSaveIP = (TextView) findViewById(R.id.button_save_ip);
        bt_logout = (TextView) findViewById(R.id.button_logout);
        etIP1 = (EditText) findViewById(R.id.editText_ip_1);
        etIP2 = (EditText) findViewById(R.id.editText_ip_2);
        etIP3 = (EditText) findViewById(R.id.editText_ip_3);
        etIP4 = (EditText) findViewById(R.id.editText_ip_4);

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreferenceTools appPreferenceTools = new AppPreferenceTools(SettingsActivity.this);
                appPreferenceTools.removeAllPrefs();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            }
        });
        if (status != null && status.equals("fromSplash")) {
            bt_logout.setVisibility(View.GONE);
        }

        ArrayAdapter<String> adapterVaziatSefaresh =
                new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.vaziat_sefaresh));
        spVaziatSefaresh.setAdapter(adapterVaziatSefaresh);

        spVaziatSefaresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                appPreferenceTools.saveVaziateSefaresh(getResources().getStringArray(R.array.vaziat_sefaresh)[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                appPreferenceTools.saveVaziateSefaresh("بیرون بر");
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

        Call<List<ContactModel>> call = mTService.getListContact();
        call.enqueue(new Callback<List<ContactModel>>() {
            @Override
            public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {

                if (response.isSuccessful()) {
                    final ArrayList<String> costumerNames = new ArrayList<String>();
                    final ArrayList<String> costumerCodes = new ArrayList<String>();

                    for (ContactModel contactModel :
                            response.body()) {
                        costumerNames.add(contactModel.getFullName());
                        costumerCodes.add(contactModel.getTafziliID());
                    }


                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, costumerNames.toArray(new String[costumerNames.size()]));
                    spCostumerCode.setAdapter(adapter);
                    llLoading.setVisibility(View.GONE);


                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText("ارتباط برقرار شد.");
                    tvStatus.setTextColor(getResources().getColor(R.color.green));

                    spCostumerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            appPreferenceTools.saveDefaultCostumerCode(costumerCodes.get(i));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            appPreferenceTools.saveDefaultCostumerCode(costumerCodes.get(0));
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

                } else {
                    llLoading.setVisibility(View.GONE);
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText("ارتباط برقرار نشد.");
                    tvStatus.setTextColor(getResources().getColor(R.color.red));
                }
            }

            @Override
            public void onFailure(Call<List<ContactModel>> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("ارتباط برقرار نشد.");
                tvStatus.setTextColor(getResources().getColor(R.color.red));
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

        et_title = (EditText) findViewById(R.id.editText_title);
        et_title.setText(title);

        final int[] u = new int[1];


        btSaveIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                ContentValues cv = new ContentValues();
                String ip = etIP1.getText().toString().trim() + "." +
                        etIP2.getText().toString().trim() + "." +
                        etIP3.getText().toString().trim() + "." +
                        etIP4.getText().toString().trim();
                cv.put(MyDatabase.IP, ip);
                cv.put(MyDatabase.TITLE, et_title.getText().toString());
                u[0] = db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                db.close();

                startActivity(getIntent());
                finish();
            }
        });


        fab.setImageResource(R.drawable.save);
        bt_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                AsyncTask at = new AsyncTask() {
                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        isSettingsUpdate = true;
                        updateMenu(SettingsActivity.this, ll_loading);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
//                        if (u[0] == 1) {
//                            Toast.makeText(SettingsActivity.this, "عملیات ذخیره با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(SettingsActivity.this, OrdersMenuActivity.class);
//                            startActivity(i);
//                        } else if (u[0] == 0) {
//                            Toast.makeText(SettingsActivity.this, "عملیات ذخیره ناموفق بود.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(SettingsActivity.this, "خطای نامشخص،با پشتیبانی تماس بگیرید.", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }.execute();
            }
        });

        final TableRow trMain = (TableRow) findViewById(R.id.tr_main);
        TextView tvResponces = (TextView) findViewById(R.id.textView_responces);
        tvResponces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db3 = new MyDatabase(SettingsActivity.this).getReadableDatabase();
                Cursor ccc = db3.query(MyDatabase.RESPONCES_TABLE, new String[]{MyDatabase.ID, MyDatabase.RESPONCE}, null, null, null, null, null, null);
                ArrayList<String> responces = new ArrayList<>();
                if (ccc.moveToFirst()) {
                    do {
                        responces.add(ccc.getInt(0) + " --- " + ccc.getString(1));
                    } while (ccc.moveToNext());
                } else {
                    responces = null;
                }

                if (responces != null) {
                    final RecyclerView rvResponces = (RecyclerView) findViewById(R.id.responces_recyclerView);
                    trMain.setVisibility(View.GONE);
                    ivTitlebar.setVisibility(View.VISIBLE);
                    ivTitlebar.setImageResource(R.drawable.delete_icon);
                    ivTitlebar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SQLiteDatabase dbbb = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                            dbbb.delete(MyDatabase.RESPONCES_TABLE, null, null);
                            dbbb.close();
                            ivTitlebar.setVisibility(View.GONE);
                            rvResponces.setVisibility(View.GONE);
                            trMain.setVisibility(View.VISIBLE);
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
            }
        });


    }

    public void updateMenu(final Context context, final LinearLayout ll) {

        if (ll != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll.setVisibility(View.VISIBLE);
                }
            });
        }
        id = -1;
        id2 = -1;


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
                                    cv.put(MyDatabase.IMAGE, Base64.decode(foodModel.getPicture(), Base64.DEFAULT));
                                    cv.put(MyDatabase.CATEGORY_CODE, foodModel.getFkGroupKala());
                                    cv.put(MyDatabase.PRICE, foodModel.getGheymatForoshAsli());
                                    id2 = db.insert(MyDatabase.FOOD_TABLE, null, cv);
                                }


                                db.close();

                                Toast.makeText(context, "به روزرسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();

                                isUpdated = true;

                                SQLiteDatabase db2 = new MyDatabase(SettingsActivity.this).getWritableDatabase();
                                ContentValues cv2 = new ContentValues();
                                cv2.put(MyDatabase.FIRST_RUN, 0);
                                db2.update(MyDatabase.SETTINGS_TABLE, cv2, MyDatabase.ID + " = ?", new String[]{"1"});
                                db2.close();

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
                } else

                {
                    Toast.makeText(SettingsActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupFoodModel>> call, Throwable t) {
            }
        });


//        new AsyncTask<Void, Void, Void>()
//
//        {
//            @Override
//            protected Void doInBackground(Void... voids) {
////                CallWebService cws = new CallWebService(context, "GetGroupFood", "test");
////                json = cws.Call("test");
//
////                CallWebService cws2 = new CallWebService(context, "GetFood", "test");
////                json2 = cws2.Call("test");
//
//                try {
////                    jsonArray = new JSONArray(json);
////                    jsonArray2 = new JSONArray(json2);
//
////                    SQLiteDatabase db = new MyDatabase(context).getWritableDatabase();
////                    db.delete(MyDatabase.FOOD_CATEGORY_TABLE, null, null);
////                    db.delete(MyDatabase.FOOD_TABLE, null, null);
//
////                    deleteRecursive(new File(getBaseContext().getFilesDir().getAbsolutePath() + "/group"));
////                    deleteRecursive(new File(getBaseContext().getFilesDir().getAbsolutePath() + "/kala"));
//
////                    for (int i = 0; i < jsonArray.length(); i++) {
////                        JSONObject jsonObject = jsonArray.getJSONObject(i);
////                        ContentValues cv = new ContentValues();
////                        cv.put(MyDatabase.CODE, jsonObject.get("ID_Group") + "");
////                        cv.put(MyDatabase.NAME, jsonObject.get("NameGroup") + "");
////                        cv.put(MyDatabase.IMAGE, Base64.decode(jsonObject.get("ImageGroup") + "", Base64.DEFAULT));
////                        id = db.insert(MyDatabase.FOOD_CATEGORY_TABLE, null, cv);
////                    }
////                    for (int i = 0; i < jsonArray2.length(); i++) {
////                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
////                        ContentValues cv = new ContentValues();
////                        cv.put(MyDatabase.CODE, jsonObject2.get("ID_Kala") + "");
////                        cv.put(MyDatabase.NAME, jsonObject2.get("Name_Kala") + "");
////                        cv.put(MyDatabase.IMAGE, Base64.decode(jsonObject2.get("Picture") + "", Base64.DEFAULT));
////                        cv.put(MyDatabase.CATEGORY_CODE, jsonObject2.get("Fk_GroupKala") + "");
////                        cv.put(MyDatabase.PRICE, jsonObject2.get("GheymatForoshAsli") + "");
////                        id2 = db.insert(MyDatabase.FOOD_TABLE, null, cv);
////                    }
////                    db.close();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("img", "not ok : " + e.getMessage());
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                if ((id != -1) && (id2 != -1)) {
////                    Toast.makeText(context, "به روزرسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
////                    isUpdated = true;
////
////                    SQLiteDatabase db2 = new MyDatabase(SettingsActivity.this).getWritableDatabase();
////                    ContentValues cv2 = new ContentValues();
////                    cv2.put(MyDatabase.FIRST_RUN, 0);
////                    db2.update(MyDatabase.SETTINGS_TABLE, cv2, MyDatabase.ID + " = ?", new String[]{"1"});
////                    db2.close();
////
////                    if (isSettingsUpdate) {
////                        Intent i = new Intent(SettingsActivity.this, OrdersMenuActivity.class);
////                        startActivity(i);
////                        SettingsActivity.this.finish();
////                    }
////
////                    if (status != null && status.equals("fromSplash")) {
////
////                        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
////                        startActivity(i);
////                        SettingsActivity.this.finish();
////                    }
//
//                } else {
//                    Toast.makeText(context, "خطا در بروزرسانی.", Toast.LENGTH_SHORT).show();
//                    isUpdated = false;
//
//                }
//                if (ll != null) {
//                    ll.setVisibility(View.GONE);
//                }
//            }
//        }.
//
//                execute();
//    }
//
//    public void saveFile(String path, String name, byte[] bytes) {
//        File file = new File(path);
//        try {
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            file = new File(path + "/" + name);
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            bos.write(bytes);
//            bos.flush();
//            bos.close();
//        } catch (Exception e) {
//            Log.e("file save : ", e.getMessage());
//        }
//    }
//
//    void deleteRecursive(File fileOrDirectory) {
//        if (fileOrDirectory.isDirectory())
//            for (File child : fileOrDirectory.listFiles())
//                deleteRecursive(child);
//
//        fileOrDirectory.delete();
//    }
    }
}

