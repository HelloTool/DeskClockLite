package com.jesse205.deskclock.lite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends Activity {
//    private final String TAG = "MainActivity";
    private boolean lightTheme = false;
    private boolean activityStarted = false;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private GestureDetector mGestureDetector;


    private DateFormat timeFormat;
    private DateFormat dateFormat;
    private TextView timeView;
    private TextView dateView;
    private int screenWidthDp;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //切换主题
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lightTheme = sharedPreferences.getBoolean("light_theme", false);
        setTheme(lightTheme ? R.style.Theme_DeskClockLite_Light : R.style.Theme_DeskClockLite);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mGestureDetector = new GestureDetector(onGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);

        FrameLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) hideSystemUI();
            return mGestureDetector.onTouchEvent(event);
        });

        timeView = findViewById(R.id.time);
        dateView = findViewById(R.id.date);

        timeFormat = new SimpleDateFormat(getString(R.string.time_template), Locale.getDefault());
        dateFormat = new SimpleDateFormat(getString(R.string.date_template), Locale.getDefault());

        //用于延时的Handler
        handler = new Handler();

        onConfigurationChanged(getResources().getConfiguration());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final int screenWidth;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            screenWidth = dp2px(newConfig.screenWidthDp);
        } else {
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            screenWidth = defaultDisplay.getWidth();
        }
        refreshSize(screenWidth);
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityStarted = false;
        handler.removeCallbacks(tickRunnable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityStarted = true;
        tickRunnable.run();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private int dp2px(int value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()) + 0.5f);
    }

    private int px2dp(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }


    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Set the content to appear under the system bars so that the
            // content doesn't resize when the system bars hide and show.
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
    }

    private void updateTime(long time) {
        if (activityStarted) {
            String timeText = timeFormat.format(time);
            String dateText = dateFormat.format(time);
            if (!timeView.getText().equals(timeText)) timeView.setText(timeText);
            if (!dateView.getText().equals(dateText)) dateView.setText(dateText);
        }
    }

    private void refreshSize(int newScreenWidth) {
        int newScreenWidthDp = px2dp(newScreenWidth);
        /*Log.i(TAG, "refreshSize: newScreenWidth=" + newScreenWidth +
                "; newScreenWidthDp=" + newScreenWidthDp);*/
        if (newScreenWidthDp != screenWidthDp) {
            screenWidthDp = newScreenWidthDp;
            int dateSize;
            int datePaddingDp;
            if (screenWidthDp >= 1600) {
                dateSize = 48;
                datePaddingDp = 56;
            } else if (screenWidthDp >= 1280) {
                dateSize = 34;
                datePaddingDp = 40;
            } else if (screenWidthDp >= 800) {
                dateSize = 20;
                datePaddingDp = 24;
            } else if (screenWidthDp >= 600) {
                dateSize = 20;
                datePaddingDp = 24;
            } else if (screenWidthDp >= 400) {
                dateSize = 18;
                datePaddingDp = 16;
            } else if (screenWidthDp >= 320) {
                dateSize = 18;
                datePaddingDp = 16;
            } else if (screenWidthDp >= 240) {
                dateSize = 16;
                datePaddingDp = 16;
            } else {
                dateSize = 12;
                datePaddingDp = 24;
            }

            // Android O 及以上是自动缩放字体，因此需要设置边距。
            // Android O 以下为公式计算
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int timePadding = (int) (newScreenWidth * 0.1);
                timeView.setPadding(timePadding, timePadding, timePadding, timePadding);
            } else timeView.setTextSize((int) (0.225f * screenWidthDp + 0.5f));

            dateView.setTextSize(dateSize);
            int datePadding = dp2px(datePaddingDp);
            dateView.setPadding(datePadding, datePadding, datePadding, datePadding);
        }
    }

    private final GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) editor.apply();
            else editor.commit();

            // 重启活动
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            long uptime = SystemClock.uptimeMillis();
            updateTime(time);

            if (activityStarted)
                // 现在启动时间+1s-超出的时间
                handler.postAtTime(this, uptime + 1000 - time % 1000);
        }
    };
}