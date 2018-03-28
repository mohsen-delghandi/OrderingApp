package co.sansystem.orderingapp.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sansystem.orderingapp.R;


/**
 * Created by Mohsen on 2017-07-15.
 */

public class LoadingDialogClass extends Dialog {

    public LoadingDialogClass(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog_layout);

        this.setCancelable(false);
        Window window = this.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.85f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        window.setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}