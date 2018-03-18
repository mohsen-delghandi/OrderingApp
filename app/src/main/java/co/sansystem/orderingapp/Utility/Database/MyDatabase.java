package co.sansystem.orderingapp.Utility.Database;

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
    public static final String SETTINGS_TABLE = "Settings_TBL";
    public static final String RESPONCES_TABLE = "Responces_TBL";
    public static final String VIDEOS_TABLE = "Videos_TBL";

    public static final String ID = "Id";
    public static final String CODE = "Code";
    public static final String CATEGORY_CODE = "CategoryCode";
    public static final String NAME = "Name";
    public static final String IMAGE = "Image";
    public static final String PRICE = "Price";
    public static final String NUMBER = "Number";
    public static final String IP = "Ip";
    public static final String FIRST_RUN = "FirstRun";
    public static final String SYNCED = "Synced";
    public static final String TITLE = "Title";
    public static final String RESPONCE = "Responce";
    public static final String FAVORITE = "Favorite";
    public static final String SENT_TO_SERVER = "SentToServer";

    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + FOOD_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        CODE + " TEXT," +
                        CATEGORY_CODE + " TEXT," +
                        NAME + " TEXT," +
                        FAVORITE + " INT," +
                        IMAGE + " BLOB," +
                        PRICE + " TEXT);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + FOOD_CATEGORY_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        CODE + " TEXT," +
                        NAME + " TEXT," +
                        IMAGE + " BLOB);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + ORDERS_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY," +
                        CODE + " TEXT," +
                        PRICE + " TEXT," +
                        SYNCED + " INTEGER," +
                        NUMBER + " INTEGER);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + SETTINGS_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        FIRST_RUN + " INTEGER," +
                        SENT_TO_SERVER + " INTEGER," +
                        TITLE + " TEXT, " +
                        IP + " TEXT);"
        );
        db.execSQL("INSERT INTO " + SETTINGS_TABLE + " VALUES ( 1 , 1 , 0 , 'عنوان' , '192.168.1.1' );");

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + RESPONCES_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        RESPONCE + " TEXT);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + VIDEOS_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        NAME + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}