package com.jesse205.deskclock.lite.util;

import android.content.res.Resources;
import android.util.TypedValue;

public class DisplayUtil {
    public static int dp2px(Resources resources, int value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.getDisplayMetrics()) + 0.5f);
    }

    public static int px2dp(Resources resources, float value) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }
}
