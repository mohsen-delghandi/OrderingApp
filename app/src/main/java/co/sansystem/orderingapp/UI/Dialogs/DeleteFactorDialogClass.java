package co.sansystem.orderingapp.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sansystem.orderingapp.R;

/**
 * Created by Mohsen on 2017-07-15.
 */

public class DeleteFactorDialogClass extends Dialog  {

    public TextView tvFishNumber, tvTableNumber, tvCustomerName, tvFactorTime, tvOk;
    private FrameLayout llMain;
    public ImageView tvNo;
    private final Context context;

    public DeleteFactorDialogClass(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.delete_factor_dialog_layout);

        tvFishNumber = findViewById(R.id.textView_fishNumber);
        tvTableNumber = findViewById(R.id.textView_tableNumber);
        tvCustomerName = findViewById(R.id.textView_customerName);
        tvFactorTime = findViewById(R.id.textView_factorTime);
        tvOk = findViewById(R.id.textView_ok);
        tvNo = findViewById(R.id.textView_cancel);

        llMain = findViewById(R.id.linearLayout_main);

        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }
}