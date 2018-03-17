package co.sansystem.orderingapp;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sansystem.mohsen.orderingapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class LastFactorsAdapter extends RecyclerView.Adapter<LastFactorsAdapter.ViewHolder> {
    List<MiniFactorModel> mData;
    private WebService mTService;
    Context context;
    AppPreferenceTools appPreferenceTools;

    public LastFactorsAdapter() {

        WebProvider provider = new WebProvider();
        mTService = provider.getTService();
    }

    public void updateAdapterData(List<MiniFactorModel> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFishNumber,tvTableNumber,tvCustomerName,tvDelete,tvFactorTime;
        public LinearLayout llMain;
        public ViewHolder(View v) {
            super(v);
            tvFishNumber = v.findViewById(R.id.textView_fishNumber);
            tvTableNumber = v.findViewById(R.id.textView_tableNumber);
            tvCustomerName = v.findViewById(R.id.textView_customerName);
            tvFactorTime = v.findViewById(R.id.textView_factorTime);
            tvDelete = v.findViewById(R.id.textView_deleteFactor);

            llMain = v.findViewById(R.id.linearLayout_main);
        }
    }

    @Override
    public LastFactorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        appPreferenceTools = new AppPreferenceTools(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_factors_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final LastFactorsAdapter.ViewHolder holder, final int position) {
        final List<MiniFactorModel> currentModel = mData;
        holder.setIsRecyclable(false);

        holder.tvFishNumber.setText(currentModel.get(position).getFish_Number());
        holder.tvCustomerName.setText(currentModel.get(position).getCustomer_Name());
        holder.tvTableNumber.setText(currentModel.get(position).getTable_Number());
        holder.tvFactorTime.setText(currentModel.get(position).getFactor_Time());

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Boolean> call = mTService.deleteFactor(currentModel.get(position).getFactor_Number(),currentModel.get(position).getSanad_Number(),appPreferenceTools.getUserID());
                call.enqueue(new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        if (response.isSuccessful()) {
                            if(response.body()){
                                Toast.makeText(context, "با موفقیت لغو گردید.", Toast.LENGTH_SHORT).show();
                                currentModel.remove(position);
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "خطا در لغو فاکتور.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<List<FactorModel>> call = mTService.getSefaresh(currentModel.get(position).getFactor_Number());
                call.enqueue(new Callback<List<FactorModel>>() {

                    @Override
                    public void onResponse(Call<List<FactorModel>> call, Response<List<FactorModel>> response) {

                        if (response.isSuccessful()) {

                            FoodOrdersAdapter.mList.clear();
                            FoodOrdersAdapter.mList = new ArrayList<>();
                            for(int i = 0 ; i < response.body().size() ; i++){
                                OrderedItem orderedItem = new OrderedItem(response.body().get(i).getFoodName(),response.body().get(i).getFoodCount(),
                                        response.body().get(i).getFoodCode(),response.body().get(i).getFoodPrice(),response.body().get(i).getFoodExp());
                                FoodOrdersAdapter.mList.add(orderedItem);
                            }

                            Intent intent = new Intent(context,OrdersMenuActivity.class);
                            intent.putExtra("editMode",true);
                            intent.putExtra("tableNumber",currentModel.get(position).getTable_Number());
                            intent.putExtra("Costumer_Name",currentModel.get(position).getCustomer_Name());
                            intent.putExtra("Vaziat_Sefaresh",currentModel.get(position).getVaziat_Sefaresh());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FactorModel>> call, Throwable t) {
                        Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return mData.size();
        } catch (Exception e) {
            return 0;
        }
    }

}

