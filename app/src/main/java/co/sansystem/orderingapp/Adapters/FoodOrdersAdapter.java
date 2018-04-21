package co.sansystem.orderingapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

import co.sansystem.orderingapp.Models.OrderedItemModel;
import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.UI.Dialogs.ExpDialogClass;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class FoodOrdersAdapter extends RecyclerView.Adapter<FoodOrdersAdapter.ViewHolder> {

    private final Context mContext;
    public static List<OrderedItemModel> mList = new ArrayList<>();
    private final RecyclerView mRvv;
    private final LinearLayoutManager mRvlm;
    private ViewHolder mHolder;
    private int mPosition;
    private final int mHeight;
    private final List<String> exps;


    public FoodOrdersAdapter(Context context, List<OrderedItemModel> list, RecyclerView rvv, LinearLayoutManager rvlm, int height) {
        mContext = context;
        mList = list;
        mRvv = rvv;
        mRvlm = rvlm;
        mHeight = height;
        exps = new ArrayList<>();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv;
        final TextView tv2;
        final CardView cv;
        final ImageView ivRemove;
        final ImageView ivPlus;
        final ImageView ivMinus;

        ViewHolder(View v) {
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
        if (exps.size() <= position) {
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
                        exps.set(position, cdd2.text.getText().toString() + " " + exps.get(position));
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
                try {
                    Thread.sleep(650);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
    public void insert(OrderedItemModel data) {
//        mList.add(data);
        notifyItemInserted(mPosition);
        notifyDataSetChanged();
//        animate = true;
    }


    private void removeItem(int position) {
        if (mList.size() > 0) {
            try {
                mList.remove(position);
                exps.remove(position);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());

        if (mList.size() == 0) {

            mContext.startActivity(new Intent(mContext, OrdersMenuActivity.class));
            ((Activity) mContext).finish();
        }
    }
}


