package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.sansystem.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Adapters.OfflineFactorItemTouchHelperCallback;
import co.sansystem.orderingapp.Adapters.OfflineFactorsAdapter;
import co.sansystem.orderingapp.Models.FactorContentModel;
import co.sansystem.orderingapp.Models.FactorModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2018-03-17.
 */

public class OfflineFactorsActivity extends AppCompatActivity {

    RecyclerView rvOfflineFactors;
    OfflineFactorsAdapter offlineFactorsAdapter;
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
        setContentView(R.layout.offline_factors_layout);

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
        WebService mTService = provider.getTService();

        final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(this);
        loadingDialogClass.show();

        SQLiteDatabase db = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.OFFLINE_FACTORS_TABLE,new String[]{MyDatabase.FACTOR_JSON,MyDatabase.ID},null,null,null,null,null,null);
        List<FactorModel> offlineFactors = null;
        List<String> offlineFactorsIDs = null;
        if(cursor.moveToFirst()){
            offlineFactors = new ArrayList<>();
            offlineFactorsIDs = new ArrayList<>();
            do{
                Gson gson = new Gson();
                String json = cursor.getString(0);
                List<FactorContentModel> factorContentModelList = gson.fromJson(json, new TypeToken<List<FactorContentModel>>(){}.getType());
                FactorModel factorModel = new FactorModel();
                factorModel.setList(factorContentModelList);
                offlineFactors.add(factorModel);

                offlineFactorsIDs.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }



        offlineFactorsAdapter.updateAdapterData(offlineFactors,offlineFactorsIDs);
        offlineFactorsAdapter.notifyDataSetChanged();
        loadingDialogClass.dismiss();
    }
}
