package com.example.mohsen.orderingapp;

/**
 * Created by Mohsen on 2017-07-08.
 */

public class OrderedItem {
    public String mName;
    public int mNumber;
    public String  mPrice;
    public String mCode;
//    public int mCode;

    OrderedItem(String name,int number,String code,String price/*,int code*/) {
        mNumber = number;
        mName = name;
        mPrice = price;
        mCode = code;
//        mCode = code;
    }

    public String  getCode() {
        return mCode;
    }

    public int getmNumber() {
        return mNumber;
    }

    public String getmPrice() {
        return mPrice;
    }
}
