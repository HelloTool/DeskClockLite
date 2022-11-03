package com.jesse205.deskclock.lite;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private boolean lightTheme = false;
    private boolean activityStarted = false;
    private Handler handler;
    private Runnable runnable;
    private SharedPreferences sharedPreferences;
    private GestureDetector mGestureDetector;
    private int lastWeekDay;
    private String lastWeekDayText;
    private String lastTime;
    private String lastDate;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lightTheme = sharedPreferences.getBoolean("light_theme", false);
        if (lightTheme)//亮色主题
            setTheme(R.style.Theme_DeskClockLite_Light);
        else
            setTheme(R.style.Theme_DeskClockLite);

        setContentView(R.layout.activity_main);
        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setIsLongpressEnabled(true);
        FrameLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setFocusable(true);
        //mainLayout.setClickable(true);
        mainLayout.setLongClickable(true);
        mainLayout.setOnTouchListener(this);

        if (Build.VERSION.SDK_INT < 16)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView timeView = findViewById(R.id.time);
        TextView dateView = findViewById(R.id.date);
        String time_template = getString(R.string.time_template);
        String date_template = getString(R.string.date_template);

        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat timeFormat = new SimpleDateFormat(time_template);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(date_template);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (activityStarted) {
                    String time = timeFormat.format(new Date());
                    String baseDate = dateFormat.format(new Date());
                    int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
                    if (lastTime == null || !lastTime.equals(time)) {
                        timeView.setText(time);
                        lastTime = time;
                    }
                    if (lastWeekDay != weekDay) {
                        lastWeekDay = weekDay;
                        lastWeekDayText = getWeekDayText(weekDay);
                    }
                    String date = baseDate + " " + lastWeekDayText;
                    if (lastDate == null || !lastDate.equals(date)) {
                        dateView.setText(date);
                        lastDate = date;
                    }
                    if (activityStarted)
                        handler.postDelayed(this, 100);
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityStarted = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityStarted = true;
        handler.postDelayed(runnable, 0);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                decorView.setSystemUiVisibility(visibility);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    /**
     * @param dayNumber calendar获取的星期
     * @return 星期几的文字
     */
    private String getWeekDayText(int dayNumber) {
        int weekNameId = R.string.week_1;//case 1 在这里
        switch (dayNumber) {
            case Calendar.MONDAY:
                weekNameId = R.string.week_2;
                break;
            case Calendar.TUESDAY:
                weekNameId = R.string.week_3;
                break;
            case Calendar.WEDNESDAY:
                weekNameId = R.string.week_4;
                break;
            case Calendar.THURSDAY:
                weekNameId = R.string.week_5;
                break;
            case Calendar.FRIDAY:
                weekNameId = R.string.week_6;
                break;
            case Calendar.SATURDAY:
                weekNameId = R.string.week_7;
                break;
        }
        return getString(weekNameId);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("light_theme", !lightTheme);
        editor.apply();

        // 重启活动
        startActivity(new Intent(this, this.getClass()));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}