package co.sansystem.orderingapp;

/**
 * Created by Mohsen on 2017-07-08.
 */

public class OrderedItem {
    public String mName;
    public int mNumber;
    public String  mPrice;
    public String mCode;
    public String mExp;

    OrderedItem(String name,int number,String code,String price,String exp) {
        mNumber = number;
        mName = name;
        mPrice = price;
        mCode = code;
        mExp = exp;
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

    public String getmExp() {
        return mExp;
    }
}
