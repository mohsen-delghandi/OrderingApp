package com.example.mohsen.orderingapp;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.ViewHolder> {

    Context mContext;
    public static ArrayList<String> mFoodsImages,mFoodCodes;
    ArrayList<String> mFoodsNames;
    View v;
    FragmentManager mFragmentManager;
    int mNumber;
    ArrayList<String> mFoodsCategoryCodes;

    public FoodMenuAdapter(Context context, ArrayList<String> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, ArrayList<String> foodCodes) {
        mContext = context;
        mFoodsImages = foodsImages;
        mFoodsNames = foodsNames;
        mFragmentManager = fragmentManager;
//        mFoodsCategoryCodes = foodsCategoryCodes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ImageView iv;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.food_item_textView);
            iv = (ImageView)v.findViewById(R.id.food_item_imageView);
        }
    }

    @Override
    public FoodMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(FoodMenuAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(mFoodsNames.get(position));
        byte[] decodedString = Base64.decode(mFoodsImages.get(position), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.iv.setImageBitmap(decodedByte);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//                FoodOrdersFragment foodOrdersFragment = new FoodOrdersFragment(mContext,mFoodCodes.get(position));
//                fragmentTransaction.replace(R.id.food_orders_fragment,foodOrdersFragment);
//                fragmentTransaction.commit();
                OrderedItem data;

//                OrdersMenuActivity.ll.setVisibility(View.VISIBLE);


                OrdersMenuActivity.fabToggle.setImageResource(R.drawable.icon_up);
                OrdersMenuActivity.fabToggle.setVisibility(View.VISIBLE);

                OrdersMenuActivity.tvTayid.setVisibility(View.VISIBLE);

                if(FoodOrdersAdapter.mList.size() == 0) {
                    ObjectAnimator obj = ObjectAnimator.ofFloat(OrdersMenuActivity.fabToggle, "alpha", 0f, 1f);
                    obj.setDuration(300);
                    obj.start();

                    ObjectAnimator obj2 = ObjectAnimator.ofFloat(OrdersMenuActivity.tvTayid, "alpha", 0f, 1f);
                    obj2.setDuration(300);
                    obj2.start();
                }

                ViewWeightAnimationWrapper animationWrapper = new ViewWeightAnimationWrapper(OrdersMenuActivity.ll);
                ObjectAnimator anim = ObjectAnimator.ofFloat(animationWrapper,
                        "weight",
                        animationWrapper.getWeight(),
                        1f);
                anim.setDuration(300);
                anim.setInterpolator(new FastOutLinearInInterpolator());
                anim.start();
                OrdersMenuActivity.fabToggle.setOnClickListener(OrdersMenuActivity.ocl);


                SQLiteDatabase mydb = new MyDatabase(mContext).getWritableDatabase();
                Cursor cursor = mydb.query(MyDatabase.ORDERS_TABLE,new String[]{MyDatabase.NUMBER},MyDatabase.CODE + " = ?",new String[]{mFoodsCategoryCodes.get(position)+""},null,null,null);
                if (cursor.moveToFirst()){
                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabase.NUMBER,cursor.getInt(0)+1);
                    mNumber = cursor.getInt(0)+1;
                    mydb.update(MyDatabase.ORDERS_TABLE,cv,MyDatabase.CODE + " = ?",new String[]{mFoodCodes.get(position)+""});
                    for(int i = 0;i<FoodOrdersAdapter.mList.size();i++){
                        if(FoodOrdersAdapter.mList.get(i).mName.equals(mFoodsNames.get(position))){
                            FoodOrdersAdapter.mList.remove(i);
                        }
                    }
                    data = new OrderedItem(mFoodsNames.get(position),mNumber);
                }else{
//                    mydb.query(MyDatabase.FOOD_TABLE,new String[]{MyDatabase.NAME},MyDatabase.CODE + " = ?",new String[]{})
                    ContentValues cv2 = new ContentValues();
                    cv2.put(MyDatabase.CODE,mFoodCodes.get(position));
                    cv2.put(MyDatabase.NUMBER,1);
                    mNumber = 1;
                    mydb.insert(MyDatabase.ORDERS_TABLE,null,cv2);
                    data = new OrderedItem(mFoodsNames.get(position),mNumber);
                }
                FoodOrdersAdapter.mList.add(data);
                FoodOrdersFragment.insert(data,mNumber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoodsImages.size();
    }

}

