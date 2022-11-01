package com.jesse205.deskclock.lite;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    boolean activitStarted = false;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        TextView timeView = findViewById(R.id.time);
        TextView dateView = findViewById(R.id.date);
        String time_template = getString(R.string.time_template);
        String date_template = getString(R.string.date_template);
        Calendar calendar = Calendar.getInstance();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (activitStarted) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat timeFormat = new SimpleDateFormat(time_template);
                    String time = timeFormat.format(new Date());
                    timeView.setText(time);

                    String date = String.format((String) date_template, getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                    String date2 = dateFormat.format(new Date());
                    dateView.setText(date2);

                    handler.postDelayed(this, 100);
                }
            }
        };

    }

    @Override
    protected void onStop() {
        super.onStop();
        activitStarted = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        activitStarted = true;
        handler.postDelayed(runnable, 0);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private String getWeekDay(int dayNumber) {
        int weekNameId;
        switch (dayNumber) {
            case 1:
                weekNameId = R.string.week_1;
                break;
            case 2:
                weekNameId = R.string.week_2;
                break;
            case 3:
                weekNameId = R.string.week_3;
                break;
            case 4:
                weekNameId = R.string.week_4;
                break;
            case 5:
                weekNameId = R.string.week_5;
                break;
            case 6:
                weekNameId = R.string.week_6;
                break;
            case 7:
                weekNameId = R.string.week_7;
                break;
            default:
                weekNameId = R.string.week_1;
        }
        return getString(weekNameId);
    }
}