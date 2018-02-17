package co.sansystem.mohsen.orderingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.sansystem.mohsen.orderingapp.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Mohsen on 2017-07-11.
 */

public class MainActivity extends BaseActivity {

    LayoutInflater inflater;
    LinearLayout ns;
    Toolbar toolbar;
    DrawerLayout drawer;
    static int height;
    int width,stopPosition,currentVideo = 0;
    FloatingActionButton fab;
    TextView tvTitlebar;
    ImageView ivTitlebar;
    ActionBarDrawerToggle toggle;
    NavigationView nv;
    String json,json2;
    JSONArray jsonArray,jsonArray2;
    LinearLayout ll_loading;
    long id, id2;
    String title;
    Handler handler;
    Runnable r;
    List<String> videoAddressList = new ArrayList<String >();
    boolean isStarted = false;

//    public void stopHandler() {
//        handler.removeCallbacks(r);
//    }
//    public void startHandler() {
//        handler.postDelayed(r, 15000);
//    }


//    @Override
//    public void onUserInteraction() {
//        // TODO Auto-generated method stub
//        super.onUserInteraction();
//        stopHandler();//stop first and then start
//        startHandler();
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void startVideo(final VideoView videoView, final List<String> videoAddressList, final int i){
        currentVideo = i;
        final Uri video = Uri.parse(videoAddressList.get(i));
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(isStarted){
                    videoView.seekTo(stopPosition);
                    videoView.start();
                }else{
                    videoView.start();
                    isStarted = true;
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                stopPosition = 0;
                if(i == videoAddressList.size()-1){
                    startVideo(videoView,videoAddressList,0);
                }else{
                    startVideo(videoView, videoAddressList, i + 1);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SQLiteDatabase db2 = new MyDatabase(this).getWritableDatabase();
        Cursor cursor = db2.query(MyDatabase.SETTINGS_TABLE,new String[]{MyDatabase.TITLE},null,null,null,null,null);
        cursor.moveToFirst();
        title = cursor.getString(0);
        cursor.close();
        db2.close();

        ll_loading = (LinearLayout)findViewById(R.id.llLoading);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        tvTitlebar = (TextView)findViewById(R.id.titleBar_title);

        ivTitlebar = (ImageView)findViewById(R.id.titleBar_icon);
        ivTitlebar.setVisibility(View.GONE);

        ns = (LinearLayout) findViewById(R.id.nestedscrollview);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Screen Size

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        //Navigation Size

        nv = (NavigationView)findViewById(R.id.nav_view);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) nv.getLayoutParams();
        params.width = width/4;
        nv.setLayoutParams(params);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected View setInflater(Context context, @LayoutRes int resource){

        inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,ns);

        return view;
    }
}
