package co.sansystem.mohsen.orderingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    int mPosition,mHeight;

//    boolean animate;


    public FoodOrdersAdapter(Context context, List<OrderedItem> list, RecyclerView rvv, LinearLayoutManager rvlm,int height) {
        mContext = context;
        mList = list;
        mRvv = rvv;
        mRvlm = rvlm;
        mHeight = height;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv, tv2;
        public CardView cv;
        public ImageView ivRemove, ivPlus, ivMinus;

        public ViewHolder(View v) {
            super(v);
            tv = (TextView) v.findViewById(R.id.food_order_item_name);
            tv2 = (TextView) v.findViewById(R.id.food_order_item_tedad);
            cv = (CardView) v.findViewById(R.id.food_item_cardView);
            ivRemove = (ImageView) v.findViewById(R.id.food_order_item_remove);
            ivPlus = (ImageView) v.findViewById(R.id.food_order_item_plus);
            ivMinus = (ImageView) v.findViewById(R.id.food_order_item_minus);

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
//        holder.setIsRecyclable(false);
        holder.tv.setText(mList.get(position).mName);
        holder.tv2.setText(mList.get(position).mNumber + "");
        mRvv.smoothScrollToPosition(mRvv.getAdapter().getItemCount());


        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeItem(position);

//                SQLiteDatabase db = new MyDatabase(mContext).getWritableDatabase();
//                db.delete(MyDatabase.ORDERS_TABLE,MyDatabase.CODE + "= ?",new String[]{mList.get(position).});
//                db.close();

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
//                animate = false;
            }
        });

//        if((position == mList.size()-1) && animate ) {
//            setAnimation(holder.cv, android.R.anim.slide_in_left);
//        }

//        holder.cv.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
//        holder.tv.getParent()..setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
//        notifyItemInserted(position);
//        mRvv.scrollToPosition(position);
//        mRvlm.scrollToPosition(position)

    }


    private long setAnimation(View viewToAnimate, @AnimRes int id) {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position == mList.size()-1)
//        {
        Animation animation = AnimationUtils.loadAnimation(mContext, id);
        long duration = animation.getDuration();
        viewToAnimate.startAnimation(animation);
//        }
        return duration;
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
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());

        if (mList.size() == 0) {

            ViewHeightAnimationWrapper animationWrapper = new ViewHeightAnimationWrapper(OrdersMenuActivity.ll);
            ObjectAnimator anim = ObjectAnimator.ofInt(animationWrapper,
                    "height",
                    animationWrapper.getHeight(),
                    0);
            anim.setDuration(300);
            anim.setInterpolator(new FastOutLinearInInterpolator());
            anim.start();

//            OrdersMenuActivity.ll.animate().y((float)mHeight).setDuration(300).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    OrdersMenuActivity.ll.setVisibility(View.GONE);
//                }
//            });
//
//            ValueAnimator va = ValueAnimator.ofInt(100, 200);
//            va.setDuration(400);
//            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    Integer value = (Integer) animation.getAnimatedValue();
//                    v.getLayoutParams().height = value.intValue();
//                    v.requestLayout();
//                }
//            });
//            va.start();

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
        }

    }
}


