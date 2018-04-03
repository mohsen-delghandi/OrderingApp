package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.orderingapp.R;

import java.util.List;

import co.sansystem.orderingapp.Models.UserModel;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    TextView btSignIn;
    String name, id, password;
    WebService mTService;
    AppPreferenceTools appPreferenceTools;

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
        setContentView(R.layout.sign_in_layout);

        WebProvider webProvider = new WebProvider();
        mTService = webProvider.getTService();

        appPreferenceTools = new AppPreferenceTools(this);

        etUserName = (EditText) findViewById(R.id.editText_userName);
        etPassword = (EditText) findViewById(R.id.editText_passWord);

        btSignIn = (TextView) findViewById(R.id.textView_signIn);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(LoginActivity.this);
                loadingDialogClass.show();
                name = etUserName.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                Call<List<UserModel>> call = mTService.loginGarson(name, password);
                call.enqueue(new Callback<List<UserModel>>() {
                    @Override
                    public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                        loadingDialogClass.dismiss();
                        if (response.isSuccessful()) {

                            if (!response.body().get(0).getLoginID().equals("0")) {
                                appPreferenceTools.loginOK();
                                appPreferenceTools.saveUserAuthenticationInfo(response.body().get(0).getLoginName(), password, response.body().get(0).getLoginID());
                                startActivity(new Intent(LoginActivity.this, OrdersMenuActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "کلمه کاربری یا رمز اشتباه است.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserModel>> call, Throwable t) {

                        loadingDialogClass.dismiss();
                        Toast.makeText(LoginActivity.this, "خطا در برقراری ارتباط با سرور،دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}