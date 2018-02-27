package co.sansystem.orderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

/**
 * Created by Mohsen on 2017-06-16.
 */

public class LoginActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    TextView btSignIn;
    String responce, name, id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etUserName = (EditText) findViewById(R.id.editText_userName);
        etPassword = (EditText) findViewById(R.id.editText_password);
        btSignIn = (TextView) findViewById(R.id.button_sign_in);

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
                            AppPreferenceTools appPreferenceTools = new AppPreferenceTools(LoginActivity.this);
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

