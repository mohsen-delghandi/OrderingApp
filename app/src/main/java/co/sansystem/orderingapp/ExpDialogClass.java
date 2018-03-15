package co.sansystem.orderingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mohsen on 2017-07-15.
 */

public class ExpDialogClass extends Dialog {

    public TextView yes, no;
    EditText text;
    RecyclerView rvExp;
    ExpDialogAdapter mRecyclerAdapter;
    Context context;
    private WebService mTService;

    public ExpDialogClass(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exp_dialog_layout);

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();

        yes = findViewById(R.id.textView_ok);
        no = findViewById(R.id.textView_cancel);
        text = findViewById(R.id.editText_dialog_text);
        rvExp = findViewById(R.id.recyclerView_exp);

        rvExp.setHasFixedSize(true);
        rvExp.setNestedScrollingEnabled(false);

        rvExp.setLayoutManager(new GridLayoutManager(context, 3));

        mRecyclerAdapter = new ExpDialogAdapter(context, text);
        rvExp.setAdapter(mRecyclerAdapter);


        final ArrayList<String> exps = new ArrayList<String>();


        Call<Object> call = mTService.getExpAshpazkhane();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response.body().toString());
                        for (int i = 0; i < jsonArray.length() - 1; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            exps.add(jsonObject.get("Mavad1").toString());
                            exps.add(jsonObject.get("Mavad2").toString());
                            exps.add(jsonObject.get("Mavad3").toString());
                            exps.add(jsonObject.get("Mavad4").toString());
                            exps.add(jsonObject.get("Mavad5").toString());
                        }
                        mRecyclerAdapter.updateAdapterData(exps);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CallWebService cws2 = new CallWebService(context, "GetExpAshpazkhane");
//                String responce2 = cws2.Call();
//                JSONArray jsonArray = null;
//                try {
//                    jsonArray = new JSONArray(responce2);
//                    for (int i = 0; i < jsonArray.length() - 1; i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        exps.add(jsonObject.get("Mavad1").toString());
//                        exps.add(jsonObject.get("Mavad2").toString());
//                        exps.add(jsonObject.get("Mavad3").toString());
//                        exps.add(jsonObject.get("Mavad4").toString());
//                        exps.add(jsonObject.get("Mavad5").toString());
//                    }
//                    mRecyclerAdapter.updateAdapterData(exps);
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mRecyclerAdapter.notifyDataSetChanged();
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}