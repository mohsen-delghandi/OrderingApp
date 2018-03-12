package co.sansystem.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sansystem.mohsen.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersAdapter extends RecyclerView.Adapter<FoodOrdersAdapter.ViewHolder> {

    Context mContext;
    public static List<OrderedItem> mList = new ArrayList<>();
    RecyclerView mRvv;
    LinearLayoutManager mRvlm;
    ViewHolder mHolder;
    int mPosition, mHeight;
    List<String> exps;


    public FoodOrdersAdapter(Context context, List<OrderedItem> list, RecyclerView rvv, LinearLayoutManager rvlm, int height) {
        mContext = context;
        mList = list;
        mRvv = rvv;
        mRvlm = rvlm;
        mHeight = height;
        exps = new ArrayList<>();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv, tv2;
        public CardView cv;
        public ImageView ivRemove, ivPlus, ivMinus;

        public ViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.food_order_item_name);
            tv2 = v.findViewById(R.id.food_order_item_tedad);
            cv = v.findViewById(R.id.food_item_cardView);
            ivRemove = v.findViewById(R.id.food_order_item_remove);
            ivPlus = v.findViewById(R.id.food_order_item_plus);
            ivMinus = v.findViewById(R.id.food_order_item_minus);

        }
    }

    @Override
    public FoodOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_order_item_layout, parent, false);
        FoodOrdersAdapter.ViewHolder holder = new FoodOrdersAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FoodOrdersAdapter.ViewHolder holder, final int position) {
        mHolder = holder;
        mPosition = position;
        if(exps.size() <= position){
            exps.add("");
        }

        holder.tv.setText(mList.get(position).mName + " " + exps.get(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ExpDialogClass cdd2 = new ExpDialogClass(mContext);
                cdd2.show();
                Window window = cdd2.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cdd2.yes.setText("تایید");
                cdd2.no.setText("انصراف");
                cdd2.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.tv.setText(holder.tv.getText().toString() + " " + cdd2.text.getText().toString());
                        exps.set(position,cdd2.text.getText().toString() + " " + exps.get(position));
                        mList.get(position).mExp = cdd2.text.getText().toString();
                        cdd2.dismiss();
                    }
                });
                cdd2.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cdd2.dismiss();
                    }
                });
            }
        });
        holder.tv2.setText(mList.get(position).mNumber + "");
        mRvv.smoothScrollToPosition(mRvv.getAdapter().getItemCount());


        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeItem(position);
            }

//                long duration = setAnimation(holder.cv,android.R.anim.slide_out_right);

//                SystemClock.sleep(duration);


//                notifyItemRangeChanged(position, mList.size());

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, mList.size());
//                    }
//                }, duration);


//                animate = false;


//                    ObjectAnimator obj2 = ObjectAnimator.ofFloat(OrdersMenuActivity.tvTayid,"alpha",1f,0f);
//                    obj2.setDuration(300);
//                    obj2.start();


//                    final Handler handler3 = new Handler();
//                    handler3.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            OrdersMenuActivity.tvTayid.setVisibility(View.GONE);
//                        }
//                    }, 300);


//                    ObjectAnimator obj = ObjectAnimator.ofFloat(OrdersMenuActivity.fabToggle,"alpha",1f,0f);
//                    obj.setDuration(300);
//                    obj.start();
//
//                    final Handler handler2 = new Handler();
//                    handler2.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            OrdersMenuActivity.fabToggle.setVisibility(View.GONE);
//                        }
//                    }, 300);
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.get(position).mNumber += 1;
                notifyDataSetChanged();
//                animate = false;
            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.get(position).mNumber -= 1;
                if (mList.get(position).mNumber == 0) {
                    removeItem(position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(OrderedItem data) {
//        mList.add(data);
        notifyItemInserted(mPosition);
        notifyDataSetChanged();
//        animate = true;
    }


//    // Remove a RecyclerView item containing a specified Data object
//    public void remove(OrderedItem data) {
//        int position = mList.indexOf(data);
//        mList.remove(position);
//        notifyItemRemoved(position);
//    }

    public void removeItem(int position) {
        if (mList.size() > 0) {
            mList.remove(position);
            exps.remove(position);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());

        if (mList.size() == 0) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(OrdersMenuActivity.ll);
                ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,
                        "height",
                        animationWrapper.getHeight(),
                        0);
                anim.setDuration(300);
                anim.setInterpolator(new FastOutLinearInInterpolator());
                anim.start();
                OrdersMenuActivity.tvTayid.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        OrdersMenuActivity.tvTayid.setVisibility(View.GONE);
                    }
                });

                OrdersMenuActivity.fabToggle.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        OrdersMenuActivity.fabToggle.setVisibility(View.GONE);
                    }
                });
            }else{
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) OrdersMenuActivity.ll.getLayoutParams();
                params.height = 0;
                OrdersMenuActivity.ll.setLayoutParams(params);
                OrdersMenuActivity.fabToggle.setAlpha(0f);
                OrdersMenuActivity.tvTayid.setAlpha(0f);
            }
        }
    }
}


