package co.sansystem.orderingapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.Extension;
import com.sansystem.orderingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.sansystem.orderingapp.Models.FactorContentModel;
import co.sansystem.orderingapp.Models.MiniFactorModel;
import co.sansystem.orderingapp.Models.OrderedItemModel;
import co.sansystem.orderingapp.UI.Activities.OrdersMenuActivity;
import co.sansystem.orderingapp.UI.Dialogs.DeleteFactorDialogClass;
import co.sansystem.orderingapp.UI.Dialogs.LoadingDialogClass;
import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Network.WebProvider;
import co.sansystem.orderingapp.Utility.Network.WebService;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
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

    public void deleteAdapterData(final int position) {

        final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(context);
        loadingDialogClass.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> call = mTService.deleteFactor(mData.get(position).getFactor_Number(), mData.get(position).getSanad_Number(), appPreferenceTools.getUserID());
                call.enqueue(new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        if (response.isSuccessful()) {
                            loadingDialogClass.dismiss();
                            if (response.body()) {
                                Toast.makeText(context, "با موفقیت لغو گردید.", Toast.LENGTH_SHORT).show();
                                mData.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "خطا در لغو فاکتور.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loadingDialogClass.dismiss();
                            SQLiteDatabase db2 = new MyDatabase(context).getWritableDatabase();
                            ContentValues cv2 = new ContentValues();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            String myDate = format.format(new Date());
                            cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response.message());
                            db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                            db2.close();
                            Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        loadingDialogClass.dismiss();
                        SQLiteDatabase db2 = new MyDatabase(context).getWritableDatabase();
                        ContentValues cv2 = new ContentValues();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String myDate = format.format(new Date());
                        cv2.put(MyDatabase.RESPONCE, myDate + " --> " + t.getMessage());
                        db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                        db2.close();
                        Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 1234);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFishNumber, tvTableNumber, tvCustomerName, tvFactorTime, mActionViewDelete;
        public FrameLayout llMain;
        View mActionContainer;
        View mViewContent;

        public ViewHolder(View v) {
            super(v);
            tvFishNumber = v.findViewById(R.id.textView_fishNumber);
            tvTableNumber = v.findViewById(R.id.textView_tableNumber);
            tvCustomerName = v.findViewById(R.id.textView_customerName);
            tvFactorTime = v.findViewById(R.id.textView_factorTime);
            mActionContainer = v.findViewById(R.id.view_list_repo_action_container);
            mViewContent = v.findViewById(R.id.view_list_main_content);
            mActionViewDelete = v.findViewById(R.id.view_list_repo_action_delete);

            llMain = v.findViewById(R.id.linearLayout_main);
        }
    }

    @Override
    public LastFactorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        appPreferenceTools = new AppPreferenceTools(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_factors_item, parent, false);
        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final LastFactorsAdapter.ViewHolder holder, final int position) {
        final List<MiniFactorModel> currentModel = mData;
        holder.setIsRecyclable(false);

        holder.tvFishNumber.setText(currentModel.get(position).getFish_Number());
        holder.tvCustomerName.setText(currentModel.get(position).getCustomer_Name());
        holder.tvTableNumber.setText(currentModel.get(position).getTable_Number());
        if (currentModel.get(position).getFactor_Time().contains("AM"))
            holder.tvFactorTime.setText(currentModel.get(position).getFactor_Time().replace("AM", "ق.ظ"));
        else if (currentModel.get(position).getFactor_Time().contains("PM"))
            holder.tvFactorTime.setText(currentModel.get(position).getFactor_Time().replace("PM", "ب.ظ"));
        else
            holder.tvFactorTime.setText(currentModel.get(position).getFactor_Time());

        holder.mActionViewDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DeleteFactorDialogClass deleteFactorDialogClass = new DeleteFactorDialogClass(context);
                        deleteFactorDialogClass.show();
                        Window window = deleteFactorDialogClass.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        holder.tvFishNumber.setText(currentModel.get(position).getFish_Number());
                        deleteFactorDialogClass.tvFishNumber.setText(currentModel.get(position).getFish_Number());
                        deleteFactorDialogClass.tvCustomerName.setText(currentModel.get(position).getCustomer_Name());
                        deleteFactorDialogClass.tvTableNumber.setText(currentModel.get(position).getTable_Number());
                        if (currentModel.get(position).getFactor_Time().contains("AM"))
                            deleteFactorDialogClass.tvFactorTime.setText(currentModel.get(position).getFactor_Time().replace("AM", "ق.ظ"));
                        else if (currentModel.get(position).getFactor_Time().contains("PM"))
                            deleteFactorDialogClass.tvFactorTime.setText(currentModel.get(position).getFactor_Time().replace("PM", "ب.ظ"));
                        else
                            deleteFactorDialogClass.tvFactorTime.setText(currentModel.get(position).getFactor_Time());
                        deleteFactorDialogClass.tvOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteAdapterData(position);
                                deleteFactorDialogClass.dismiss();
                            }
                        });
                        deleteFactorDialogClass.tvNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteFactorDialogClass.dismiss();
                            }
                        });
                    }
                }

        );

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LoadingDialogClass loadingDialogClass = new LoadingDialogClass(context);
                loadingDialogClass.show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<List<FactorContentModel>> call = mTService.getSefaresh(currentModel.get(position).getFactor_Number());
                        call.enqueue(new Callback<List<FactorContentModel>>() {

                            @Override
                            public void onResponse(Call<List<FactorContentModel>> call, Response<List<FactorContentModel>> response) {

                                if (response.isSuccessful()) {

                                    loadingDialogClass.dismiss();

                                    FoodOrdersAdapter.mList.clear();
                                    FoodOrdersAdapter.mList = new ArrayList<>();
                                    for (int i = 0; i < response.body().size(); i++) {
                                        OrderedItemModel orderedItem = new OrderedItemModel(response.body().get(i).getFoodName(), response.body().get(i).getFoodCount(),
                                                response.body().get(i).getFoodCode(), response.body().get(i).getFoodPrice(), response.body().get(i).getFoodExp());
                                        FoodOrdersAdapter.mList.add(orderedItem);
                                    }

                                    Intent intent = new Intent(context, OrdersMenuActivity.class);
                                    intent.putExtra("editMode", true);
                                    intent.putExtra("tableNumber", currentModel.get(position).getTable_Number());
                                    intent.putExtra("Costumer_Name", currentModel.get(position).getCustomer_Name());
                                    intent.putExtra("Vaziat_Sefaresh", currentModel.get(position).getVaziat_Sefaresh());
                                    intent.putExtra("Factor_Number", currentModel.get(position).getFactor_Number());
                                    intent.putExtra("Fish_Number", currentModel.get(position).getFish_Number());
                                    context.startActivity(intent);
                                } else {
                                    loadingDialogClass.dismiss();
                                    SQLiteDatabase db2 = new MyDatabase(context).getWritableDatabase();
                                    ContentValues cv2 = new ContentValues();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                    String myDate = format.format(new Date());
                                    cv2.put(MyDatabase.RESPONCE, myDate + " --> " + response.message());
                                    db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                                    db2.close();
                                    Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<FactorContentModel>> call, Throwable t) {
                                loadingDialogClass.dismiss();
                                SQLiteDatabase db2 = new MyDatabase(context).getWritableDatabase();
                                ContentValues cv2 = new ContentValues();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                String myDate = format.format(new Date());
                                cv2.put(MyDatabase.RESPONCE, myDate + " --> " + t.getMessage());
                                db2.insert(MyDatabase.RESPONCES_TABLE, null, cv2);
                                db2.close();
                                Toast.makeText(context, "عدم ارتباط با سرور،لطفا دوباره تلاش کنید.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, 1234);


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


    class ItemViewHolderWithRecyclerWidth extends ViewHolder {

        View mActionViewDelete;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ViewHolder implements Extension {

        View mActionViewDelete;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

}

