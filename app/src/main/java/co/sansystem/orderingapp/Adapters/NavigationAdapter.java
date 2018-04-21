package co.sansystem.orderingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import java.util.ArrayList;

import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.UI.Fragments.FoodMenuFragment;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private static Context mContext;
    private final int mWidth;
    private static int mHeight;
    private final ArrayList<byte[]> mFoodsCategoryImages;
    private final ArrayList<String> mFoodsCategoryNames;
    private final ArrayList<String> mFoodsCategoryCodes;
    private View v;
    private static FragmentManager mFragmentManager;
    private static DrawerLayout mDrawer;
    public static boolean isFavorite;

    public NavigationAdapter(Context context, int width, ArrayList<byte[]> foodsImages, ArrayList<String> foodsNames, FragmentManager fragmentManager, DrawerLayout drawer, ArrayList<String> foodCategoryCodes, int height) {
        mContext = context;
        mWidth = width;
        mFoodsCategoryImages = foodsImages;
        mFoodsCategoryNames = foodsNames;
        mFoodsCategoryCodes = foodCategoryCodes;
        mFragmentManager = fragmentManager;
        mDrawer = drawer;
        mHeight = height;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv;
        final ImageView iv;
        final LinearLayout llMain;

        ViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.nav_textView);
            iv = v.findViewById(R.id.nav_imageView);
            llMain = v.findViewById(R.id.linearLayout_main);
        }
    }

    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NavigationAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            holder.tv.setText("محبوب ترین ها");
            holder.setIsRecyclable(false);

            holder.llMain.setTag("myIdea");

            holder.iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {
                    int h = holder.iv.getMeasuredWidth();
                    holder.iv.setLayoutParams(new FrameLayout.LayoutParams(h, h));

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
                    refreshFavorites();
                }
            });
        } else {
            if(position == 1)
                holder.llMain.setTag("myIdea2");
            position--;
            holder.tv.setText(mFoodsCategoryNames.get(position));
            holder.setIsRecyclable(false);

//            Runtime.getRuntime().gc();
            try {
                Bitmap decodedByte = BitmapFactory.decodeByteArray(mFoodsCategoryImages.get(position), 0,
                        mFoodsCategoryImages.get(position).length);
//                decodedByte.compress(Bitmap.CompressFormat.PNG,10,new ByteArrayOutputStream());
                holder.iv.setImageBitmap(decodedByte);
            }catch (Exception e){

            }

            holder.iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {
                    int h = holder.iv.getMeasuredWidth();
                    holder.iv.setLayoutParams(new FrameLayout.LayoutParams(h, h));

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        holder.iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        holder.iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });

//            holder.iv.getLayoutParams().width = mWidth / 5;
//            holder.iv.getLayoutParams().height = mWidth / 5;

            final int finalPosition = position;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        FoodMenuFragment foodMenuFragment = new FoodMenuFragment(mContext, mFragmentManager, mFoodsCategoryCodes.get(finalPosition), mHeight);
                        fragmentTransaction.replace(R.id.food_menu_fragment, foodMenuFragment);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fragmentTransaction.commit();
                        mDrawer.closeDrawer(Gravity.START);
                        isFavorite = false;
                        OrdersMenuActivity.linearLayout.setVisibility(View.GONE);
                    }catch (Exception e){

                    }
                }
            });
        }
    }

    public static void refreshFavorites() {
        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            FoodMenuFragment foodMenuFragment = new FoodMenuFragment(mContext, mFragmentManager, "favorites", mHeight);
//        if(FoodMenuFragment.isFavoriteEmpty){
//            OrdersMenuActivity.linearLayout.setVisibility(View.VISIBLE);
//        }else{
//            OrdersMenuActivity.linearLayout.setVisibility(View.GONE);
//        }
            fragmentTransaction.replace(R.id.food_menu_fragment, foodMenuFragment);
//                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
            mDrawer.closeDrawer(Gravity.START);
            isFavorite = true;
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return mFoodsCategoryImages.size() + 1;
    }
}
