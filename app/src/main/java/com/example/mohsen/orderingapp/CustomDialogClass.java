package com.example.mohsen.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

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
    String mPriceFormatted = "",response;
    JSONArray jsonArray;
    CallWebService cws;
    LinearLayout llLoadingDialog;
    TableLayout tlMain;

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
        llLoadingDialog = (LinearLayout)findViewById(R.id.llLoading_dialog);
        tlMain = (TableLayout)findViewById(R.id.tableLayout_main);
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

        etTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && Integer.parseInt(charSequence.toString()) > 999) {
                    etTable.setText(999 + "");
                    etTable.selectAll();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
    public void dismiss() {
        InputMethodManager inputMethodManager = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        inputMethodManager.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        super.dismiss();
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
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager)c.getSystemService(INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(etTable.getWindowToken(), 0);
                                    llLoadingDialog.setVisibility(View.VISIBLE);
                                    tlMain.setAlpha(0.1f);
                                }
                            });

                            response = cws.Call(jsonArray.toString());
                            if (response.equals("1")) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(c, "سفارش به صندوق ارسال شد.", Toast.LENGTH_SHORT).show();
                                        FoodOrdersAdapter.mList.clear();
                                        ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(OrdersMenuActivity.ll);
                                        ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,
                                                "height",
                                                animationWrapper.getHeight(),
                                                0);
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
                                        dismiss();
//                                        InputMethodManager inputMethodManager = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
//                                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                    }
                                });
                            } else {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
                                        ContentValues cv2 = new ContentValues();
                                        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        String myDate = format.format(new Date());
                                        cv2.put(MyDatabase.RESPONCE,myDate + " --> " +response);
                                        db2.insert(MyDatabase.RESPONCES_TABLE,null,cv2);
                                        db2.close();
                                        llLoadingDialog.setVisibility(View.GONE);
                                        tlMain.setAlpha(1f);
                                        Toast.makeText(c, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
//                                        dismiss();
                                    }
                                });
                            }
                        }
                    }).start();


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