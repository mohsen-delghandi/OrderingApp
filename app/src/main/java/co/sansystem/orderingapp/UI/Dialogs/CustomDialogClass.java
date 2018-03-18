package co.sansystem.orderingapp.UI.Dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Models.FactorModel;
import co.sansystem.orderingapp.Models.FavoriteModel;
import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.Utility.Animation.ViewHeightAnimationWrapper;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
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
    public LinearLayout llLoadingDialog;
    public TableLayout tlMain;
    TableRow trJameKol;
    public TableRow trVaziat;
    AppPreferenceTools appPreferenceTools;
    Spinner spVaziatSefaresh;
    String vaziatSefaresh;
    String tableNumber = null, costumerName = null, vaziat_sefaresh = null, factorNumber = "0", Fish_Number = "0";
    private WebService mTService;
    private WebService mTService3;

    public CustomDialogClass(Activity a, String defaultCostumerCode) {
        super(a);
        this.c = a;
        costumerCode = defaultCostumerCode;
    }

    public CustomDialogClass(Activity a, String defaultCostumerCode, String tableNumber, String costumer_name,
                             String vaziat_sefaresh, String factorNumber, String Fish_Number) {
        super(a);
        this.c = a;
        costumerCode = defaultCostumerCode;
        this.tableNumber = tableNumber;
        costumerName = costumer_name;
        this.vaziat_sefaresh = vaziat_sefaresh;
        this.factorNumber = factorNumber;
        this.Fish_Number = Fish_Number;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basket_dialog_layout);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        WebProvider provider3 = new WebProvider();
        mTService3 = provider3.getTService();

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
                vaziatSefaresh = c.getResources().getStringArray(R.array.vaziat_sefaresh)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vaziatSefaresh = "بیرون بر";
            }
        });


        if (tableNumber != null) {
            etTable.setText(tableNumber);
            etName.setText(costumerName);
            for (int i = 0; i < c.getResources().getStringArray(R.array.vaziat_sefaresh).length; i++) {
                if (c.getResources().getStringArray(R.array.vaziat_sefaresh)[i].equals(vaziat_sefaresh)) {
                    spVaziatSefaresh.setSelection(i);
                }
            }
        }

        for (int i = 0; i < c.getResources().getStringArray(R.array.vaziat_sefaresh).length; i++) {
            if (c.getResources().getStringArray(R.array.vaziat_sefaresh)[i].equals(vaziat_sefaresh)) {
                spVaziatSefaresh.setSelection(i);
            }
        }

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
                    List<FactorModel> factorModelList = new ArrayList<>();
                    for (int i = 0; i < FoodOrdersAdapter.mList.size(); i++) {
                        FactorModel factorModel = new FactorModel();

                        factorModel.setFoodCode(FoodOrdersAdapter.mList.get(i).getCode());
                        factorModel.setFoodCount(FoodOrdersAdapter.mList.get(i).getmNumber());
                        factorModel.setSumPriceRow((Integer.parseInt(FoodOrdersAdapter.mList.get(i).getmPrice()) * FoodOrdersAdapter.mList.get(i).getmNumber()) + "");
                        factorModel.setTableNumber(etTable.getText() + "");
                        factorModel.setCostumerCode(costumerCode);
                        factorModel.setCostumerName(etName.getText() + "");
                        if (appPreferenceTools.getprintAfterConfirm()) {
                            factorModel.setPrintConfirm("true");
                        } else {
                            factorModel.setPrintConfirm("false");
                        }
                        factorModel.setFoodExp(FoodOrdersAdapter.mList.get(i).getmExp());
                        factorModel.setUserId(appPreferenceTools.getUserID() + "");
                        factorModel.setPriceSum(mPrice + "");
                        factorModel.setVaziatSefaresh(vaziatSefaresh);

                        factorModelList.add(factorModel);
                    }


                    InputMethodManager imm = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etTable.getWindowToken(), 0);
                    llLoadingDialog.setVisibility(View.VISIBLE);
                    tlMain.setAlpha(0.1f);

                    Call<Object> call = mTService.saveFactor(factorModelList,factorNumber,Fish_Number);
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
                                tv.setVisibility(View.GONE);
                                no.setText("تایید");
                                no.setBackgroundColor(c.getResources().getColor(R.color.green));
                                text.setText("شماره فیش ارسالی به شماره " + response.body().toString() + " به نام " + etName.getText().toString().trim() + " ثبت گردید.");
                                text.setTextSize(25);
                                text.setPadding(40, 40, 40, 40);
                                etTable.setVisibility(View.GONE);
                                llLoadingDialog.setVisibility(View.GONE);
                                tvNameMoshtari.setVisibility(View.GONE);
                                etName.setVisibility(View.GONE);
                                trVaziat.setVisibility(View.GONE);
                                tlMain.setAlpha(1f);
                                yes.setVisibility(View.GONE);

                                Call<List<FavoriteModel>> call2 = mTService3.getFoodFavorite();
                                call2.enqueue(new Callback<List<FavoriteModel>>() {

                                    @Override
                                    public void onResponse
                                            (Call<List<FavoriteModel>> call, Response<List<FavoriteModel>> response) {

                                        if (response.isSuccessful()) {


                                            SQLiteDatabase dbFavorite = new MyDatabase(c).getWritableDatabase();

                                            dbFavorite.execSQL("UPDATE " + MyDatabase.FOOD_TABLE + " SET " + MyDatabase.FAVORITE + " = '0' ;");

                                            for (FavoriteModel favoriteModel :
                                                    response.body()) {
                                                dbFavorite.execSQL(" UPDATE " + MyDatabase.FOOD_TABLE + " SET " + MyDatabase.FAVORITE + " = '1' WHERE " + MyDatabase.CODE + " = " + favoriteModel.getIDKala());
                                            }


                                            dbFavorite.close();

                                            NavigationAdapter.refreshFavorites();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<List<FavoriteModel>> call, Throwable t) {
                                    }
                                });
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