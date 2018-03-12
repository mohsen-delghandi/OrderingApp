package co.sansystem.orderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-06-16.
 */

public class LoginActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    TextView btSignIn,btChangeIp, btSave;
    String responce, responce2, name, id, password;
    Spinner spCostumerCode;
    TableRow trMain;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUserName = (EditText) findViewById(R.id.editText_userName);
        etPassword = (EditText) findViewById(R.id.editText_password);
        btSignIn = (TextView) findViewById(R.id.button_sign_in);
        btChangeIp = (TextView) findViewById(R.id.button_change_ip);
        btSave = (TextView) findViewById(R.id.button_save);
        trMain = (TableRow) findViewById(R.id.tr_main);
        spCostumerCode = (Spinner) findViewById(R.id.spinner_default_costumer);

        final AppPreferenceTools appPreferenceTools = new AppPreferenceTools(LoginActivity.this);

        btChangeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbb = new MyDatabase(LoginActivity.this).getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(MyDatabase.FIRST_RUN,1);
                dbb.update(MyDatabase.SETTINGS_TABLE,cv,MyDatabase.ID + " = 1",null);


                Intent i = new Intent(LoginActivity.this, SettingsActivity.class);
                i.putExtra("status","fromSplash");
                startActivity(i);
                finish();
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        name = etUserName.getText().toString().trim();
                        password = etPassword.getText().toString().trim();
                        CallWebService cws = new CallWebService(LoginActivity.this, "LoginGarson", "_UserName", "_Password");
                        responce = cws.Call(name, password);

                        if (responce.length() > 5 && responce.substring(0, 4).equals("true")) {

                            appPreferenceTools.loginOK();
                            appPreferenceTools.saveUserAuthenticationInfo(name, password, responce.substring(5));
                            startActivity(new Intent(LoginActivity.this, OrdersMenuActivity.class));
                            finish();
                        } else {
                            if (responce.equals("0")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "اطلاعات کاربر معتبر نیست.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "ارتباط با سرور برقرار نشد.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                }).start();
            }
        });
    }
}

