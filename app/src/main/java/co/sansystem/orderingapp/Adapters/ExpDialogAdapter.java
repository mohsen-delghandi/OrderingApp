package co.sansystem.orderingapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

import java.util.ArrayList;

public class ExpDialogAdapter extends RecyclerView.Adapter<ExpDialogAdapter.ExpViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private ArrayList<String> mData;
    private final EditText text;

    public ExpDialogAdapter(Context context, EditText text) {
        Context mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.text = text;
    }

    public void updateAdapterData(ArrayList<String> data) {
        this.mData = data;
    }


    @Override
    public ExpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.exp_item, parent, false);
        return new ExpViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final ExpViewHolder holder, final int position) {
        final ArrayList<String> currentModel = mData;
        if (currentModel.get(position) != null &&
                !currentModel.get(position).equals(null) &&
                !currentModel.get(position).equals("null") &&
                !currentModel.get(position).equals("")) {
            holder.tvExpName.setText(currentModel.get(position));
        }else{
            holder.tvExpName.setVisibility(View.GONE);
        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText().toString() + " " + currentModel.get(position));
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

    public class ExpViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvExpName;
        private final LinearLayout llMain;

        ExpViewHolder(View itemView) {
            super(itemView);
            tvExpName = itemView.findViewById(R.id.textView_exp);
            llMain = itemView.findViewById(R.id.linearLayout_main);
        }
    }
}
