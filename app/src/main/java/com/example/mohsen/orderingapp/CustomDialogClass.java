package com.example.mohsen.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String mPriceFormatted = "",response,response2;
    JSONArray jsonArray;
    CallWebService cws,cws2;

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


        for(int i = 0 ; i < FoodOrdersAdapter.mList.size() ; i++){
            mPrice += Integer.parseInt(FoodOrdersAdapter.mList.get(i).getmPrice())*FoodOrdersAdapter.mList.get(i).getmNumber();
        }

        mPrice /= 10;

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

                if(etTable.getText().toString().equals("") || (Integer.parseInt(etTable.getText()+"") < 1)){
                    Toast.makeText(c, "شماره میز صحیح نیست.", Toast.LENGTH_SHORT).show();
                }else {
                    jsonArray = new JSONArray();
                    for (int i = 0 ; i < FoodOrdersAdapter.mList.size() ; i++){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Food_Code", FoodOrdersAdapter.mList.get(i).getCode());
                            jsonObject.put("Food_Count",FoodOrdersAdapter.mList.get(i).getmNumber());
                            jsonObject.put("Food_Price",Integer.parseInt(FoodOrdersAdapter.mList.get(i).getmPrice())*FoodOrdersAdapter.mList.get(i).getmNumber());
                            jsonObject.put("Table_Number", etTable.getText() + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }

                    cws = new CallWebService(c.getBaseContext(), "SaveFactor", "JsonString");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            response = cws.Call(jsonArray.toString());
                            if (response.equals("1")) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(c, "سفارش به صندوق ارسال شد.", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                });
                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(c, "ارتباط با سرور برقرار نشد.", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                });
                            }
                        }
                    }).start();

                    SQLiteDatabase db = new MyDatabase(c.getBaseContext()).getWritableDatabase();
                    db.delete(MyDatabase.ORDERS_TABLE, null, null);
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

                    OrdersMenuActivity.tvTayid.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            OrdersMenuActivity.tvTayid.setVisibility(View.GONE);
                        }
                    });

                    OrdersMenuActivity.fabToggle.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            OrdersMenuActivity.fabToggle.setVisibility(View.GONE);
                        }
                    });
                }

                break;
            case R.id.textView_cancel:
                dismiss();
                break;
            default:
                break;
        }
//        dismiss();
    }

}