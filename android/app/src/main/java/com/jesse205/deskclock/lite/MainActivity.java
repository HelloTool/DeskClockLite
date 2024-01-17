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
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import com.jesse205.deskclock.lite.binding.ActivityMainBinding;
import com.jesse205.deskclock.lite.util.DisplayUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private final Handler handler = new Handler();
    private ActivityMainBinding binding;
    private boolean lightTheme = false;
    private boolean activityStarted = false;
    private SharedPreferences sharedPreferences;
    private final GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
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

    };
    private GestureDetector mGestureDetector;
    private DateFormat timeFormat;
    private DateFormat dateFormat;

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            long uptime = SystemClock.uptimeMillis();
            updateTime(time);

            if (activityStarted) {
                // 现在启动时间+1s-超出的时间
                handler.postAtTime(this, uptime + 1000 - time % 1000);
            }
        }
    };
    private int screenWidthDp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 切换主题
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lightTheme = sharedPreferences.getBoolean("light_theme", false);
        setTheme(lightTheme ? R.style.Theme_DeskClockLite_Light : R.style.Theme_DeskClockLite);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = new ActivityMainBinding(this);
        setContentView(binding.root);

        mGestureDetector = new GestureDetector(this, onGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        binding.root.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) hideSystemUI();
            return mGestureDetector.onTouchEvent(event);
        });

        timeFormat = new SimpleDateFormat(getString(R.string.time_template), Locale.getDefault());
        dateFormat = new SimpleDateFormat(getString(R.string.date_template), Locale.getDefault());

        onConfigurationChanged(getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final int screenWidth;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            screenWidth = DisplayUtil.dp2px(getResources(), newConfig.screenWidthDp);
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

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Set the content to appear under the system bars so that the
            // content doesn't resize when the system bars hide and show.
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    // Hide the nav bar and status bar
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
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
            if (!binding.time.getText().equals(timeText)) binding.time.setText(timeText);
            if (!binding.date.getText().equals(dateText)) binding.date.setText(dateText);
        }
    }

    private void refreshSize(int newScreenWidth) {
        int newScreenWidthDp = DisplayUtil.px2dp(getResources(), newScreenWidth);
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "refreshSize: newScreenWidth=" + newScreenWidth +
                    "; newScreenWidthDp=" + newScreenWidthDp);
        }
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
                binding.time.setPadding(timePadding, timePadding, timePadding, timePadding);
            } else {
                binding.time.setTextSize((int) (0.225f * screenWidthDp + 0.5f));
            }

            binding.date.setTextSize(dateSize);
            int datePadding = DisplayUtil.dp2px(getResources(), datePaddingDp);
            binding.date.setPadding(datePadding, datePadding, datePadding, datePadding);
        }
    }
}