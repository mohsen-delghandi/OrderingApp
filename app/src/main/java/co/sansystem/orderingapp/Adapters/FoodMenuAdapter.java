package co.sansystem.orderingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.orderingapp.R;

import java.util.ArrayList;

import co.sansystem.orderingapp.Models.OrderedItemModel;
import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.UI.Fragments.FoodOrdersFragment;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.ViewHolder> {

    private final Context mContext;
    private OrderedItemModel data;
    private static ArrayList<byte[]> mFoodsImages;
    private final ArrayList<String> mFoodsNames;
    private View v;
    private ViewHolder mHolder;
    private final FragmentManager mFragmentManager;
    private int mNumber;
    private final int mHeight;
    private static ArrayList<String> mFoodsCodes;
    private static ArrayList<String> mFoodsPrices;

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
        final TextView tv;
        final ImageView iv;
        final LinearLayout ll;

        ViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.food_item_textView);
            iv = v.findViewById(R.id.food_item_imageView);
            ll = v.findViewById(R.id.linearLayout_cardView);
        }
    }

    @Override
    public FoodMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(final FoodMenuAdapter.ViewHolder holder, final int position) {
        mHolder = holder;
        holder.setIsRecyclable(false);
//        holder.ll.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,holder.tvJameKol.getLayoutParams().width));
        holder.tv.setText(mFoodsNames.get(position));
        SQLiteDatabase dbf2 = new MyDatabase(mContext).getReadableDatabase();
        Cursor cf = dbf2.query(MyDatabase.FOOD_TABLE, new String[]{MyDatabase.FAVORITE}, MyDatabase.CODE + " = ?", new String[]{mFoodsCodes.get(position)}, null, null, null);
        if (cf.moveToFirst()) {
            if (cf.getInt(0) == 1) {
                v.setOnLongClickListener(olclRemoveFavorite);
            } else {
                v.setOnLongClickListener(olclAddFavorite);
            }
        }

        cf.close();
        dbf2.close();
        v.setId(position);

        System.gc();
        try {
            Bitmap decodedByte = BitmapFactory.decodeByteArray(mFoodsImages.get(position), 0, mFoodsImages.get(position).length);
            holder.iv.setImageBitmap(decodedByte);
        } catch (Exception e) {

        }

//        File file = new File(mContext.getFilesDir().getAbsolutePath() + "/kala/" + mFoodsCodes.get(position)+".jpg");
//        Picasso.
//                with(mContext).
//                load(file).
//                into(holder.iv);

        holder.iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                int h = holder.iv.getMeasuredWidth();
                holder.iv.setLayoutParams(new LinearLayout.LayoutParams(h, h));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    holder.iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                OrdersMenuActivity.fabToggle.setImageResource(R.drawable.icon_up);
//                OrdersMenuActivity.fabToggle.setVisibility(View.VISIBLE);

                OrdersMenuActivity.tvTayid.setVisibility(View.VISIBLE);

                if (FoodOrdersAdapter.mList.size() == 0) {

//                    OrdersMenuActivity.fabToggle.setAlpha(1f);
                    OrdersMenuActivity.tvTayid.setAlpha(1f);
                }

                OrdersMenuActivity.ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                if (FoodOrdersAdapter.mList.size() == 0) {
                    data = new OrderedItemModel(mFoodsNames.get(position), 1, mFoodsCodes.get(position), mFoodsPrices.get(position), "");
                }

                for (int i = 0; i < FoodOrdersAdapter.mList.size(); i++) {
                    if (FoodOrdersAdapter.mList.get(i).getCode().equals(mFoodsCodes.get(position))) {
                        data = new OrderedItemModel(mFoodsNames.get(position), FoodOrdersAdapter.mList.get(i).getmNumber() + 1, mFoodsCodes.get(position), mFoodsPrices.get(position), "");
                        FoodOrdersAdapter.mList.remove(i);
                        i = FoodOrdersAdapter.mList.size();
                    } else {
                        data = new OrderedItemModel(mFoodsNames.get(position), 1, mFoodsCodes.get(position), mFoodsPrices.get(position), "");
                    }
                }

                OrdersMenuActivity.frOrders.setVisibility(View.VISIBLE);
                FoodOrdersAdapter.mList.add(data);
                FoodOrdersFragment.insert(data, mNumber);
            }
        });
    }

    private final View.OnLongClickListener olclAddFavorite = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            SQLiteDatabase dbf = new MyDatabase(mContext).getWritableDatabase();
            ContentValues cvf = new ContentValues();
            cvf.put(MyDatabase.FAVORITE, 1);
            dbf.update(MyDatabase.FOOD_TABLE, cvf, MyDatabase.CODE + " = ?", new String[]{mFoodsCodes.get(view.getId())});
            dbf.close();
            Toast.makeText(mContext, "به خواستنی ها افزوده شد.", Toast.LENGTH_SHORT).show();
            view.setOnLongClickListener(olclRemoveFavorite);
            return true;
        }
    };

    private final View.OnLongClickListener olclRemoveFavorite = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (NavigationAdapter.isFavorite) {
//                notifyItemRemoved(view.getId());
//                notifyItemRangeChanged(view.getId(),mFoodsCodes.size());
//                view.setVisibility(View.GONE);
//                onBindViewHolder(mHolder,view.getId());
                NavigationAdapter.refreshFavorites();
            }
            SQLiteDatabase dbf = new MyDatabase(mContext).getWritableDatabase();
            ContentValues cvf = new ContentValues();
            cvf.put(MyDatabase.FAVORITE, 0);
            dbf.update(MyDatabase.FOOD_TABLE, cvf, MyDatabase.CODE + " = ?", new String[]{mFoodsCodes.get(view.getId())});
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

