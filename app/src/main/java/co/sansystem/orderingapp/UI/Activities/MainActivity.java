package co.sansystem.orderingapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sansystem.orderingapp.R;

import co.sansystem.orderingapp.Utility.Database.MyDatabase;
import co.sansystem.orderingapp.Utility.Utility.BaseActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-07-11.
 */

public class MainActivity extends BaseActivity {

    private LayoutInflater inflater;
    private LinearLayout ns;
    Toolbar toolbar;
    private AppBarLayout appBarLayout;
    DrawerLayout drawer;
    static int height;
    int width;
    private TextView tvTitlebar;
    TextView textView;
    ImageView ivTitlebar;
    private ImageView ivNavBack;
    ActionBarDrawerToggle toggle;
    private NavigationView nv;
    long id, id2;
    String title;
    boolean isStarted = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);

        ViewGroup.MarginLayoutParams paramss = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        paramss.setMargins(0,getStatusBarHeight(),0,0);
        toolbar.setLayoutParams(paramss);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SQLiteDatabase db2 = new MyDatabase(this).getWritableDatabase();
        Cursor cursor = db2.query(MyDatabase.SETTINGS_TABLE,new String[]{MyDatabase.TITLE},null,null,null,null,null);
        cursor.moveToFirst();
        title = cursor.getString(0);
        cursor.close();
        db2.close();

        ivNavBack = findViewById(R.id.imageView_nav_back);


        textView = findViewById(R.id.textView);
        tvTitlebar = findViewById(R.id.titleBar_title);
        tvTitlebar.setText(title);

        ivTitlebar = findViewById(R.id.titleBar_icon);
        ivTitlebar.setVisibility(View.GONE);

        ns = findViewById(R.id.nestedscrollview);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);

                if(SplashActivity.tourNumber == 2) {
                    SplashActivity.tourNumber++;
                    TapTargetView.showFor(MainActivity.this,                 // `this` is an Activity
                                TapTarget.forView(drawerView.findViewWithTag("myIdea"), "محبوب ترین ها", "با لمس اینجا می توانید غذاهای محبوب مشتریان این مجموعه را مشاهده کنید.")
                                    .textTypeface(Typeface.createFromAsset(
                                            getAssets(),
                                            "fonts/IRANSansWeb_Bold.ttf"))
                                    // All options below are optional
                                    .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                                    .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                                    .titleTextSize(25)                  // Specify the size (in sp) of the title text
                                    .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                    .textColor(R.color.accent)            // Specify a color for both the title and description text
                                    .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                    .tintTarget(false)                   // Whether to tint the target view's color
                                    .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                    .targetRadius(70),                  // Specify the target radius (in dp)
                            new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                @Override
                                public void onTargetClick(TapTargetView view) {
                                    super.onTargetClick(view);
                                    if(SplashActivity.tourNumber == 3) {
                                        SplashActivity.tourNumber++;
                                        TapTargetView.showFor(MainActivity.this,                 // `this` is an Activity
                                                TapTarget.forView(drawerView.findViewWithTag("myIdea2"), "گروه های غذایی", "و در ادامه دیگر گروه های غذایی مجموعه را مشاهده می کنید.")
                                                        .textTypeface(Typeface.createFromAsset(
                                                                getAssets(),
                                                                "fonts/IRANSansWeb_Bold.ttf"))
                                                        // All options below are optional
                                                        .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                                                        .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                                                        .titleTextSize(25)                  // Specify the size (in sp) of the title text
                                                        .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                                        .textColor(R.color.accent)            // Specify a color for both the title and description text
                                                        .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                                                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                                                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                                        .tintTarget(false)                   // Whether to tint the target view's color
                                                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                                        .targetRadius(70),                  // Specify the target radius (in dp)
                                                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                                    @Override
                                                    public void onTargetClick(TapTargetView view) {
                                                        super.onTargetClick(view);      // This call is optional
                                                        drawer.closeDrawer(Gravity.START);
                                                        if(SplashActivity.tourNumber == 4) {
                                                            SplashActivity.tourNumber++;
                                                            TapTargetView.showFor(MainActivity.this,                 // `this` is an Activity
                                                                    TapTarget.forView(ivTitlebar, "تنظیمات برنامه", "با لمس اینجا وارد بخش تنظیمات برنامه می شوید.")
                                                                            .textTypeface(Typeface.createFromAsset(
                                                                                    getAssets(),
                                                                                    "fonts/IRANSansWeb_Bold.ttf"))
                                                                            // All options below are optional
                                                                            .outerCircleColor(R.color.primary)      // Specify a color for the outer circle
                                                                            .outerCircleAlpha(0.8f)// Specify the alpha amount for the outer circle
                                                                            .titleTextSize(25)                  // Specify the size (in sp) of the title text
                                                                            .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                                                            .textColor(R.color.accent)            // Specify a color for both the title and description text
                                                                            .dimColor(R.color.primary_text)            // If set, will dim behind the view with 30% opacity of the given color
                                                                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                                                                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                                                            .tintTarget(false)                   // Whether to tint the target view's color
                                                                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                                                            .targetRadius(20),                  // Specify the target radius (in dp)
                                                                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                                                                        @Override
                                                                        public void onTargetClick(TapTargetView view) {
                                                                            super.onTargetClick(view);      // This call is optional
                                                                            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                                                                            startActivity(i);
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });// This call is optional
                                    }
                                }
                            });
                    // Do whatever you want here
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Screen Size

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        //Navigation Size

        nv = findViewById(R.id.nav_view);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nv.getLayoutParams();
        params.width = width;
        nv.setLayoutParams(params);

        ivNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String priceFormatter(String mPrice){
        String mPriceFormatted;
        int a = (mPrice + "").length();
        mPriceFormatted = (mPrice + "").substring(0, a % 3);
        for (int i = 0; i < (mPrice + "").length() / 3; i++) {
            if (!mPriceFormatted.equals("")) mPriceFormatted += ",";
            mPriceFormatted += (mPrice + "").substring(a % 3 + 3 * i, a % 3 + 3 * (i + 1));
        }
        return mPriceFormatted;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    void setInflater(Context context, @LayoutRes int resource){

        inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,ns);
    }
}
