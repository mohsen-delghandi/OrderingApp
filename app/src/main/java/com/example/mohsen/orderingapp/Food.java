package com.example.mohsen.orderingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Mohsen on 2017-07-02.
 */

public class Food {

    public ArrayList<String> foodCategoryNames = new ArrayList<String>();
    public ArrayList<String> foodCategoryImages = new ArrayList<String>();
    public ArrayList<String> foodCategoryCodes = new ArrayList<String>();

    public Food(Context context) {
        SQLiteDatabase db = new MyDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.FOOD_CATEGORY_TABLE,new String[]{MyDatabase.CODE,MyDatabase.NAME,MyDatabase.IMAGE},null,null,null,null,null);
        cursor.moveToFirst();
        do{
            foodCategoryCodes.add(cursor.getString(0));
            foodCategoryNames.add(cursor.getString(1));
            foodCategoryImages.add(cursor.getString(2));
        }while (cursor.moveToNext());
        cursor.close();
        db.close();
    }


    public ArrayList<String> getFoodCategoryNames() {
        return foodCategoryNames;
    }

    public ArrayList<String> getFoodCategoryImages() {
        return foodCategoryImages;
    }

    public ArrayList<String> getFoodCategoryCodes() {
        return foodCategoryCodes;
    }

//    public static String[] foodCategoryNames = {"کباب","پیتزا","لازانیا","نوشیدنی"};
//    public static int[] foodCategoryImages = {R.drawable.kabab,R.drawable.pizza,R.drawable.lazania,R.drawable.nooshidani};

    public static String[] foodNames = {"کباب قفقازی","کباب کوبیده","کباب بختیاری","کباب برگ","کباب سلطانی","جوجه کباب","پیتزا سبزیجات","پیتزا پپرونی","پیتزا ایتالیایی","پیتزا بادمجان","لازانیا ایتالیایی","لازانیا لقمه ای","لازانیا نخودسبز","لازانیا گیاهی","لازانیا گوشت","لازانیا کدوتنبل","لازانیا ماهی","نوشابه کوکاکولا","نوشابه فانتا","نوشابه پپسی","دلستر استوایی","دوغ","آب معدنی"};
    public static int[] foodImages = {R.drawable.qafqazi_kabab,R.drawable.kubide_kabab,R.drawable.bakhtiari_kabab,R.drawable.barg_kabab,R.drawable.soltani_kabab,R.drawable.juje_kabab,R.drawable.sabzijat_pizza,R.drawable.peperoni_pizza,R.drawable.italia_pizza,R.drawable.bademjan_pizza,R.drawable.italia_lazania,R.drawable.loghme_lazania,R.drawable.nokhodsabz_lazania,R.drawable.giahi_lazania,R.drawable.goosht_lazania,R.drawable.kadutanbal_lazania,R.drawable.mahi_lazania,R.drawable.cocacola_nooshidani,R.drawable.fanta_nooshidani,R.drawable.pepsi_nooshidani,R.drawable.delester_ostova_nooshidani,R.drawable.doogh_nooshidani,R.drawable.ab_nooshidani};


}
