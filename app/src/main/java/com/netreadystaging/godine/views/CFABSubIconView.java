package com.netreadystaging.godine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;

/**
 * Created by sony on 22-11-2016.
 */

public class CFABSubIconView extends LinearLayout {

    private TextView imageText ;
    private ImageView imageIcon ;

    public CFABSubIconView(Context context) {
        super(context);
        init();
    }

    public CFABSubIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CFABSubIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
       View view  =  LayoutInflater.from(getContext()).inflate(R.layout.cfab_sub_icon_view,this);
        imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
        imageText  = (TextView)view.findViewById(R.id.imageText);
    }

    public void setView(int resId , String text)
    {
        imageIcon.setImageDrawable(getResources().getDrawable(resId));
        imageText.setText(text);
    }
}
