package co.sansystem.orderingapp;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-06-16.
 */

public class SettingsActivity extends MainActivity {


    TextView bt_save,bt_logout;
    EditText et_title;
    EditText etIP1, etIP2, etIP3, etIP4;
    String ip;
    CheckBox checkBox;

    public static String json = null, json2 = null;
    public static JSONArray jsonArray, jsonArray2;
    public static long id, id2;
    public static boolean isUpdated;
    public boolean isSettingsUpdate = false;

    public SettingsActivity() {
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,OrdersMenuActivity.class));
        finish();
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
                startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
                finish();
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
        }catch (Exception e){

        }

        et_title = (EditText) findViewById(R.id.editText_title);
        et_title.setText(title);

        final int[] u = new int[1];

        fab.setImageResource(R.drawable.save);
        bt_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AsyncTask at = new AsyncTask() {
                    @Override
                    protected void onPreExecute() {
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebService cws = new CallWebService(context, "GetGroupFood", "test");
                json = cws.Call("test");

                CallWebService cws2 = new CallWebService(context, "GetFood", "test");
                json2 = cws2.Call("test");

                try {
                    jsonArray = new JSONArray(json);
                    jsonArray2 = new JSONArray(json2);

                    SQLiteDatabase db = new MyDatabase(context).getWritableDatabase();
                    db.delete(MyDatabase.FOOD_CATEGORY_TABLE, null, null);
                    db.delete(MyDatabase.FOOD_TABLE, null, null);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.CODE, jsonObject.get("ID_Group") + "");
                        cv.put(MyDatabase.NAME, jsonObject.get("NameGroup") + "");
                        cv.put(MyDatabase.IMAGE, Base64.decode(jsonObject.get("ImageGroup") + "", Base64.DEFAULT));
                        id = db.insert(MyDatabase.FOOD_CATEGORY_TABLE, null, cv);
                    }
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(MyDatabase.CODE, jsonObject2.get("ID_Kala") + "");
                        cv.put(MyDatabase.NAME, jsonObject2.get("Name_Kala") + "");
                        cv.put(MyDatabase.IMAGE, Base64.decode(jsonObject2.get("Picture") + "", Base64.DEFAULT));
                        cv.put(MyDatabase.CATEGORY_CODE, jsonObject2.get("Fk_GroupKala") + "");
                        cv.put(MyDatabase.PRICE, jsonObject2.get("GheymatForoshAsli") + "");
                        id2 = db.insert(MyDatabase.FOOD_TABLE, null, cv);
                    }
                    db.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((id != -1) && (id2 != -1)) {
                            Toast.makeText(context, "به روزرسانی با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                            isUpdated = true;
                            if (isSettingsUpdate) {
                                Intent i = new Intent(SettingsActivity.this, OrdersMenuActivity.class);
                                startActivity(i);
                                SettingsActivity.this.finish();
                            }
                        } else {
                            Toast.makeText(context, "خطا در بروزرسانی.", Toast.LENGTH_SHORT).show();
                            isUpdated = false;
                        }
                        if (ll != null) {
                            ll.setVisibility(View.GONE);
                        }

                    }
                });


            }
        }).start();
    }
}

