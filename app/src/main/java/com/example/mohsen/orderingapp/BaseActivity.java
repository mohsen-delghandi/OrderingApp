package com.example.mohsen.orderingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mohsen on 2017-10-05.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent("PERMISSION_RECEIVER");
        intent.putExtra("requestCode",requestCode);
        intent.putExtra("permissions",permissions);
        intent.putExtra("grantResults",grantResults);
        sendBroadcast(intent);
    }
}
