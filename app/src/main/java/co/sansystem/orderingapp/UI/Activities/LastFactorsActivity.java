package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.sansystem.orderingapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.sansystem.orderingapp.Adapters.LastFactorItemTouchHelperCallback;
import co.sansystem.orderingapp.Adapters.LastFactorsAdapter;
import co.sansystem.orderingapp.Models.MiniFactorModel;
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

public class LastFactorsActivity extends AppCompatActivity {

    RecyclerView rvLastFactors;
    LastFactorsAdapter lastFactorsAdapter;
    private WebService mTService;
    ImageView ivBack;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.last_factors_layout);

        ivBack = (ImageView) findViewById(R.id.imageView_nav_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvLastFactors = (RecyclerView) findViewById(R.id.last_factors_recyclerView);
        rvLastFactors.setHasFixedSize(true);
        rvLastFactors.setNestedScrollingEnabled(false);
        rvLastFactors.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        rvLastFactors.setLayoutManager(new LinearLayoutManager(this));
        lastFactorsAdapter = new LastFactorsAdapter();
        rvLastFactors.setAdapter(lastFactorsAdapter);


        mCallback = new LastFactorItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(rvLastFactors);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(this);
        loadingDialogClass.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Call<List<MiniFactorModel>> call = mTService.getLastFactors();
                call.enqueue(new Callback<List<MiniFactorModel>>() {

                    @Override
                    public void onResponse(Call<List<MiniFactorModel>> call, Response<List<MiniFactorModel>> response) {

                        if (response.isSuccessful()) {
                            if (response.body().size() == 0) {
                                Toast.makeText(LastFactorsActivity.this, "تاریخچه ای موجود نیست.", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                lastFactorsAdapter.updateAdapterData(response.body());
                                lastFactorsAdapter.notifyDataSetChanged();
                                loadingDialogClass.dismiss();
                            }
                        } else {
                            SQLiteDatabase db2 = new MyDatabase(LastFactorsActivity.this).getWritableDatabase();
                            ContentValues cv2 = new ContentValues();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            String myDate = format.format(new Date());
                            cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response.message());
                            db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                            db2.close();
                            Toast.makeText(LastFactorsActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                            loadingDialogClass.dismiss();

                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MiniFactorModel>> call, Throwable t) {
                        SQLiteDatabase db2 = new MyDatabase(LastFactorsActivity.this).getWritableDatabase();
                        ContentValues cv2 = new ContentValues();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String myDate = format.format(new Date());
                        cv2.put(MyDatabase.RESPONCE, myDate + " --> " + t.getMessage());
                        db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                        db2.close();
                        Toast.makeText(LastFactorsActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        loadingDialogClass.dismiss();

                        onBackPressed();
                    }
                });
            }
        }, 1234);
    }

}
