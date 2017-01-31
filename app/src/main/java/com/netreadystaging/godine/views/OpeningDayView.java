package com.netreadystaging.godine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;

/**
 * Created by lenovo on 1/30/2017.
 */

public class OpeningDayView  extends LinearLayout{


    private TextView tvDayTitle;
    private TextView tvLunchTiming;
    private TextView tvDinnerTiming;

    public OpeningDayView(Context context) {
        super(context);
        init();
    }

    public OpeningDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpeningDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_opening_day,this);
        tvDayTitle = (TextView) view.findViewById(R.id.tvDayTitle);
        tvLunchTiming = (TextView) view.findViewById(R.id.tvLunchTiming);
        tvDinnerTiming = (TextView)  view.findViewById(R.id.tvDinnerTiming);
    }

    public void setOpeningHourOfDay(String day ,String lunchTiming,String dinnerTiming)
    {
        tvDayTitle.setText(day);
        tvLunchTiming.setText(lunchTiming);
        tvDinnerTiming.setText(dinnerTiming);
    }


}
