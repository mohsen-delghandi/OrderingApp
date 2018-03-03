package co.sansystem.orderingapp;

import android.content.ContentValues;
import android.content.Intent;
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

/**
 * Created by Mohsen on 2017-06-16.
 */

public class LoginActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    TextView btSignIn, btSave;
    String responce, responce2, name, id, password;
    Spinner spCostumerCode;
    TableRow trMain, trDefaultCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUserName = (EditText) findViewById(R.id.editText_userName);
        etPassword = (EditText) findViewById(R.id.editText_password);
        btSignIn = (TextView) findViewById(R.id.button_sign_in);
        btSave = (TextView) findViewById(R.id.button_save);
        trDefaultCode = (TableRow) findViewById(R.id.tr_costumer_code);
        trMain = (TableRow) findViewById(R.id.tr_main);
        spCostumerCode = (Spinner) findViewById(R.id.spinner_default_costumer);

        final AppPreferenceTools appPreferenceTools = new AppPreferenceTools(LoginActivity.this);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        name = etUserName.getText().toString();
                        password = etPassword.getText().toString();
                        CallWebService cws = new CallWebService(LoginActivity.this, "LoginGarson", "_UserName", "_Password");
                        responce = cws.Call(name, password);

                        if (responce.length() > 5 && responce.substring(0, 4).equals("true")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    trMain.setVisibility(View.GONE);
                                    trDefaultCode.setVisibility(View.VISIBLE);
                                }
                            });


                            CallWebService cws2 = new CallWebService(LoginActivity.this, "GetListContact");
                            responce2 = cws2.Call();
                            JSONArray jsonArray = null;
                            final ArrayList<String> costumerNames = new ArrayList<String>();
                            final ArrayList<String> costumerCodes = new ArrayList<String>();
                            try {
                                jsonArray = new JSONArray(responce2);
                                for (int i = 0; i < jsonArray.length() - 1; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    costumerNames.add(jsonObject.get("FullName").toString());
                                    costumerCodes.add(jsonObject.get("Tafzili_ID").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> adapter =
                                            new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, costumerNames.toArray(new String[costumerNames.size()]));
                                    spCostumerCode.setAdapter(adapter);
                                }
                            });

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

                            btSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    appPreferenceTools.loginOK();
                                    appPreferenceTools.saveUserAuthenticationInfo(name, password, responce.substring(5));
                                    startActivity(new Intent(LoginActivity.this, OrdersMenuActivity.class));
                                    finish();
                                }
                            });
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

