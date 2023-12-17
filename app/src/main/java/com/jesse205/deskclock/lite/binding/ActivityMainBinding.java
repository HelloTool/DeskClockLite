package com.jesse205.deskclock.lite.binding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jesse205.deskclock.lite.R;

public class ActivityMainBinding {

    public final FrameLayout root;
    public final TextView time;
    public final TextView date;

    public ActivityMainBinding(FrameLayout root) {
        this.root = root;
        time = root.findViewById(R.id.time);
        date = root.findViewById(R.id.date);
    }

    public ActivityMainBinding(LayoutInflater inflater,
                               android.view.ViewGroup root,
                               boolean attachToRoot) {
        this((FrameLayout) inflater.inflate(R.layout.activity_main, root, attachToRoot));
    }

    public ActivityMainBinding(Activity activity) {
        this(activity.getLayoutInflater(), null, false);
    }

}
