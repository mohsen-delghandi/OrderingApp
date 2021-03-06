package co.sansystem.orderingapp.UI.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sansystem.orderingapp.R;

import java.util.ArrayList;

import co.sansystem.orderingapp.Adapters.FoodMenuAdapter;
import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;

/**
 * Created by Mohsen on 2017-07-01.
 */

public class FoodMenuFragment extends Fragment {

    private RecyclerView rvv;
    private RecyclerView.LayoutManager rvlm;
    private RecyclerView.Adapter rva;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mFoodsCategoryCode;
    private int mHeight;
    private static boolean isFavoriteEmpty = true;

    public FoodMenuFragment(Context context, FragmentManager fragmentManager, String foodsCategoryCode,int height) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mHeight = height;
        if(foodsCategoryCode.trim().equals("favorites")){
            mFoodsCategoryCode = -1;
        }else{
            mFoodsCategoryCode = Integer.parseInt(foodsCategoryCode);
        }
    }

    public FoodMenuFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.food_menu_layout, container, false);
        rvv = v.findViewById(R.id.food_menu_recyclerView);

        rvv.setHasFixedSize(true);
        rvv.setNestedScrollingEnabled(false);

        rvlm = new GridLayoutManager(mContext,3);
        rvv.setLayoutManager(rvlm);

        ArrayList<String> foodsNames = new ArrayList<>();
        ArrayList<byte[]> foodsImages = new ArrayList<>();
        ArrayList<String> foodsCodes = new ArrayList<>();
        ArrayList<String> foodsPrices = new ArrayList<>();
        SQLiteDatabase mydb = new MyDatabase(getActivity()).getWritableDatabase();
        Cursor cur;

        if(mFoodsCategoryCode == -1){
            cur = mydb.query(MyDatabase.FOOD_TABLE, new String[]{MyDatabase.NAME, MyDatabase.IMAGE, MyDatabase.CODE, MyDatabase.PRICE}, MyDatabase.FAVORITE + " = ?", new String[]{"1"}, null, null, null);

        }else {
            cur = mydb.query(MyDatabase.FOOD_TABLE, new String[]{MyDatabase.NAME, MyDatabase.IMAGE, MyDatabase.CODE, MyDatabase.PRICE}, MyDatabase.CATEGORY_CODE + " = ?", new String[]{mFoodsCategoryCode + ""}, null, null, null);
        }
        if (cur.moveToFirst()) {
            if(mFoodsCategoryCode == -1){
                isFavoriteEmpty = false;
                OrdersMenuActivity.linearLayout.setVisibility(View.GONE);
            }
            do {
                foodsNames.add(cur.getString(0));
                foodsImages.add(cur.getBlob(1));
                foodsCodes.add(cur.getString(2));
                foodsPrices.add(cur.getString(3));
            } while (cur.moveToNext());
        }else{
            if(mFoodsCategoryCode == -1){
                isFavoriteEmpty = true;
                OrdersMenuActivity.linearLayout.setVisibility(View.VISIBLE);
            }
        }
        cur.close();
        mydb.close();
        rva = new FoodMenuAdapter(mContext,foodsImages,foodsNames,mFragmentManager,foodsCodes,foodsPrices,mHeight);

        rvv.setAdapter(rva);
        return v;
    }
}
