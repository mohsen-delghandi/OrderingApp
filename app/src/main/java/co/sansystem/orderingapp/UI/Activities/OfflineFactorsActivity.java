package co.sansystem.orderingapp.UI.Activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sansystem.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Adapters.OfflineFactorsAdapter;
import co.sansystem.orderingapp.Models.FactorContentModel;
import co.sansystem.orderingapp.Models.FactorModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;

/**
 * Created by Mohsen on 2018-03-17.
 */

public class OfflineFactorsActivity extends MainActivity {

    RecyclerView rvOfflineFactors;
    OfflineFactorsAdapter offlineFactorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.offline_factors_layout);

        rvOfflineFactors = (RecyclerView) findViewById(R.id.offline_factors_recyclerView);
        rvOfflineFactors.setHasFixedSize(true);
        rvOfflineFactors.setNestedScrollingEnabled(false);
        rvOfflineFactors.setLayoutManager(new LinearLayoutManager(this));
        offlineFactorsAdapter = new OfflineFactorsAdapter();
        rvOfflineFactors.setAdapter(offlineFactorsAdapter);

        WebProvider provider = new WebProvider();
        WebService mTService = provider.getTService();

        tvTitlebar.setText(title + " - " + "فیش های آفلاین");
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icon_back);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

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
