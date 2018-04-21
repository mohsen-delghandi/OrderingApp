package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SuccessActivity extends AppCompatActivity {

    private TextView tvNameMoshtari;
    private TextView tvFishNumber;
    private TextView tvOkk;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_layout);

        tvNameMoshtari = findViewById(R.id.textView_moshtari_name);
        tvFishNumber = findViewById(R.id.textView_fish_number);

        tvNameMoshtari.setText(getIntent().getStringExtra("name_moshtari"));
        tvFishNumber.setText(getIntent().getStringExtra("fish_number"));

        tvOkk = findViewById(R.id.textView_okk);
        tvOkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessActivity.this, OrdersMenuActivity.class));
                finish();
            }
        });
    }
}
