package co.sansystem.orderingapp.Utility.Font;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.sansystem.orderingapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mohsen on 2017-04-30.
 */

public class Font extends Application {

    public static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansWeb_Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        context = getApplicationContext();
    }
}
