package co.sansystem.orderingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.sansystem.orderingapp.NavigationAdapter.mContext;

/**
 * Created by Mohsen on 2018-03-17.
 */

public class LastFactorsActivity extends MainActivity {

    RecyclerView rvLastFactors;
    LastFactorsAdapter lastFactorsAdapter;
    private WebService mTService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.last_factors_layout);

        rvLastFactors = (RecyclerView) findViewById(R.id.last_factors_recyclerView);
        rvLastFactors.setHasFixedSize(true);
        rvLastFactors.setNestedScrollingEnabled(false);
        rvLastFactors.setLayoutManager(new LinearLayoutManager(this));
        lastFactorsAdapter = new LastFactorsAdapter();
        rvLastFactors.setAdapter(lastFactorsAdapter);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        Call<List<MiniFactorModel>> call = mTService.getLastFactors();
        call.enqueue(new Callback<List<MiniFactorModel>>() {

            @Override
            public void onResponse(Call<List<MiniFactorModel>> call, Response<List<MiniFactorModel>> response) {

                if (response.isSuccessful()) {
                    lastFactorsAdapter.updateAdapterData(response.body());
                    lastFactorsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LastFactorsActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MiniFactorModel>> call, Throwable t) {
                Toast.makeText(LastFactorsActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
