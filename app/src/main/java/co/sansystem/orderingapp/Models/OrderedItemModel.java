package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2017-07-08.
 */

public class OrderedItemModel {
    public final String mName;
    public int mNumber;
    private final String  mPrice;
    private final String mCode;
    public String mExp;

    public OrderedItemModel(String name, int number, String code, String price, String exp) {
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
