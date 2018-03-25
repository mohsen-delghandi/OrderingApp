package co.sansystem.orderingapp.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import java.util.List;

import co.sansystem.orderingapp.Adapters.LastFactorsAdapter;
import co.sansystem.orderingapp.Models.MiniFactorModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        ivTitlebarList.setVisibility(View.VISIBLE);
        ivTitlebarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LastFactorsActivity.this, ReportActivity.class);
                startActivity(i);
            }
        });

        tvTitlebar.setText(title + " - " + "فیش های اخیر");
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
        Call<List<MiniFactorModel>> call = mTService.getLastFactors();
        call.enqueue(new Callback<List<MiniFactorModel>>() {

            @Override
            public void onResponse(Call<List<MiniFactorModel>> call, Response<List<MiniFactorModel>> response) {

                if (response.isSuccessful()) {
                    lastFactorsAdapter.updateAdapterData(response.body());
                    lastFactorsAdapter.notifyDataSetChanged();
                    loadingDialogClass.dismiss();
                } else {
                    Toast.makeText(LastFactorsActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                    loadingDialogClass.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<MiniFactorModel>> call, Throwable t) {
                Toast.makeText(LastFactorsActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                loadingDialogClass.dismiss();
            }
        });
    }
}
