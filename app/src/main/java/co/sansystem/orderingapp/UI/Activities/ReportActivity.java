package co.sansystem.orderingapp.UI.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.sansystem.orderingapp.Models.ReportModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 2018-03-25.
 */

public class ReportActivity extends MainActivity {
    private WebService mTService;

    @BindView(R.id.linearLayout_az_tarikh)
    LinearLayout llAzTarikh;
    @BindView(R.id.linearLayout_ta_tarikh)
    LinearLayout llTaTarikh;
    @BindView(R.id.textView_az_tarikh)
    TextView tvAZTarikh;
    @BindView(R.id.textView_ta_tarikh)
    TextView tvTaTarikh;
    @BindView(R.id.textView_SumKolFact)
    TextView tvJameKol;
    @BindView(R.id.textView_JamBirunbar)
    TextView tvJameBirunBar;
    @BindView(R.id.textView_Jampeik)
    TextView tvJamePeyk;
    @BindView(R.id.textView_JaminSalon)
    TextView tvJameSalonHa;
    @BindView(R.id.textView_JamSumCancleFact)
    TextView tvJameFactorLaghvShode;
    @BindView(R.id.textView_mablagkhales)
    TextView tvJameMablaghjKhales;
    @BindView(R.id.textView_mohasebe)
    TextView tvMohasebe;

    String azTarikh, taTarikh;
    String day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInflater(this, R.layout.report_layout);
        ButterKnife.bind(this);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        tvTitlebar.setText(title + " - " + "گزارش های اخیر");
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icon_back);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        azTarikh = "01-01-2018";
        tvAZTarikh.setText(azTarikh);
        llAzTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if ((i2 + "").length() == 1) {
                            day = "0" + i2;
                        } else {
                            day = i2 + "";
                        }

                        if (((i1 + 1) + "").length() == 1) {
                            month = "0" + (i1 + 1);
                        } else {
                            month = (i1 + 1) + "";
                        }

                        year = i + "";

                        azTarikh = day + "-" + month + "-" + year;
                        tvAZTarikh.setText(azTarikh);
                    }
                }, 2018, 0, 01);
                datePickerDialog.show();
            }
        });

        taTarikh = "01-02-2018";
        tvTaTarikh.setText(taTarikh);
        llTaTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if ((i2 + "").length() == 1) {
                            day = "0" + i2;
                        } else {
                            day = i2 + "";
                        }

                        if (((i1 + 1) + "").length() == 1) {
                            month = "0" + (i1 + 1);
                        } else {
                            month = (i1 + 1) + "";
                        }

                        year = i + "";

                        taTarikh = day + "-" + month + "-" + year;
                        tvTaTarikh.setText(taTarikh);
                    }
                }, 2018, 1, 01);
                datePickerDialog.show();
            }
        });

        tvMohasebe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(ReportActivity.this);
                loadingDialogClass.show();
                Call<List<ReportModel>> call = mTService.reportForush(azTarikh,taTarikh);
                call.enqueue(new Callback<List<ReportModel>>() {

                    @Override
                    public void onResponse(Call<List<ReportModel>> call, Response<List<ReportModel>> response) {

                        if (response.isSuccessful()) {
                            loadingDialogClass.dismiss();
                            tvJameKol.setText(response.body().get(0).getSumKolFact());
                            tvJameBirunBar.setText(response.body().get(0).getJamBirunbar());
                            tvJamePeyk.setText(response.body().get(0).getJampeik());
                            tvJameSalonHa.setText(response.body().get(0).getJaminSalon());
                            tvJameFactorLaghvShode.setText(response.body().get(0).getJamSumCancleFact());
                            tvJameMablaghjKhales.setText(response.body().get(0).getMablagkhales());
                        } else {
                            Toast.makeText(ReportActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                            loadingDialogClass.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ReportModel>> call, Throwable t) {
                        Toast.makeText(ReportActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        loadingDialogClass.dismiss();
                    }
                });
            }
        });
    }
}