package co.sansystem.orderingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class Food {

    public ArrayList<String> foodCategoryNames = new ArrayList<String>();
    public ArrayList<byte[]> foodCategoryImages = new ArrayList<>();
    public ArrayList<String> foodCategoryCodes = new ArrayList<String>();

    public Food(Context context) {
        SQLiteDatabase db = new MyDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.FOOD_CATEGORY_TABLE,new String[]{MyDatabase.CODE,MyDatabase.NAME,MyDatabase.IMAGE},null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                foodCategoryCodes.add(cursor.getString(0));
                foodCategoryNames.add(cursor.getString(1));
                foodCategoryImages.add(cursor.getBlob(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


    public ArrayList<String> getFoodCategoryNames() {
        return foodCategoryNames;
    }

    public ArrayList<byte[]> getFoodCategoryImages() {
        return foodCategoryImages;
    }

    public ArrayList<String> getFoodCategoryCodes() {
        return foodCategoryCodes;
    }
}
