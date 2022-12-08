package com.jesse205.deskclock.lite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnTouchListener, GestureDetector.OnGestureListener, Runnable, ViewTreeObserver.OnGlobalLayoutListener {
    private boolean lightTheme = false;
    private boolean activityStarted = false;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private GestureDetector mGestureDetector;
    private int lastWeekDay;
    private String lastWeekDayText;
    private String lastTime;
    private String lastDate;

    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;
    private View decorView;
    private FrameLayout mainLayout;
    private TextView timeView;
    private TextView dateView;
    private int screenWidthDp;

    private final Calendar calendar = Calendar.getInstance();

    @SuppressLint({"ClickableViewAccessibility", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lightTheme = sharedPreferences.getBoolean("light_theme", false);
        if (lightTheme)//Light theme
            setTheme(R.style.Theme_DeskClockLite_Light);
        else
            setTheme(R.style.Theme_DeskClockLite);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_main);

        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setIsLongpressEnabled(true);
        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(this);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        timeView = findViewById(R.id.time);
        dateView = findViewById(R.id.date);

        timeFormat = new SimpleDateFormat(getString(R.string.time_template));
        dateFormat = new SimpleDateFormat(getString(R.string.date_template));

        handler = new Handler();


    }

    @Override
    protected void onStop() {
        super.onStop();
        activityStarted = false;
        handler.removeCallbacks(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityStarted = true;
        run();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private float dp2px(int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, getResources().getDisplayMetrics());
    }

    private int px2dp(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value / scale);
    }


    private void hideSystemUI() {

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

    /**
     * @param weekDay Number of the day of the week
     * @return Text of the day of the week
     */
    private String getWeekDayText(int weekDay) {
        int dayNameId = R.string.week_1;//case 1 在这里
        switch (weekDay) {
            case Calendar.MONDAY:
                dayNameId = R.string.week_2;
                break;
            case Calendar.TUESDAY:
                dayNameId = R.string.week_3;
                break;
            case Calendar.WEDNESDAY:
                dayNameId = R.string.week_4;
                break;
            case Calendar.THURSDAY:
                dayNameId = R.string.week_5;
                break;
            case Calendar.FRIDAY:
                dayNameId = R.string.week_6;
                break;
            case Calendar.SATURDAY:
                dayNameId = R.string.week_7;
                break;
        }
        return getString(dayNameId);
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
        if (event.getAction() == MotionEvent.ACTION_UP)
            hideSystemUI();
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void run() {
        if (activityStarted) {
            String time = timeFormat.format(new Date());
            String baseDate = dateFormat.format(new Date());
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (lastWeekDay != weekDay) {
                lastWeekDay = weekDay;
                lastWeekDayText = getWeekDayText(weekDay);
            }
            String date = baseDate + " " + lastWeekDayText;

            if (activityStarted)
                handler.postDelayed(this, 100);

            if (lastTime == null || !lastTime.equals(time)) {
                timeView.setText(time);
                lastTime = time;
            }
            if (lastDate == null || !lastDate.equals(date)) {
                dateView.setText(date);
                lastDate = date;
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        int newScreenWidthDp = px2dp(mainLayout.getMeasuredWidth());
        if (newScreenWidthDp != screenWidthDp) {
            screenWidthDp = newScreenWidthDp;
            int timeSize;
            int dateSize;
            int dateMarginDp;
            if (screenWidthDp >= 1600) {
                timeSize = 360;
                dateSize = 48;
                dateMarginDp = 56;
            } else if (screenWidthDp >= 1280) {
                timeSize = 288;
                dateSize = 34;
                dateMarginDp = 40;
            } else if (screenWidthDp >= 800) {
                timeSize = 192;
                dateSize = 20;
                dateMarginDp = 24;
            } else if (screenWidthDp >= 600) {
                timeSize = 144;
                dateSize = 20;
                dateMarginDp = 24;
            } else if (screenWidthDp >= 400) {
                timeSize = 96;
                dateSize = 18;
                dateMarginDp = 16;
            } else if (screenWidthDp >= 320) {
                timeSize = 80;
                dateSize = 18;
                dateMarginDp = 16;
            } else if (screenWidthDp >= 240) {
                timeSize = 60;
                dateSize = 16;
                dateMarginDp = 16;
            } else {
                timeSize = 44;
                dateSize = 12;
                dateMarginDp = 24;
            }
            int dateMargin = (int) dp2px(dateMarginDp);
            timeView.setTextSize(timeSize);
            dateView.setTextSize(dateSize);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dateView.getLayoutParams();
            layoutParams.setMargins(dateMargin, dateMargin, dateMargin, dateMargin);
            dateView.setLayoutParams(layoutParams);
        }
    }

}