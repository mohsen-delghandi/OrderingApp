package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sansystem.orderingapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2018-03-25.
 */

public class HelpActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Intent intent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        intent = getIntent();
        if (intent.getStringExtra("number").equals("1"))
            setContentView(R.layout.help_ip_layout);
        if (intent.getStringExtra("number").equals("2"))
            setContentView(R.layout.help_title_layout);
        if (intent.getStringExtra("number").equals("3"))
            setContentView(R.layout.help_info_layout);
        if (intent.getStringExtra("number").equals("4"))
            setContentView(R.layout.help_login_layout);

        ivBack = findViewById(R.id.imageView_nav_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}