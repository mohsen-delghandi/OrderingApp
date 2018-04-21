package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sansystem.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Models.ContactModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderInfoActivity extends AppCompatActivity {

    private Spinner spCostumerCode;
    private Spinner spVaziatSefaresh;
    private TextView btOK;
    private WebService mTService;
    private CheckBox checkBox;
    private AppPreferenceTools appPreferenceTools;

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
        setContentView(R.layout.order_info_layout);

        ImageView ivBack = findViewById(R.id.imageView_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ImageView ivHelp = findViewById(R.id.imageView_help);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderInfoActivity.this,HelpActivity.class);
                intent.putExtra("number","3");
                startActivity(intent);
            }
        });

        appPreferenceTools = new AppPreferenceTools(this);

        spCostumerCode = findViewById(R.id.spinner_default_costumer);
        spVaziatSefaresh = findViewById(R.id.spinner_vaziat_sefaresh);
        checkBox = findViewById(R.id.checkbox_print);
        btOK = findViewById(R.id.textView_ok);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appPreferenceTools.printAfterConfirm(b);
            }
        });

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(this);
        loadingDialogClass.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<List<ContactModel>> call = mTService.getListContact();
                call.enqueue(new Callback<List<ContactModel>>() {
                    @Override
                    public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {

                        if (response.isSuccessful()) {

                            SQLiteDatabase db = new MyDatabase(OrderInfoActivity.this).getWritableDatabase();
                            db.delete(MyDatabase.CONTACTS_INFORMATION,null,null);

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MyDatabase.LIST_CONTACT_JSON,json);
                            db.insert(MyDatabase.CONTACTS_INFORMATION,null,contentValues);
                            db.close();

                            final ArrayList<String> costumerNames = new ArrayList<>();
                            final ArrayList<String> costumerCodes = new ArrayList<>();

                            for (ContactModel contactModel :
                                    response.body()) {
                                costumerNames.add(contactModel.getFullName());
                                costumerCodes.add(contactModel.getTafziliID());
                            }

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(OrderInfoActivity.this, android.R.layout.simple_spinner_item, costumerNames.toArray(new String[costumerNames.size()]));
                            spCostumerCode.setAdapter(adapter);
                            loadingDialogClass.dismiss();

                            spCostumerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    appPreferenceTools.saveDefaultCostumerCode(costumerCodes.get(i));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    appPreferenceTools.saveDefaultCostumerCode(costumerCodes.get(0));
                                }
                            });

                            for (int i = 0; i < costumerNames.size(); i++) {
                                if (appPreferenceTools.getDefaultCostumerCode().equals(costumerCodes.get(i))) {
                                    final int finalI = i;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            spCostumerCode.setSelection(finalI);
                                        }
                                    });
                                }
                            }

                        } else {
                            loadingDialogClass.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ContactModel>> call, Throwable t) {
                        loadingDialogClass.dismiss();
                    }
                });
            }
        }, 1234);



        ArrayAdapter<String> adapterVaziatSefaresh =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.vaziat_sefaresh));
        spVaziatSefaresh.setAdapter(adapterVaziatSefaresh);

        spVaziatSefaresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                appPreferenceTools.saveVaziateSefaresh(getResources().getStringArray(R.array.vaziat_sefaresh)[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                appPreferenceTools.saveVaziateSefaresh("بیرون بر");
            }
        });

        if (!appPreferenceTools.getVaziatSefaresh().equals("")) {
            for (int i = 0; i < getResources().getStringArray(R.array.vaziat_sefaresh).length; i++) {
                if (getResources().getStringArray(R.array.vaziat_sefaresh)[i].equals(appPreferenceTools.getVaziatSefaresh())) {
                    spVaziatSefaresh.setSelection(i);
                }
            }
        } else {
            spVaziatSefaresh.setSelection(0);
        }

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderInfoActivity.this,LoginActivity.class));
            }
        });
    }

}