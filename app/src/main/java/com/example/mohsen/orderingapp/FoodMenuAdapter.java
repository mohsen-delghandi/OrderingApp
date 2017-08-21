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
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.ViewHolder> {

    Context mContext;
    OrderedItem data ;
    public static ArrayList<byte[]> mFoodsImages;
    ArrayList<String> mFoodsNames;
    View v;

    FragmentManager mFragmentManager;
    int mNumber;
    public static ArrayList<String> mFoodsCodes;
    public static ArrayList<String> mFoodsPrices;

    public FoodMenuAdapter(Context context, ArrayList<byte[]> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, ArrayList<String> foodsCodes, ArrayList<String> foodsPrices) {
        mContext = context;
        mFoodsImages = foodsImages;
        mFoodsNames = foodsNames;
        mFragmentManager = fragmentManager;
        mFoodsCodes = foodsCodes;
        mFoodsPrices = foodsPrices;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ImageView iv;
        LinearLayout ll;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.food_item_textView);
            iv = (ImageView)v.findViewById(R.id.food_item_imageView);
            ll = (LinearLayout)v.findViewById(R.id.linearLayout_cardView);
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
        holder.setIsRecyclable(false);
//        holder.ll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,holder.tv.getLayoutParams().width));
        holder.tv.setText(mFoodsNames.get(position));
//        byte[] decodedString = Base64.decode(mFoodsImages.get(position), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(mFoodsImages.get(position), 0, mFoodsImages.get(position).length);
        holder.iv.setImageBitmap(decodedByte);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//                FoodOrdersFragment foodOrdersFragment = new FoodOrdersFragment(mContext,mFoodCodes.get(position));
//                fragmentTransaction.replace(R.id.food_orders_fragment,foodOrdersFragment);
//                fragmentTransaction.commit();


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

                if(FoodOrdersAdapter.mList.size() == 0){
                    data = new OrderedItem(mFoodsNames.get(position),1,mFoodsCodes.get(position),mFoodsPrices.get(position));
                }

                for(int i = 0 ; i < FoodOrdersAdapter.mList.size() ; i++){
                    if(FoodOrdersAdapter.mList.get(i).getCode().equals(mFoodsCodes.get(position))){
                        data = new OrderedItem(mFoodsNames.get(position),FoodOrdersAdapter.mList.get(i).getmNumber()+1,mFoodsCodes.get(position),mFoodsPrices.get(position));
                        FoodOrdersAdapter.mList.remove(i);
                        i = FoodOrdersAdapter.mList.size();
                    }else{
                        data = new OrderedItem(mFoodsNames.get(position),1,mFoodsCodes.get(position),mFoodsPrices.get(position));
                    }
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

