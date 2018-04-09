package co.sansystem.orderingapp.UI.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Adapters.ExpDialogAdapter;
import co.sansystem.orderingapp.Models.ExpModel;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Mohsen on 2017-07-15.
 */

public class ExpDialogClass extends Dialog {

    public TextView yes, no;
    public EditText text;
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


        Call<List<ExpModel>> call = mTService.getExpAshpazkhane();
        call.enqueue(new Callback<List<ExpModel>>() {
            @Override
            public void onResponse(Call<List<ExpModel>> call, Response<List<ExpModel>> response) {

                if (response.isSuccessful()) {
                    for (ExpModel expModel :
                            response.body()) {
                        exps.add(expModel.getMavad1());
                        exps.add(expModel.getMavad2());
                        exps.add(expModel.getMavad3());
                        exps.add(expModel.getMavad4());
                        exps.add(expModel.getMavad5());
                    }

                    SQLiteDatabase database = new MyDatabase(context).getWritableDatabase();
                    database.delete(MyDatabase.OFFLINE_EXP,null,null);
                    for(String exp : exps){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MyDatabase.EXP,exp);
                        database.insert(MyDatabase.OFFLINE_EXP,null,contentValues);
                    }
                    database.close();

                    mRecyclerAdapter.updateAdapterData(exps);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                } else {

                    SQLiteDatabase database = new MyDatabase(context).getReadableDatabase();
                    Cursor cursor = database.query(MyDatabase.OFFLINE_EXP,new String[]{MyDatabase.EXP},null,null,null,null,null);
                    if(cursor.moveToFirst()){
                        do{
                            exps.add(cursor.getString(0));
                        }while (cursor.moveToNext());
                        mRecyclerAdapter.updateAdapterData(exps);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        Toast.makeText(context, "ارتباط برقرار نیست.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    database.close();

                }
            }

            @Override
            public void onFailure(Call<List<ExpModel>> call, Throwable t) {
                SQLiteDatabase database = new MyDatabase(context).getReadableDatabase();
                Cursor cursor = database.query(MyDatabase.OFFLINE_EXP,new String[]{MyDatabase.EXP},null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        exps.add(cursor.getString(0));
                    }while (cursor.moveToNext());
                    mRecyclerAdapter.updateAdapterData(exps);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                }else{
                    Toast.makeText(context, "ارتباط برقرار نیست.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                database.close();
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