package com.netreadystaging.godine.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        iniit();
    }

    private void iniit() {
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"fonts/Fenotype - PeachesandCreamBold.ttf");
        setTypeface(typeface);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        iniit();

    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniit();

    }


}
