package com.netreadystaging.godine.activities.main;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;

import in.technobuff.helper.activities.MediaPlayerActivity;


public class WhyJoinActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_why_join);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       final Button btnJoinNow = (Button) findViewById(R.id.btnJoinNow);
        btnJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WhyJoinActivity.this,Join_GoDine.class));
                overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });
        ImageView imgViewGoDineBanner = (ImageView) findViewById(R.id.imgViewGoDineBanner);
        imgViewGoDineBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(WhyJoinActivity.this, MediaPlayerActivity.class);
                intent.putExtra(MediaPlayerActivity.SOURCE_FROM,MediaPlayerActivity.LOCAL);
                intent.putExtra(MediaPlayerActivity.LOCAL_FILE_FULLNAME,"gd_video.mp4");
                startActivity(intent);
            }
        });
    }
    public void goBackTomain(View view)
    {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_why_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
