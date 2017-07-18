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
    public TextView yes,no;
    long mPrice = 0;
    String mPriceFormatted = "";

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basket_dialog_layout);
        yes = (TextView) findViewById(R.id.textView_ok);
        no = (TextView) findViewById(R.id.textView_cancel);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        for(int i = 0 ; i<FoodOrdersAdapter.mList.size() ; i++){
            mPrice += FoodOrdersAdapter.mList.get(i).mNumber*1000;
        }
        TextView tv = (TextView)findViewById(R.id.textView_price);
        int a =  (mPrice+"").length();
        mPriceFormatted = (mPrice+"").substring(0,a%3);
        for(int i = 0 ; i < (mPrice+"").length()/3 ; i++) {
            if (!mPriceFormatted.equals("")) mPriceFormatted += ",";
            mPriceFormatted += (mPrice+"").substring(a % 3 + 3 * i , a % 3 + 3 * (i+1));
        }
        tv.setText(mPriceFormatted+"");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_ok:
                Toast.makeText(c, "SENT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}