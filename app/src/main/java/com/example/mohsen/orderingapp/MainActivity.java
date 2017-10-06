package com.example.mohsen.orderingapp;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.backtory.java.internal.BacktoryCallBack;
import com.backtory.java.internal.BacktoryObject;
import com.backtory.java.internal.BacktoryQuery;
import com.backtory.java.internal.BacktoryResponse;
import com.backtory.java.internal.BacktoryUser;
import com.backtory.java.internal.LoginResponse;
import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;

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

    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 30000);
    }


    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }

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

//        final List<String> backtoryVideoNames = new ArrayList<String>();
//
//        BacktoryUser.loginAsGuestInBackground(new BacktoryCallBack<LoginResponse>() {
//
//            // Login done (fail or success), checking for result
//            @Override
//            public void onResponse(BacktoryResponse<LoginResponse> response) {
//                // Checking if operation was successful
//                if (response.isSuccessful()) {
//                    // Getting new username and password from CURRENT user
//                    String newUsername = BacktoryUser.getCurrentUser().getUsername();
//                    String newPassword = BacktoryUser.getCurrentUser().getGuestPassword();
//
//                    Toast.makeText(MainActivity.this, "login ok", Toast.LENGTH_SHORT).show();
//
//                    BacktoryQuery nameQuery = new BacktoryQuery("videos");
//                    nameQuery.whereExists("name");
//                    nameQuery.findInBackground(new BacktoryCallBack<List<BacktoryObject>>() {
//                        @Override
//                        public void onResponse(BacktoryResponse<List<BacktoryObject>> backtoryResponse) {
//                            if(backtoryResponse.isSuccessful()){
//                                List<BacktoryObject> videoNames = backtoryResponse.body();
//                                SQLiteDatabase dbVideos = new MyDatabase(MainActivity.this).getWritableDatabase();
//                                Cursor cursorVideos = dbVideos.query(MyDatabase.VIDEOS_TABLE,new String[]{MyDatabase.NAME},null,null,null,null,null,null);
//                                for(BacktoryObject names : videoNames){
////                                    backtoryVideoNames.add(names.getString("name"));
//                                    boolean existed = false;
//                                    if(cursorVideos.moveToFirst()){
//                                        do{
//                                            if(names.getString("name").trim().equals(cursorVideos.getString(0).trim())){
//                                                existed = true;
//                                            }
//                                        }while (cursorVideos.moveToNext());
//                                    }
//                                    if(!existed){
//                                        new DownloadTask(MainActivity.this,"https://storage.backtory.com/ordering-app-video/" + names.getString("name"));
//                                        ContentValues cv = new ContentValues();
//                                        cv.put(MyDatabase.NAME,names.getString("name"));
//                                        dbVideos.insert(MyDatabase.VIDEOS_TABLE,null,cv);
//                                    }
//                                }
//                                Toast.makeText(MainActivity.this, "database successful", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(MainActivity.this, "database not successful", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    // Logging new username and password
//                    Log.d(TAG, "your guest username: " + newUsername
//                            + " & your guest password: " + newPassword);
//                } else {
//                    // Operation generally failed, maybe internet connection issue
//                    Log.d(TAG, "Login failed for other reasons like network issues");
//                    Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        List<String> localVideoNames = new ArrayList<String>();
//
//        SQLiteDatabase dbVideos = new MyDatabase(this).getWritableDatabase();
//        Cursor cursorVideos = dbVideos.query(MyDatabase.VIDEOS_TABLE,new String[]{MyDatabase.NAME},null,null,null,null,null,null);
//        if(cursorVideos.moveToFirst()){
//            do{
//                localVideoNames.add(cursorVideos.getString(0));
//            }while (cursorVideos.moveToNext());
//        }else{
//            if(backtoryVideoNames.size()>0){
//                ContentValues cvVideos = new ContentValues();
//                for(int i = 0 ; i < backtoryVideoNames.size() ; i++){
//                    new DownloadTask(this,"https://storage.backtory.com/ordering-app-video/" + backtoryVideoNames.get(i));
//                    cvVideos.put(MyDatabase.NAME,backtoryVideoNames.get(i));
//                }
//                dbVideos.insert(MyDatabase.VIDEOS_TABLE,null,cvVideos);
//            }
//        }












//        new DownloadTask(this,"https://storage.backtory.com/ordering-app-video/video1.mp4");
//        new DownloadTask(this,"https://storage.backtory.com/ordering-app-video/video2.mp4");
//        new DownloadTask(this,"https://storage.backtory.com/ordering-app-video/video3.mp4");
//        new DownloadTask(this,"https://storage.backtory.com/ordering-app-video/video4.mp4");
//        new DownloadTask(this,"https://storage.backtory.com/ordering-app-video/video5.mp4");

        videoAddressList.add(Environment.getExternalStorageDirectory() + "/orderingappvideos/video1.mp4");
        videoAddressList.add(Environment.getExternalStorageDirectory() + "/orderingappvideos/video2.mp4");
        videoAddressList.add(Environment.getExternalStorageDirectory() + "/orderingappvideos/video3.mp4");
        videoAddressList.add(Environment.getExternalStorageDirectory() + "/orderingappvideos/video4.mp4");
        videoAddressList.add(Environment.getExternalStorageDirectory() + "/orderingappvideos/video5.mp4");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        final ImageView ivScreenSaver = (ImageView) findViewById(R.id.imageView_screenSaver);
        final VideoView vvScreenSaver = (VideoView) findViewById(R.id.videoView);
        final View include = findViewById(R.id.include);
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout_screenSaver);
        final NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        Button btScreenSaver = (Button)findViewById(R.id.button_screenSaver);



//        Glide.with(this).load(R.drawable.ads).into(ivScreenSaver);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btScreenSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                include.setVisibility(View.VISIBLE);
                navigationView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                vvScreenSaver.stopPlayback();
                stopPosition = vvScreenSaver.getCurrentPosition();
                vvScreenSaver.stopPlayback();
                startHandler();
            }
        });

        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                include.setVisibility(View.GONE);
                navigationView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                Uri video = Uri.parse(Environment.getExternalStorageDirectory() + "/orderingappvideos/video1.mp4");
//                vvScreenSaver.setVideoURI(video);
//                vvScreenSaver.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
////                        mp.setLooping(true);
//                        vvScreenSaver.start();
//                    }
//                });


                startVideo(vvScreenSaver,videoAddressList,currentVideo);



//                vvScreenSaver.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        startVideo(vvScreenSaver,"https://storage.backtory.com/ordering-app-video/video2.mp4");
//                    }
//                });
//                vvScreenSaver.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        startVideo(vvScreenSaver,"https://storage.backtory.com/ordering-app-video/video3.mp4");
//                    }
//                });
                stopHandler();
            }
        };
        startHandler();

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

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,ns);

        return view;
    }
}
