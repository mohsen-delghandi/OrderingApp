package co.sansystem.orderingapp.Adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sansystem.mohsen.orderingapp.R;

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

        public ViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.nav_textView);
            iv = v.findViewById(R.id.nav_imageView);
        }
    }

    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            holder.tv.setText("محبوب ترین ها");
            holder.setIsRecyclable(false);
            holder.iv.getLayoutParams().width = mWidth / 5;
            holder.iv.getLayoutParams().height = mWidth / 5;

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

            holder.iv.getLayoutParams().width = mWidth / 5;
            holder.iv.getLayoutParams().height = mWidth / 5;

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
