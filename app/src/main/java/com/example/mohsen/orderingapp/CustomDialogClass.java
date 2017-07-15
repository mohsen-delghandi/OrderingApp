package com.example.mohsen.orderingapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mohsen on 2017-07-15.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView yes;
    public Button no;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basket_dialog_layout);
        yes = (TextView) findViewById(R.id.textView_ok);
//        no = (Button) findViewById(R.id.button_cancel);
        yes.setOnClickListener(this);
//        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_ok:
                Toast.makeText(c, "SENT", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.button_cancel:
//                dismiss();
//                break;
            default:
                break;
        }
        dismiss();
    }

}