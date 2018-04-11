package co.sansystem.orderingapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sansystem.orderingapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.UI.Fragments.FoodMenuFragment;

/**
 * Created by Mohsen on 2017-06-29.
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    static Context mContext;
    int mWidth;
    static int mHeight;
    ArrayList<byte[]> mFoodsCategoryImages;
    ArrayList<String> mFoodsCategoryNames;
    ArrayList<String> mFoodsCategoryCodes;
    View v;
    static FragmentManager mFragmentManager;
    static DrawerLayout mDrawer;
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
        public TextView tv;
        public ImageView iv;
        LinearLayout llMain;

        public ViewHolder(View v) {
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

            TapTargetView.showFor((Activity)mContext,                 // `this` is an Activity
                    TapTarget.forView(holder.llMain, "لیست های غذایی", "با لمس اینجا می توانید لیست های مختلف غذایی را مشاهده کنید.")
                            .textTypeface(Typeface.createFromAsset(
                                    mContext.getAssets(),
                                    "fonts/IRANSansWeb.ttf"))
                            // All options below are optional
                            .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                            .titleTextSize(25)                  // Specify the size (in sp) of the title text
                            .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                            .textColor(R.color.accent)            // Specify a color for both the title and description text
                            .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(false)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(25),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional

                        }
                    });

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

    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mFileTemp = null;
        String root = mContext.getDir("my_sub_dir", Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        try {
            mFileTemp = File.createTempFile(imageFileName, ".jpg", myDir.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return mFileTemp;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
