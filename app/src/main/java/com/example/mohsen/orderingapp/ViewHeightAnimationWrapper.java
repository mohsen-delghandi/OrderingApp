package com.example.mohsen.orderingapp;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Mohsen on 2017-07-15.
 */

public class ViewHeightAnimationWrapper {

    private View view;

    public ViewHeightAnimationWrapper(View view) {
        if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            this.view = view;
        } else {
            throw new IllegalArgumentException("The view should have LinearLayout as parent");
        }
    }

    public void setHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = height;
        view.getParent().requestLayout();
    }

    public int getHeight() {
        return ((LinearLayout.LayoutParams) view.getLayoutParams()).height;
    }
}