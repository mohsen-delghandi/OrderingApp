package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.sansystem.orderingapp.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.sansystem.orderingapp.Models.ReportModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2018-03-25.
 */

public class ReportActivity extends AppCompatActivity {
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

    private String azTarikh;
    private String taTarikh;
    private String dayy;
    private String monthh;
    private String yearr;
    private ImageView ivBack;
    private TextView tvCurrency;
    private AppPreferenceTools appPreferenceTools;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_layout);
        ButterKnife.bind(this);

        appPreferenceTools = new AppPreferenceTools(this);
        ivBack = findViewById(R.id.imageView_nav_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvCurrency = findViewById(R.id.textView_currency);

        tvCurrency.setText("بر حسب " + appPreferenceTools.getCurrency());

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        final DatePicker.Builder builder = new DatePicker
                .Builder()
                .theme(R.style.DialogTheme)
                .future(true);

        final Date mDate = new Date();
        builder.date(mDate.getDay(), mDate.getMonth(), mDate.getYear());


        tvAZTarikh.setText(mDate.getDate());
        llAzTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.build(new DateSetListener() {
                    @Override
                    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                        mDate.setDate(day, month, year);


                        if ((calendar.get(Calendar.DAY_OF_MONTH) + "").length() == 1) {
                            dayy = "0" + calendar.get(Calendar.DAY_OF_MONTH);
                        } else {
                            dayy = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        }

                        if (((calendar.get(Calendar.MONTH) + 1) + "").length() == 1) {
                            monthh = "0" + (calendar.get(Calendar.MONTH) + 1);
                        } else {
                            monthh = (calendar.get(Calendar.MONTH) + 1) + "";
                        }

                        yearr = calendar.get(Calendar.YEAR) + "";



                        azTarikh = dayy + "-" + monthh + "-" + yearr;
                        tvAZTarikh.setText(mDate.getDate());
                    }
                }).show(getSupportFragmentManager(), "");
            }
        });

        tvTaTarikh.setText(mDate.getDate());
        llTaTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.build(new DateSetListener() {
                    @Override
                    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
                        mDate.setDate(day, month, year);


                        if ((calendar.get(Calendar.DAY_OF_MONTH) + "").length() == 1) {
                            dayy = "0" + calendar.get(Calendar.DAY_OF_MONTH);
                        } else {
                            dayy = calendar.get(Calendar.DAY_OF_MONTH) + "";
                        }

                        if (((calendar.get(Calendar.MONTH) + 1) + "").length() == 1) {
                            monthh = "0" + (calendar.get(Calendar.MONTH) + 1);
                        } else {
                            monthh = (calendar.get(Calendar.MONTH) + 1) + "";
                        }

                        yearr = calendar.get(Calendar.YEAR) + "";



                        taTarikh = dayy + "-" + monthh + "-" + yearr;
                        tvTaTarikh.setText(mDate.getDate());
                    }
                }).show(getSupportFragmentManager(), "");
            }
        });

        tvMohasebe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(ReportActivity.this);
                loadingDialogClass.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<List<ReportModel>> call = mTService.reportForush(azTarikh, taTarikh);
                        call.enqueue(new Callback<List<ReportModel>>() {

                            @Override
                            public void onResponse(Call<List<ReportModel>> call, Response<List<ReportModel>> response) {

                                if (response.isSuccessful()) {
                                    loadingDialogClass.dismiss();
                                    tvJameKol.setText(MainActivity.priceFormatter(response.body().get(0).getSumKolFact()));
                                    tvJameBirunBar.setText(MainActivity.priceFormatter(response.body().get(0).getJamBirunbar()));
                                    tvJamePeyk.setText(MainActivity.priceFormatter(response.body().get(0).getJampeik()));
                                    tvJameSalonHa.setText(MainActivity.priceFormatter(response.body().get(0).getJaminSalon()));
                                    tvJameFactorLaghvShode.setText(MainActivity.priceFormatter(response.body().get(0).getJamSumCancleFact()));
                                    tvJameMablaghjKhales.setText(MainActivity.priceFormatter(response.body().get(0).getMablagkhales()));
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
                }, 1234);
            }
        });
    }
}

class Date extends DateItem {
    String getDate() {
        Calendar calendar = getCalendar();
        return String.format(Locale.US,
                "%d/%d/%d",
                getYear(), getMonth(), getDay(),
                calendar.get(Calendar.YEAR),
                +calendar.get(Calendar.MONTH) + 1,
                +calendar.get(Calendar.DAY_OF_MONTH));
    }
}