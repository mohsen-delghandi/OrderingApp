package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.orderingapp.R;

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

public class IpSetActivity extends AppCompatActivity {

    EditText etIPFirstRun1, etIPFirstRun2, etIPFirstRun3, etIPFirstRun4, etTitleFirstRun;
    TextView btConnect;
    String ip;
    private WebService mTService;
    private WebService mTService2;
    AppPreferenceTools appPreferenceTools;
    ImageView ivHelp;

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
        setContentView(R.layout.ip_set_layout);

        ivHelp = (ImageView) findViewById(R.id.imageView_help);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IpSetActivity.this,HelpActivity.class);
                intent.putExtra("number","1");
                startActivity(intent);
            }
        });

        appPreferenceTools = new AppPreferenceTools(this);

        etIPFirstRun1 = (EditText) findViewById(R.id.editText_ip_firstrun1);
        etIPFirstRun2 = (EditText) findViewById(R.id.editText_ip_firstrun2);
        etIPFirstRun3 = (EditText) findViewById(R.id.editText_ip_firstrun3);
        etIPFirstRun4 = (EditText) findViewById(R.id.editText_ip_firstrun4);

        btConnect = (TextView) findViewById(R.id.textView_connect);

        etTitleFirstRun = (EditText) findViewById(R.id.editText_title_firstrun);

        etIPFirstRun1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun1.setText(255 + "");
                    etIPFirstRun1.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIPFirstRun2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun2.setText(255 + "");
                    etIPFirstRun2.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIPFirstRun3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun3.setText(255 + "");
                    etIPFirstRun3.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etIPFirstRun4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 255) {
                    etIPFirstRun4.setText(255 + "");
                    etIPFirstRun4.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SQLiteDatabase db = new MyDatabase(this).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.SETTINGS_TABLE, new String[]{MyDatabase.IP}, null, null, null, null, null);
        if (cursor.moveToFirst())
            ip = cursor.getString(0);
        else
            ip = "192.168.1.1";
        cursor.close();
        db.close();

        try {
            String[] ipArray = ip.split("\\.");
            etIPFirstRun1.setText(ipArray[0]);
            etIPFirstRun2.setText(ipArray[1]);
            etIPFirstRun3.setText(ipArray[2]);
            etIPFirstRun4.setText(ipArray[3]);
        } catch (Exception e) {
        }

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(IpSetActivity.this);
                loadingDialogClass.show();

                ip = etIPFirstRun1.getText().toString().trim() + "." +
                        etIPFirstRun2.getText().toString().trim() + "." +
                        etIPFirstRun3.getText().toString().trim() + "." +
                        etIPFirstRun4.getText().toString().trim();

                appPreferenceTools.saveDomainName(ip);

                WebProvider provider = new WebProvider();
                mTService = provider.getTService();
                WebProvider provider2 = new WebProvider();
                mTService2 = provider2.getTService();


                Call<Boolean> call2 = mTService2.deviceRegister(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), Build.MANUFACTURER + "-" + Build.MODEL);
                call2.enqueue(new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        if (response.isSuccessful()) {
                            if (response.body()) {

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Call<List<ContactModel>> call = mTService.getListContact();
                                        call.enqueue(new Callback<List<ContactModel>>() {
                                            @Override
                                            public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {

                                                if (response.isSuccessful()) {

                                                    SQLiteDatabase db = new MyDatabase(IpSetActivity.this).getWritableDatabase();
                                                    ContentValues cv = new ContentValues();
                                                    cv.put(MyDatabase.IP, ip);
                                                    db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                                                    db.close();
                                                    loadingDialogClass.dismiss();
                                                    startActivity(new Intent(IpSetActivity.this,TitleSetActivity.class));
                                                }else{
                                                    loadingDialogClass.dismiss();
                                                    Toast.makeText(IpSetActivity.this, "ارتباط با سرور برقرار نشد، دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<ContactModel>> call, Throwable t) {
                                                loadingDialogClass.dismiss();
                                                setProgressBarIndeterminateVisibility(false);
                                                Toast.makeText(IpSetActivity.this, "ارتباط با سرور برقرار نشد، دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }, 1234);

                            } else {
                                loadingDialogClass.dismiss();
                                Toast.makeText(IpSetActivity.this, "شما اجازه ی دسترسی به سرور را ندارید.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loadingDialogClass.dismiss();
                            Toast.makeText(IpSetActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        loadingDialogClass.dismiss();
                        Toast.makeText(IpSetActivity.this, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
