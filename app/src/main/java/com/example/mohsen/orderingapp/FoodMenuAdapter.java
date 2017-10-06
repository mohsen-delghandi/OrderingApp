package com.example.mohsen.orderingapp;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.Toast;

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
    ViewHolder mHolder;
    FragmentManager mFragmentManager;
    int mNumber,mHeight;
    public static ArrayList<String> mFoodsCodes;
    public static ArrayList<String> mFoodsPrices;

    public FoodMenuAdapter(Context context, ArrayList<byte[]> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, ArrayList<String> foodsCodes, ArrayList<String> foodsPrices, int height) {
        mContext = context;
        mFoodsImages = foodsImages;
        mFoodsNames = foodsNames;
        mFragmentManager = fragmentManager;
        mFoodsCodes = foodsCodes;
        mFoodsPrices = foodsPrices;
        mHeight = height;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv,tvFavorite;
        public ImageView iv;
        LinearLayout ll;
        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.food_item_textView);
            tvFavorite = (TextView)v.findViewById(R.id.textView_favorite);
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
        mHolder = holder;
        holder.setIsRecyclable(false);
//        holder.ll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,holder.tv.getLayoutParams().width));
        holder.tv.setText(mFoodsNames.get(position));
        SQLiteDatabase dbf2 = new MyDatabase(mContext).getReadableDatabase();
        Cursor cf = dbf2.query(MyDatabase.FOOD_TABLE,new String[]{MyDatabase.FAVORITE},MyDatabase.CODE + " = ?",new String[]{mFoodsCodes.get(position)},null,null,null);
        if (cf.moveToFirst()){
            if(cf.getInt(0)==1){
                holder.tvFavorite.setVisibility(View.VISIBLE);
                v.setOnLongClickListener(olclRemoveFavorite);
            }else{
                holder.tvFavorite.setVisibility(View.INVISIBLE);
                v.setOnLongClickListener(olclAddFavorite);
            }
        }

        v.setId(position);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(mFoodsImages.get(position), 0, mFoodsImages.get(position).length);
        holder.iv.setImageBitmap(decodedByte);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(OrdersMenuActivity.ll);
                ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,
                        "height",
                        animationWrapper.getHeight(),
                        mHeight/3);
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

    View.OnLongClickListener olclAddFavorite = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            view.findViewById(R.id.textView_favorite).setVisibility(View.VISIBLE);
            SQLiteDatabase dbf = new MyDatabase(mContext).getWritableDatabase();
            ContentValues cvf = new ContentValues();
            cvf.put(MyDatabase.FAVORITE,1);
            dbf.update(MyDatabase.FOOD_TABLE,cvf,MyDatabase.CODE + " = ?",new String[]{mFoodsCodes.get(view.getId())});
            dbf.close();
            Toast.makeText(mContext, "به خواستنی ها افزوده شد.", Toast.LENGTH_SHORT).show();
            view.setOnLongClickListener(olclRemoveFavorite);
            return true;
        }
    };

    View.OnLongClickListener olclRemoveFavorite = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if(NavigationAdapter.isFavorite){
//                notifyItemRemoved(view.getId());
//                notifyItemRangeChanged(view.getId(),mFoodsCodes.size());
//                view.setVisibility(View.GONE);
//                onBindViewHolder(mHolder,view.getId());
                NavigationAdapter.refreshFavorites();
            }
            view.findViewById(R.id.textView_favorite).setVisibility(View.INVISIBLE);
            SQLiteDatabase dbf = new MyDatabase(mContext).getWritableDatabase();
            ContentValues cvf = new ContentValues();
            cvf.put(MyDatabase.FAVORITE,0);
            dbf.update(MyDatabase.FOOD_TABLE,cvf,MyDatabase.CODE + " = ?",new String[]{mFoodsCodes.get(view.getId())});
            dbf.close();
            Toast.makeText(mContext, "از خواستنی ها حذف شد.", Toast.LENGTH_SHORT).show();
            view.setOnLongClickListener(olclAddFavorite);
            return true;
        }
    };

    @Override
    public int getItemCount() {
        return mFoodsImages.size();
    }

}

