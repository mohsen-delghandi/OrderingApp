package com.example.mohsen.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
    public EditText etTable;
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
        etTable = (EditText)findViewById(R.id.editText_tableNumber);
        yes = (TextView) findViewById(R.id.textView_ok);
        no = (TextView) findViewById(R.id.textView_cancel);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        etTable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    CustomDialogClass.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });


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
                Toast.makeText(c, "سفارش به صندوق ارسال شد.", Toast.LENGTH_SHORT).show();

                SQLiteDatabase db = new MyDatabase(c.getBaseContext()).getWritableDatabase();
                db.delete(MyDatabase.ORDERS_TABLE,null,null);
                db.close();

                FoodOrdersAdapter.mList.clear();


                ViewWeightAnimationWrapper animationWrapper = new ViewWeightAnimationWrapper(OrdersMenuActivity.ll);
                ObjectAnimator anim = ObjectAnimator.ofFloat(animationWrapper,
                        "weight",
                        animationWrapper.getWeight(),
                        0f);
                anim.setDuration(300);
                anim.setInterpolator(new FastOutLinearInInterpolator());
                anim.start();

//                    ObjectAnimator obj2 = ObjectAnimator.ofFloat(OrdersMenuActivity.tvTayid,"alpha",1f,0f);
//                    obj2.setDuration(300);
//                    obj2.start();

                OrdersMenuActivity.tvTayid.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        OrdersMenuActivity.tvTayid.setVisibility(View.GONE);
                    }
                });

//                    final Handler handler3 = new Handler();
//                    handler3.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            OrdersMenuActivity.tvTayid.setVisibility(View.GONE);
//                        }
//                    }, 300);


                OrdersMenuActivity.fabToggle.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        OrdersMenuActivity.fabToggle.setVisibility(View.GONE);
                    }
                    });

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