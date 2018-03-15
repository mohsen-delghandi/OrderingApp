package co.sansystem.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Mohsen on 2017-07-15.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView yes, no, text, jameKol, tv, tvNameMoshtari;
    public EditText etTable;
    public EditText etName;
    long mPrice = 0;
    String mPriceFormatted = "", response, costumerCode;
    JSONArray jsonArray;
    CallWebService cws;
    LinearLayout llLoadingDialog;
    TableLayout tlMain;
    TableRow trJameKol, trVaziat;
    AppPreferenceTools appPreferenceTools;
    Spinner spVaziatSefaresh;
    String vaziatSefaresh;
    private WebService mTService;

    public CustomDialogClass(Activity a, String defaultCostumerCode) {
        super(a);
        this.c = a;
        costumerCode = defaultCostumerCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basket_dialog_layout);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        etTable = findViewById(R.id.editText_tableNumber);
        etName = findViewById(R.id.editText_name);
        llLoadingDialog = findViewById(R.id.llLoading_dialog);
        tlMain = findViewById(R.id.tableLayout_main);
        yes = findViewById(R.id.textView_ok);
        no = findViewById(R.id.textView_cancel);
        text = findViewById(R.id.textView_text);
        jameKol = findViewById(R.id.textView_jameKol);
        tv = findViewById(R.id.textView_price);
        tvNameMoshtari = findViewById(R.id.textView_nameMoshtari);
        trJameKol = findViewById(R.id.tableRow_jameKol);
        trVaziat = findViewById(R.id.tableRow_vaziat);
        no.setBackgroundColor(c.getResources().getColor(R.color.red));
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        appPreferenceTools = new AppPreferenceTools(c);
        spVaziatSefaresh = findViewById(R.id.spinner_vaziat_sefaresh);

        vaziatSefaresh = appPreferenceTools.getVaziatSefaresh();

        ArrayAdapter<String> adapterVaziatSefaresh =
                new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, c.getResources().getStringArray(R.array.vaziat_sefaresh));
        spVaziatSefaresh.setAdapter(adapterVaziatSefaresh);

        spVaziatSefaresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vaziatSefaresh = i + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vaziatSefaresh = "0";
            }
        });

        spVaziatSefaresh.setSelection(Integer.parseInt(appPreferenceTools.getVaziatSefaresh()));

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


        for (int i = 0; i < FoodOrdersAdapter.mList.size(); i++) {
            mPrice += Integer.parseInt(FoodOrdersAdapter.mList.get(i).getmPrice()) * FoodOrdersAdapter.mList.get(i).getmNumber();
        }

        int a = (mPrice + "").length();
        mPriceFormatted = (mPrice + "").substring(0, a % 3);
        for (int i = 0; i < (mPrice + "").length() / 3; i++) {
            if (!mPriceFormatted.equals("")) mPriceFormatted += ",";
            mPriceFormatted += (mPrice + "").substring(a % 3 + 3 * i, a % 3 + 3 * (i + 1));
        }
        tv.setText(mPriceFormatted + "");
    }

    @Override
    public void dismiss() {
        InputMethodManager inputMethodManager = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        inputMethodManager.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else {
            inputMethodManager.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
        }
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_ok:

                if (etTable.getText().toString().equals("") || (Integer.parseInt(etTable.getText() + "") < 1)) {
                    Toast.makeText(c, "شماره میز صحیح نیست.", Toast.LENGTH_SHORT).show();
                } else if (etName.getText().toString().equals("")) {
                    Toast.makeText(c, "نام مشتری را وارد کنید.", Toast.LENGTH_SHORT).show();
                } else {
                    jsonArray = new JSONArray();
                    for (int i = 0; i < FoodOrdersAdapter.mList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Food_Code", FoodOrdersAdapter.mList.get(i).getCode());
                            jsonObject.put("Food_Count", FoodOrdersAdapter.mList.get(i).getmNumber());
                            jsonObject.put("Food_Price", Integer.parseInt(FoodOrdersAdapter.mList.get(i).getmPrice()) * FoodOrdersAdapter.mList.get(i).getmNumber());
                            jsonObject.put("Table_Number", etTable.getText() + "");
                            jsonObject.put("Costumer_Code", costumerCode);
                            jsonObject.put("Costumer_Name", etName.getText() + "");
                            if (appPreferenceTools.getprintAfterConfirm()) {
                                jsonObject.put("Print_Confirm", "true");
                            } else {
                                jsonObject.put("Print_Confirm", "false");
                            }
                            jsonObject.put("Food_Exp", FoodOrdersAdapter.mList.get(i).getmExp());
                            jsonObject.put("User_Id", appPreferenceTools.getUserID() + "");
                            jsonObject.put("Price_Sum", mPrice + "");
                            jsonObject.put("Vaziat_Sefaresh", vaziatSefaresh);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }


                    InputMethodManager imm = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etTable.getWindowToken(), 0);
                    llLoadingDialog.setVisibility(View.VISIBLE);
                    tlMain.setAlpha(0.1f);

                    Call<Object> call = mTService.saveFactor(jsonArray.toString());
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()) {

                                Toast.makeText(c, "سفارش به صندوق ارسال شد.", Toast.LENGTH_SHORT).show();
                                FoodOrdersAdapter.mList.clear();
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {

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
                                } else {
                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
                                    params.height = 0;
                                    OrdersMenuActivity.ll.setLayoutParams(params);
                                    OrdersMenuActivity.tvTayid.setAlpha(0f);
                                    OrdersMenuActivity.fabToggle.setAlpha(0f);
                                }

                                jameKol.setText("فاکتور با موفقیت ثبت گردید.");
                                jameKol.setTextSize(30);
                                jameKol.setTextColor(c.getResources().getColor(R.color.accent));
                                // trJameKol.setBackgroundColor(c.getResources().getColor(R.color.accent));
                                tv.setVisibility(View.GONE);
                                no.setText("تایید");
                                no.setBackgroundColor(c.getResources().getColor(R.color.green));
                                text.setText("شماره فیش ارسالی به شماره " + response.body().toString().substring(4) + " به نام " + etName.getText().toString().trim() + " ثبت گردید.");
                                text.setTextSize(25);
                                text.setPadding(40, 40, 40, 40);
                                etTable.setVisibility(View.GONE);
                                llLoadingDialog.setVisibility(View.GONE);
                                tvNameMoshtari.setVisibility(View.GONE);
                                etName.setVisibility(View.GONE);
                                trVaziat.setVisibility(View.GONE);
                                tlMain.setAlpha(1f);
                                yes.setVisibility(View.GONE);
                            } else {

                                SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
                                ContentValues cv2 = new ContentValues();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                String myDate = format.format(new Date());
                                cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response);
                                db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                                db2.close();
                                llLoadingDialog.setVisibility(View.GONE);
                                tlMain.setAlpha(1f);
                                Toast.makeText(c, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                            SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
                            ContentValues cv2 = new ContentValues();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            String myDate = format.format(new Date());
                            cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response);
                            db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                            db2.close();
                            llLoadingDialog.setVisibility(View.GONE);
                            tlMain.setAlpha(1f);
                            Toast.makeText(c, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        }
                    });


//                    cws = new CallWebService(c.getBaseContext(), "SaveFactor", "JsonString");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                public void run() {
//                                    InputMethodManager imm = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
//                                    imm.hideSoftInputFromWindow(etTable.getWindowToken(), 0);
//                                    llLoadingDialog.setVisibility(View.VISIBLE);
//                                    tlMain.setAlpha(0.1f);
//                                }
//                            });

//                            response = cws.Call(jsonArray.toString());
//                            if (response.length() > 4 && response.substring(0, 4).equals("True")) {
//                                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                    public void run() {
//                                        Toast.makeText(c, "سفارش به صندوق ارسال شد.", Toast.LENGTH_SHORT).show();
//                                        FoodOrdersAdapter.mList.clear();
//                                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//
//                                            ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(OrdersMenuActivity.ll);
//                                            ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,
//                                                    "height",
//                                                    animationWrapper.getHeight(),
//                                                    0);
//                                            anim.setDuration(300);
//                                            anim.setInterpolator(new FastOutLinearInInterpolator());
//                                            anim.start();
//
//                                            OrdersMenuActivity.tvTayid.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                                                @Override
//                                                public void onAnimationEnd(Animator animation) {
//                                                    OrdersMenuActivity.tvTayid.setVisibility(View.GONE);
//                                                }
//                                            });
//
//                                            OrdersMenuActivity.fabToggle.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                                                @Override
//                                                public void onAnimationEnd(Animator animation) {
//                                                    OrdersMenuActivity.fabToggle.setVisibility(View.GONE);
//                                                }
//                                            });
//                                        } else {
//                                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
//                                            params.height = 0;
//                                            OrdersMenuActivity.ll.setLayoutParams(params);
//                                            OrdersMenuActivity.tvTayid.setAlpha(0f);
//                                            OrdersMenuActivity.fabToggle.setAlpha(0f);
//                                        }
//
//                                        jameKol.setText("فاکتور با موفقیت ثبت گردید.");
//                                        jameKol.setTextSize(30);
//                                        jameKol.setTextColor(c.getResources().getColor(R.color.accent));
//                                        // trJameKol.setBackgroundColor(c.getResources().getColor(R.color.accent));
//                                        tv.setVisibility(View.GONE);
//                                        no.setText("تایید");
//                                        no.setBackgroundColor(c.getResources().getColor(R.color.green));
//                                        text.setText("شماره فیش ارسالی به شماره " + response.substring(4) + " به نام " + etName.getText().toString().trim() + " ثبت گردید.");
//                                        text.setTextSize(25);
//                                        text.setPadding(40, 40, 40, 40);
//                                        etTable.setVisibility(View.GONE);
//                                        llLoadingDialog.setVisibility(View.GONE);
//                                        tvNameMoshtari.setVisibility(View.GONE);
//                                        etName.setVisibility(View.GONE);
//                                        trVaziat.setVisibility(View.GONE);
//                                        tlMain.setAlpha(1f);
//                                        yes.setVisibility(View.GONE);
////                                        InputMethodManager inputMethodManager = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
////                                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                                    }
//                                });
//                            } else {
//                                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                    public void run() {
//                                        SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
//                                        ContentValues cv2 = new ContentValues();
//                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//                                        String myDate = format.format(new Date());
//                                        cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response);
//                                        db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
//                                        db2.close();
//                                        llLoadingDialog.setVisibility(View.GONE);
//                                        tlMain.setAlpha(1f);
//                                        Toast.makeText(c, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
////                                        dismiss();
//                                    }
//                                });
//                            }
//                        }
//                    }).start();


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