package com.netreadystaging.godine.activities.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;

public class AboutRestaurantActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_restaurant);
        String about = getIntent().getStringExtra("about");
        final TextView restAbout = (TextView) findViewById(R.id.restAbout);
        restAbout.setText(about);
        setupTopBar();
    }

    private void setupTopBar()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView  ivToolBarNavigationIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        final ImageView ivToolBarBack = (ImageView)toolbar.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

        final TextView title= (TextView)toolbar.findViewById(R.id.tvToolBarMiddleLabel) ;
        title.setVisibility(View.VISIBLE);
        title.setText("Description");
        final ImageView ivToolBarEndIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarEndIcn.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
    }


}
