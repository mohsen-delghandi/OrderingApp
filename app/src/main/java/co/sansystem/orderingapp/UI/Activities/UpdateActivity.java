package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.orderingapp.R;

import java.util.List;

import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Models.FavoriteModel;
import co.sansystem.orderingapp.Models.FoodModel;
import co.sansystem.orderingapp.Models.GroupFoodModel;
import co.sansystem.orderingapp.Models.SettingModel;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UpdateActivity extends AppCompatActivity {

    private WebService mTService5;
    private WebService mTService2;
    private WebService mTService3;
    private WebService mTService4;
    private AppPreferenceTools appPreferenceTools;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private TextView tvUpdate;
    private TextView tvOk;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.update_layout);

        progressBar = findViewById(R.id.progressBar_update);
        progressBar2 = findViewById(R.id.progressBar2);
        tvUpdate = findViewById(R.id.textView_update);
        tvOk = findViewById(R.id.textView_update_ok);

        WebProvider provider = new WebProvider();
        mTService5 = provider.getTService();

        WebProvider provider2 = new WebProvider();
        mTService2 = provider2.getTService();

        WebProvider provider3 = new WebProvider();
        mTService3 = provider3.getTService();

        WebProvider provider4 = new WebProvider();
        mTService4 = provider4.getTService();

        appPreferenceTools = new AppPreferenceTools(this);

        blink();


        try {
            progressBar.setProgress(20, true);
        } catch (Error e) {

        }


        updateMenu(this);
    }

    private void blink() {
        tvUpdate.animate().alpha(0).setDuration(1000).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvUpdate.animate().alpha(1).setDuration(1000).start();
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blink();
                    }
                }, 1000);
            }
        }, 1000);
    }

    private void updateMenu(final Context context) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<List<GroupFoodModel>> call = mTService5.getGroupFood();
                call.enqueue(new Callback<List<GroupFoodModel>>() {
                    @Override
                    public void onResponse(Call<List<GroupFoodModel>> call, Response<List<GroupFoodModel>> response) {

                        if (response.isSuccessful()) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // Do something for lollipop and above versions
                                    try {
                                        progressBar.setProgress(40, true);
                                    } catch (Error e) {


                                    }
                                }
                            }, 500);

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
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                // Do something for lollipop and above versions
                                                try {
                                                    progressBar.setProgress(60, true);
                                                } catch (Error e) {

                                                }

                                            }
                                        }, 500);

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
                                            db.insert(MyDatabase.FOOD_TABLE, null, cv);
                                        }


                                        db.close();

                                        Call<List<FavoriteModel>> call = mTService3.getFoodFavorite();
                                        call.enqueue(new Callback<List<FavoriteModel>>() {

                                            @Override
                                            public void onResponse
                                                    (Call<List<FavoriteModel>> call, Response<List<FavoriteModel>> response) {

                                                if (response.isSuccessful()) {
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            // Do something for lollipop and above versions
                                                            try {
                                                                progressBar.setProgress(80, true);
                                                            } catch (Error e) {

                                                            }

                                                        }
                                                    }, 500);
                                                    SQLiteDatabase dbFavorite = new MyDatabase(UpdateActivity.this).getWritableDatabase();

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
                                                                Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        // Do something for lollipop and above versions
                                                                        try {
                                                                            progressBar.setProgress(100, true);
                                                                        } catch (Error e) {

                                                                        }

                                                                        progressBar.setVisibility(View.GONE);
                                                                        progressBar2.setVisibility(View.VISIBLE);
                                                                        tvOk.setVisibility(View.VISIBLE);
                                                                        tvUpdate.setVisibility(View.GONE);
                                                                        Handler handler = new Handler();
                                                                        handler.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                startActivity(new Intent(UpdateActivity.this, OrdersMenuActivity.class));
                                                                                finish();
                                                                            }
                                                                        }, 1500);
                                                                    }
                                                                }, 500);
                                                                appPreferenceTools.saveSettings(response.body().get(0).getSettingDardadTakhfif(), response.body().get(0).getSettingMablaghTakhfif()
                                                                        , response.body().get(0).getSettingDarsadService(), response.body().get(0).getSettingMablaghService(), response.body().get(0).getSettingDarsadMaliyat());

                                                                SQLiteDatabase db2 = new MyDatabase(UpdateActivity.this).getWritableDatabase();
                                                                ContentValues cv2 = new ContentValues();
                                                                cv2.put(MyDatabase.FIRST_RUN, 0);
                                                                db2.update(MyDatabase.SETTINGS_TABLE, cv2, MyDatabase.ID + " = ?", new String[]{"1"});
                                                                db2.close();


//                                                                loadingDialogClass.dismiss();

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<List<SettingModel>> call, Throwable t) {

//                                                            loadingDialogClass.dismiss();
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
                                        Toast.makeText(UpdateActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<FoodModel>> call, Throwable t) {

                                }
                            });
                        } else {
//                            loadingDialogClass.dismiss();
                            Toast.makeText(UpdateActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<GroupFoodModel>> call, Throwable t) {

//                        loadingDialogClass.dismiss();

                    }
                });
            }
        }, 1234);


    }
}
