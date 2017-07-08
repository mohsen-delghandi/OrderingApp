package com.example.mohsen.orderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohsen on 2017-07-03.
 */

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;

    public static final String FOOD_TABLE = "Food_TBL";
    public static final String FOOD_CATEGORY_TABLE = "Food_Category_TBL";
    public static final String ORDERS_TABLE = "Orders_TBL";

    public static final String ID = "Id";
    public static final String CODE = "Code";
    public static final String CATEGORY_CODE = "CategoryCode";
    public static final String NAME = "Name";
    public static final String IMAGE = "Image";
    public static final String PRICE = "Price";
    public static final String NUMBER = "Number";

    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + FOOD_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        CODE + " INTEGER," +
                        CATEGORY_CODE + " INTEGER," +
                        NAME + " TEXT," +
                        IMAGE + " INTEGER," +
                        PRICE + " INTEGER);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + FOOD_CATEGORY_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        CODE + " INTEGER," +
                        NAME + " TEXT," +
                        IMAGE + " INTEGER);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + ORDERS_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        CODE + " INTEGER," +
                        NUMBER + " INTEGER);"
        );


        int food_category_count = 4;
        ContentValues[] cv = new ContentValues[food_category_count];
        for (int i = 0 ; i < food_category_count ; i++){
            cv[i] = new ContentValues();
            cv[i].put(CODE,"100"+(i+1));
            cv[i].put(NAME,Food.foodCategoryNames[i]);
            cv[i].put(IMAGE,Food.foodCategoryImages[i]);
            db.insert(FOOD_CATEGORY_TABLE,null,cv[i]);
        }

        int food_count = 23;
        ContentValues[] cv2 = new ContentValues[food_count];
        for (int i = 0 ; i < food_count ; i++){
            cv2[i] = new ContentValues();
            if(i >=0 && i < 6){
                cv2[i].put(CODE,"200"+(i+1));
                cv2[i].put(CATEGORY_CODE,"1001");
            }
            if(i >=6 && i < 9){
                cv2[i].put(CODE,"200"+(i+1));
                cv2[i].put(CATEGORY_CODE,"1002");
            }
            if(i >=9 && i < 17){
                cv2[i].put(CODE,"20"+(i+1));
                cv2[i].put(CATEGORY_CODE,"1003");
            }
            if(i >=17 && i < 23){
                cv2[i].put(CODE,"20"+(i+1));
                cv2[i].put(CATEGORY_CODE,"1004");
            }
            cv2[i].put(NAME,Food.foodNames[i]);
            cv2[i].put(IMAGE,Food.foodImages[i]);
            cv2[i].put(PRICE,9999);
            db.insert(FOOD_TABLE,null,cv2[i]);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
