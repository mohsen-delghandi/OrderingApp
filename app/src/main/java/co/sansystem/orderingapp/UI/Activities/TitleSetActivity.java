package co.sansystem.orderingapp.UI.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TitleSetActivity extends AppCompatActivity {

    EditText et_title;
    TextView btSave;
    private ImageView ivHelp;
    private ImageView ivBack;

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
        setContentView(R.layout.title_set_layout);

        ivBack = (ImageView) findViewById(R.id.imageView_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivHelp = (ImageView) findViewById(R.id.imageView_help);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TitleSetActivity.this,HelpActivity.class));
            }
        });

        SQLiteDatabase db = new MyDatabase(this).getWritableDatabase();
        Cursor cursor = db.query(MyDatabase.SETTINGS_TABLE, new String[]{MyDatabase.TITLE}, null, null, null, null, null);
        cursor.moveToFirst();
        String title = cursor.getString(0);
        cursor.close();
        db.close();

        btSave = (TextView) findViewById(R.id.textView_save);

        et_title = (EditText) findViewById(R.id.editText_title);
        et_title.setText(title);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = new MyDatabase(TitleSetActivity.this).getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(MyDatabase.TITLE, et_title.getText().toString());
                db.update(MyDatabase.SETTINGS_TABLE, cv, MyDatabase.ID + " = ?", new String[]{" 1 "});
                db.close();

                startActivity(new Intent(TitleSetActivity.this,OrderInfoActivity.class));
            }
        });
    }
}