package co.sansystem.orderingapp.UI.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sansystem.orderingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.sansystem.orderingapp.Adapters.FoodOrdersAdapter;
import co.sansystem.orderingapp.Adapters.NavigationAdapter;
import co.sansystem.orderingapp.Models.AddressModel;
import co.sansystem.orderingapp.Models.ContactModel;
import co.sansystem.orderingapp.Models.FactorContentModel;
import co.sansystem.orderingapp.Models.FavoriteModel;
import co.sansystem.orderingapp.Models.TellModel;
import co.sansystem.orderingapp.UI.Activities.MainActivity;
import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
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
    public TextView tvOkk, yes, text, jameKol, tvJameKol, tvNameMoshtari,tvFishNumber, tvMaliatText, tvMaliat, tvTakhfifText, tvTakhfif, tvServiceText, tvService, tvFactorText, tvFactor;
    public ImageView no;
    public EditText etTable;
    long mPrice = 0;
    String costumerCode;
    public LinearLayout llLoadingDialog, llSuccess;
    public ScrollView svMain;
    public RelativeLayout rlMain, rlSuccess;
    public TableLayout tlMain;
    AppPreferenceTools appPreferenceTools;
    public Spinner spVaziatSefaresh;
    String vaziatSefaresh;
    String tableNumber = null, costumerName = null, vaziat_sefaresh = null, factorNumber = "0", Fish_Number = "0";
    private WebService mTService;
    private WebService mTService2;
    private WebService mTService3;
    private WebService mTService4;
    private WebService mTService5;
    public AutoCompleteTextView textView;
    public AutoCompleteTextView textViewAddress;
    public AutoCompleteTextView textViewTell;
    ArrayList<String> costumerNamess;
    ArrayList<String> costumerCodess;
    ArrayList<String> costumerTafzilis;
    ArrayList<String> costumerAddresses;
    ArrayList<String> costumerTells;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.basket_dialog_layout);

        rlMain = findViewById(R.id.relativeLayout_main);
        rlSuccess = findViewById(R.id.relativeLayout_success);
        svMain = findViewById(R.id.scrollView);
        llSuccess = findViewById(R.id.linearLayout_success);

        tvNameMoshtari = findViewById(R.id.textView_moshtari_name);
        tvFishNumber = findViewById(R.id.textView_fish_number);
        tvOkk = findViewById(R.id.textView_okk);
        tvOkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        WebProvider provider2 = new WebProvider();
        mTService2 = provider2.getTService();

        WebProvider provider3 = new WebProvider();
        mTService3 = provider3.getTService();

        WebProvider provider4 = new WebProvider();
        mTService4 = provider4.getTService();

        WebProvider provider5 = new WebProvider();
        mTService5 = provider5.getTService();


        costumerNamess = new ArrayList<String>();
        costumerCodess = new ArrayList<String>();
        costumerTafzilis = new ArrayList<String>();
        costumerAddresses = new ArrayList<String>();
        costumerTells = new ArrayList<String>();


        SQLiteDatabase db = new MyDatabase(c).getWritableDatabase();
        Cursor cursor = db.query(MyDatabase.CONTACTS_INFORMATION, new String[]{MyDatabase.LIST_CONTACT_JSON}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Gson gson = new Gson();
            String json = cursor.getString(0);
            List<ContactModel> contactModelList = gson.fromJson(json, new TypeToken<ArrayList<ContactModel>>() {
            }.getType());
            for (ContactModel contactModel :
                    contactModelList) {
                costumerNamess.add(contactModel.getFullName());
                costumerCodess.add(contactModel.getContactsID());
                costumerTafzilis.add(contactModel.getTafziliID());
            }
        }
        db.close();

        Call<List<ContactModel>> call = mTService2.getListContact();
        call.enqueue(new Callback<List<ContactModel>>() {
            @Override
            public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {

                if (response.isSuccessful()) {

                    SQLiteDatabase db = new MyDatabase(c).getWritableDatabase();
                    db.delete(MyDatabase.CONTACTS_INFORMATION, null, null);

                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MyDatabase.LIST_CONTACT_JSON, json);
                    db.insert(MyDatabase.CONTACTS_INFORMATION, null, contentValues);

                    db.close();

                    for (ContactModel contactModel :
                            response.body()) {
                        costumerNamess.add(contactModel.getFullName());
                        costumerCodess.add(contactModel.getContactsID());
                        costumerTafzilis.add(contactModel.getTafziliID());
                    }
                    db.close();
                }
            }

            @Override
            public void onFailure(Call<List<ContactModel>> call, Throwable t) {
            }
        });

        textView = findViewById(R.id.editText_name);
        textViewAddress = findViewById(R.id.editText_adress);
        textViewTell = findViewById(R.id.editText_tell);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, costumerNamess);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);
                int pos = -1;

                for (int i = 0; i < costumerNamess.size(); i++) {
                    if (costumerNamess.get(i).equals(selection)) {
                        pos = i;
                        break;
                    }
                }

                SQLiteDatabase db = new MyDatabase(c).getWritableDatabase();
                Cursor cursor = db.query(MyDatabase.CONTACTS_ADDRESSES, new String[]{MyDatabase.LIST_ADDRESS_JSON}, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    Gson gson = new Gson();
                    String json = cursor.getString(0);
                    List<AddressModel> contactModelList = gson.fromJson(json, new TypeToken<ArrayList<AddressModel>>() {
                    }.getType());
                    for (AddressModel addressModel :
                            contactModelList) {
                        costumerAddresses.add(addressModel.getAdress());
                    }

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, costumerAddresses);
                    textViewAddress.setAdapter(adapter2);
                    if (costumerAddresses.size() == 0) {
                        textViewAddress.setText("0");
                    } else {
                        textViewAddress.setText(costumerAddresses.get(0));
                    }
                }
                db.close();

                Call<List<AddressModel>> call2 = mTService4.getAddressContact(costumerCodess.get(pos));
                call2.enqueue(new Callback<List<AddressModel>>() {
                    @Override
                    public void onResponse(Call<List<AddressModel>> call2, Response<List<AddressModel>> response) {

                        if (response.isSuccessful()) {

                            SQLiteDatabase db = new MyDatabase(c).getWritableDatabase();
                            db.delete(MyDatabase.CONTACTS_ADDRESSES, null, null);

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MyDatabase.LIST_ADDRESS_JSON, json);
                            db.insert(MyDatabase.CONTACTS_ADDRESSES, null, contentValues);

                            db.close();

                            for (AddressModel addressModel :
                                    response.body()) {
                                costumerAddresses.add(addressModel.getAdress());
                            }

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, costumerAddresses);
                            textViewAddress.setAdapter(adapter2);
                            if (costumerAddresses.size() == 0) {
                                textViewAddress.setText("0");
                            } else {
                                textViewAddress.setText(costumerAddresses.get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AddressModel>> call2, Throwable t) {
                    }

                });


                SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
                Cursor cursor2 = db2.query(MyDatabase.CONTACTS_TELLS, new String[]{MyDatabase.LIST_TELL_JSON}, null, null, null, null, null);
                if (cursor2.moveToFirst()) {
                    Gson gson = new Gson();
                    String json = cursor2.getString(0);
                    List<TellModel> contactModelList = gson.fromJson(json, new TypeToken<ArrayList<TellModel>>() {
                    }.getType());
                    for (TellModel tellModel :
                            contactModelList) {
                        costumerTells.add(tellModel.getTellContact());
                    }

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, costumerTells);
                    textViewTell.setAdapter(adapter3);
                    if (costumerTells.size() == 0) {
                        textViewTell.setText("0");
                    } else {
                        textViewTell.setText(costumerTells.get(0));
                    }
                }
                db2.close();

                Call<List<TellModel>> calll = mTService5.getTellContact(costumerCodess.get(pos));
                calll.enqueue(new Callback<List<TellModel>>() {
                    @Override
                    public void onResponse(Call<List<TellModel>> calll, Response<List<TellModel>> response) {

                        if (response.isSuccessful()) {


                            SQLiteDatabase db = new MyDatabase(c).getWritableDatabase();
                            db.delete(MyDatabase.CONTACTS_TELLS, null, null);

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MyDatabase.LIST_TELL_JSON, json);
                            db.insert(MyDatabase.CONTACTS_TELLS, null, contentValues);

                            db.close();

                            for (TellModel tellModel :
                                    response.body()) {
                                costumerTells.add(tellModel.getTellContact());
                            }

                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, costumerTells);
                            textViewTell.setAdapter(adapter3);
                            if (costumerTells.size() == 0) {
                                textViewTell.setText("0");
                            } else {
                                textViewTell.setText(costumerTells.get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TellModel>> calll, Throwable t) {
                    }
                });
            }
        });


        etTable = findViewById(R.id.editText_tableNumber);
        yes = findViewById(R.id.textView_ok);
        no = findViewById(R.id.textView_cancel);
        text = findViewById(R.id.textView_text);
        jameKol = findViewById(R.id.textView_jameKol);
        tvJameKol = findViewById(R.id.textView_price);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        appPreferenceTools = new AppPreferenceTools(c);
        spVaziatSefaresh = findViewById(R.id.spinner_vaziat_sefaresh);

        tvMaliat = findViewById(R.id.textView_maliyat);
        tvMaliatText = findViewById(R.id.textView_maliyat_text);
        tvTakhfif = findViewById(R.id.textView_takhfif);
        tvTakhfifText = findViewById(R.id.textView_takhfif_text);
        tvService = findViewById(R.id.textView_service);
        tvServiceText = findViewById(R.id.textView_service_text);
        tvFactor = findViewById(R.id.textView_factor);
        tvFactorText = findViewById(R.id.textView_factor_text);

        vaziatSefaresh = appPreferenceTools.getVaziatSefaresh();

        ArrayAdapter<String> adapterVaziatSefaresh =
                new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, c.getResources().getStringArray(R.array.vaziat_sefaresh));
        spVaziatSefaresh.setAdapter(adapterVaziatSefaresh);

        spVaziatSefaresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vaziatSefaresh = c.getResources().getStringArray(R.array.vaziat_sefaresh)[i];
                if (i == 2 || i == 3 || i == 4) {
                    text.setVisibility(View.VISIBLE);
                    etTable.setVisibility(View.VISIBLE);
                    textViewAddress.setVisibility(View.GONE);
                    textViewTell.setVisibility(View.GONE);
                } else {
                    text.setVisibility(View.INVISIBLE);
                    etTable.setVisibility(View.INVISIBLE);
                    textViewAddress.setVisibility(View.VISIBLE);
                    textViewTell.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vaziatSefaresh = "بیرون بر";
            }
        });


        if (tableNumber != null) {
            etTable.setText(tableNumber);
            textView.setText(costumerName);
            for (int i = 0; i < c.getResources().getStringArray(R.array.vaziat_sefaresh).length; i++) {
                if (c.getResources().getStringArray(R.array.vaziat_sefaresh)[i].equals(vaziat_sefaresh)) {
                    spVaziatSefaresh.setSelection(i);
                }
            }

            for (int i = 0; i < costumerNamess.size(); i++) {
                if (costumerNamess.get(i).equals(textView.getText().toString())) {
                    if (costumerAddresses.size() == 0) {
                        textViewAddress.setText("0");
                    } else {
                        textViewAddress.setText(costumerAddresses.get(0));
                    }
                    if (costumerTells.size() == 0) {
                        textViewTell.setText("0");
                    } else {
                        textViewTell.setText(costumerTells.get(0));
                    }
                    break;
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

        tvFactor.setText(MainActivity.priceFormatter(mPrice + ""));


        if (appPreferenceTools.getDarsadTakhfif().equals("0.0")) {
            if (appPreferenceTools.getMablaghTakhfif().equals("0")) {
                tvTakhfif.setText("0");
            } else {
                tvTakhfif.setText(Integer.parseInt(appPreferenceTools.getMablaghTakhfif()) + "");
            }
        } else {
            tvTakhfif.setText(((int) Float.parseFloat(appPreferenceTools.getDarsadTakhfif()) * mPrice / 100) + "");
        }

        if (appPreferenceTools.getDarsadService().equals("0.0")) {
            if (appPreferenceTools.getMablaghService().equals("0")) {
                tvService.setText("0");
            } else {
                tvService.setText(appPreferenceTools.getMablaghService());
            }
        } else {
            tvService.setText(((int) Float.parseFloat(appPreferenceTools.getDarsadService()) * (mPrice - (int) Float.parseFloat(tvTakhfif.getText().toString())) / 100) + "");
        }

        if (appPreferenceTools.getDarsadMaliyat().equals("0.0")) {
            tvMaliat.setText("0");
        } else {
            tvMaliat.setText(((int) Float.parseFloat(appPreferenceTools.getDarsadMaliyat()) * (mPrice - (int) Float.parseFloat(tvTakhfif.getText().toString())) / 100) + "");
        }

        tvJameKol.setText(MainActivity.priceFormatter((int) (mPrice - Float.parseFloat(tvTakhfif.getText().toString()) + Float.parseFloat(tvMaliat.getText().toString()) + Float.parseFloat(tvService.getText().toString())) + ""));


    }

    @Override
    public void dismiss() {
        InputMethodManager inputMethodManager = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
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

                if (etTable.getVisibility() == View.VISIBLE && (etTable.getText().toString().equals("") || (Integer.parseInt(etTable.getText() + "") < 1))) {
                    Toast.makeText(c, "شماره میز صحیح نیست.", Toast.LENGTH_SHORT).show();
                } else if (textView.getText().toString().equals("")) {
                    Toast.makeText(c, "نام مشتری را وارد کنید.", Toast.LENGTH_SHORT).show();
                } else {
                    final List<FactorContentModel> factorContentModelList = new ArrayList<>();
                    for (int i = 0; i < FoodOrdersAdapter.mList.size(); i++) {
                        FactorContentModel factorContentModel = new FactorContentModel();

                        factorContentModel.setFoodCode(FoodOrdersAdapter.mList.get(i).getCode());
                        factorContentModel.setFoodCount(FoodOrdersAdapter.mList.get(i).getmNumber());
                        factorContentModel.setSumPriceRow((Integer.parseInt(FoodOrdersAdapter.mList.get(i).getmPrice()) * FoodOrdersAdapter.mList.get(i).getmNumber()) + "");
                        if (etTable.getVisibility() == View.VISIBLE) {
                            factorContentModel.setTableNumber(etTable.getText() + "");
                            factorContentModel.setAddress("0");
                            factorContentModel.setTell("0");
                        } else {
                            factorContentModel.setTableNumber("0");
                            factorContentModel.setAddress(textViewAddress.getText() + "");
                            factorContentModel.setTell(textViewTell.getText() + "");
                        }

                        factorContentModel.setCostumerCode(costumerCode);
                        for (int ii = 0; ii < costumerNamess.size(); ii++) {
                            if (costumerNamess.get(ii).equals(textView.getText().toString())) {
                                factorContentModel.setCostumerCode(costumerTafzilis.get(ii));
                                break;
                            }
                        }

                        factorContentModel.setCostumerName(textView.getText() + "");
                        if (appPreferenceTools.getprintAfterConfirm()) {
                            factorContentModel.setPrintConfirm("true");
                        } else {
                            factorContentModel.setPrintConfirm("false");
                        }
                        factorContentModel.setFoodExp(FoodOrdersAdapter.mList.get(i).getmExp());
                        factorContentModel.setUserId(appPreferenceTools.getUserID() + "");
                        factorContentModel.setPriceSum(mPrice + "");
                        factorContentModel.setVaziatSefaresh(vaziatSefaresh);
                        factorContentModel.setJameMablaghMaliat(tvMaliat.getText().toString());
                        factorContentModel.setJameMablaghKhales((mPrice - Float.parseFloat(tvTakhfif.getText().toString()) + Float.parseFloat(tvMaliat.getText().toString()) +
                                Float.parseFloat(tvService.getText().toString())) + "");
                        factorContentModel.setJameMablaghTakhfif(tvTakhfif.getText().toString());
                        factorContentModel.setJameMablaghServic(tvService.getText().toString());

                        factorContentModelList.add(factorContentModel);
                    }


                    InputMethodManager imm = (InputMethodManager) c.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etTable.getWindowToken(), 0);
                    final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(c);
                    loadingDialogClass.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Call<Object> call = mTService.saveFactor(factorContentModelList, factorNumber, Fish_Number);
                            call.enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {

                                    if (response.isSuccessful()) {
loadingDialogClass.dismiss();
                                        svMain.setVisibility(View.GONE);
                                        rlMain.setVisibility(View.GONE);
                                        rlSuccess.setVisibility(View.VISIBLE);
                                        llSuccess.setVisibility(View.VISIBLE);

                                        tvFishNumber.setText(response.body().toString());
                                        tvNameMoshtari.setText(textView.getText().toString().trim());
                                        FoodOrdersAdapter.mList.clear();

                                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
                                        params.height = 0;
                                        OrdersMenuActivity.ll.setLayoutParams(params);
                                        OrdersMenuActivity.tvTayid.setAlpha(0f);
                                        OrdersMenuActivity.fabToggle.setAlpha(0f);

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
loadingDialogClass.dismiss();
                                        SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
                                        ContentValues cv2 = new ContentValues();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        String myDate = format.format(new Date());
                                        cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response.message());
                                        db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);

                                        Gson gson = new Gson();
                                        String json = gson.toJson(factorContentModelList);
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(MyDatabase.FACTOR_JSON, json);
                                        db2.insert(MyDatabase.OFFLINE_FACTORS_TABLE, null, contentValues);

                                        db2.close();
                                        llLoadingDialog.setVisibility(View.GONE);
                                        tlMain.setAlpha(1f);
                                        Toast.makeText(c, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    loadingDialogClass.dismiss();
                                    SQLiteDatabase db2 = new MyDatabase(c).getWritableDatabase();
                                    ContentValues cv2 = new ContentValues();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                    String myDate = format.format(new Date());
                                    cv2.put(MyDatabase.RESPONCE, myDate + " --> " + t.getMessage());
                                    db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);

                                    Gson gson = new Gson();
                                    String json = gson.toJson(factorContentModelList);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(MyDatabase.FACTOR_JSON, json);
                                    db2.insert(MyDatabase.OFFLINE_FACTORS_TABLE, null, contentValues);

                                    db2.close();
                                    llLoadingDialog.setVisibility(View.GONE);
                                    tlMain.setAlpha(1f);
                                    Toast.makeText(c, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 1234);

                }

                break;
            case R.id.textView_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

}