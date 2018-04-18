package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.sansystem.orderingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.sansystem.orderingapp.Adapters.OfflineFactorItemTouchHelperCallback;
import co.sansystem.orderingapp.Adapters.OfflineFactorsAdapter;
import co.sansystem.orderingapp.Models.FactorContentModel;
import co.sansystem.orderingapp.Models.FactorModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2018-03-17.
 */

public class OfflineFactorsActivity extends AppCompatActivity {

    RecyclerView rvOfflineFactors;
    OfflineFactorsAdapter offlineFactorsAdapter;
    ImageView ivBack, ivAll;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;
    WebService mTService, mTService2;
    List<FactorModel> offlineFactors = null;
    List<String> offlineFactorsIDs = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.offline_factors_layout);

        ivAll = (ImageView) findViewById(R.id.imageView_update_all);
        ivBack = (ImageView) findViewById(R.id.imageView_nav_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvOfflineFactors = (RecyclerView) findViewById(R.id.offline_factors_recyclerView);
        rvOfflineFactors.setHasFixedSize(true);
        rvOfflineFactors.setNestedScrollingEnabled(false);
        rvOfflineFactors.setLayoutManager(new LinearLayoutManager(this));
        offlineFactorsAdapter = new OfflineFactorsAdapter();
        rvOfflineFactors.setAdapter(offlineFactorsAdapter);

        mCallback = new OfflineFactorItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(rvOfflineFactors);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        WebProvider provider2 = new WebProvider();
        mTService2 = provider2.getTService();

        final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(this);
        loadingDialogClass.show();

        SQLiteDatabase db = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.OFFLINE_FACTORS_TABLE, new String[]{MyDatabase.FACTOR_JSON, MyDatabase.ID}, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            offlineFactors = new ArrayList<>();
            offlineFactorsIDs = new ArrayList<>();
            do {
                Gson gson = new Gson();
                String json = cursor.getString(0);
                List<FactorContentModel> factorContentModelList = gson.fromJson(json, new TypeToken<List<FactorContentModel>>() {
                }.getType());
                FactorModel factorModel = new FactorModel();
                factorModel.setList(factorContentModelList);
                offlineFactors.add(factorModel);

                offlineFactorsIDs.add(cursor.getString(1));
            } while (cursor.moveToNext());

            offlineFactorsAdapter.updateAdapterData(offlineFactors, offlineFactorsIDs);
            offlineFactorsAdapter.notifyDataSetChanged();
            loadingDialogClass.dismiss();
        } else {
            Toast.makeText(this, "فیش آفلاین موجود نیست.", Toast.LENGTH_SHORT).show();
            finish();
        }



        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(OfflineFactorsActivity.this);
                loadingDialogClass.show();

                SQLiteDatabase db = new MyDatabase(OfflineFactorsActivity.this).getReadableDatabase();
                Cursor cursor = db.query(MyDatabase.OFFLINE_FACTORS_TABLE, new String[]{MyDatabase.FACTOR_JSON, MyDatabase.ID}, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    offlineFactors = new ArrayList<>();
                    offlineFactorsIDs = new ArrayList<>();
                    do {
                        Gson gson = new Gson();
                        String json = cursor.getString(0);
                        List<FactorContentModel> factorContentModelList = gson.fromJson(json, new TypeToken<List<FactorContentModel>>() {
                        }.getType());
                        FactorModel factorModel = new FactorModel();
                        factorModel.setList(factorContentModelList);
                        offlineFactors.add(factorModel);

                        offlineFactorsIDs.add(cursor.getString(1));
                    } while (cursor.moveToNext());
                }

                int i = 0;
                for (final FactorModel factorModel : offlineFactors) {

                    final Handler handler = new Handler();
                    final int finalI = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Call<Object> call = mTService.saveFactor(factorModel.getList(), "0", "0");
                            call.enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {

                                    if (response.isSuccessful()) {

                                        if (!response.body().equals("NotActive")) {
                                            SQLiteDatabase database = new MyDatabase(OfflineFactorsActivity.this).getWritableDatabase();
                                            database.delete(MyDatabase.OFFLINE_FACTORS_TABLE, MyDatabase.ID + " = " + offlineFactorsIDs.get(finalI), null);
                                            database.close();

//                                            offlineFactors.remove(finalI);
//                                            offlineFactorsIDs.remove(finalI);
//                                            offlineFactorsAdapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(OfflineFactorsActivity.this, "خطا در قفل سخت افزاری", Toast.LENGTH_SHORT).show();
                                        }
                                        Gson gson = new Gson();
                                        String json = gson.toJson(factorModel.getList());

                                    } else {
                                        SQLiteDatabase db2 = new MyDatabase(OfflineFactorsActivity.this).getWritableDatabase();
                                        ContentValues cv2 = new ContentValues();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        String myDate = format.format(new java.util.Date());
                                        cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response.message());
                                        db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                                        db2.close();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    SQLiteDatabase db2 = new MyDatabase(OfflineFactorsActivity.this).getWritableDatabase();
                                    ContentValues cv2 = new ContentValues();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                    String myDate = format.format(new Date());
                                    cv2.put(MyDatabase.RESPONCE, myDate + " --> " + t.getMessage());
                                    db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                                    db2.close();
                                }
                            });
                        }
                    }, 1234);
                    i++;
                }
                loadingDialogClass.dismiss();
                startActivity(getIntent());
                finish();
            }
        });

    }
}
