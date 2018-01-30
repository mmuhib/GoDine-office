package com.netreadystaging.godine.activities.main;

import android.content.Intent;

import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.onboard.Splash2;
import com.netreadystaging.godine.utils.Utility;

import in.technobuff.helper.activities.MediaPlayerActivity;


public class WhyJoinActivity extends AppBaseActivity {
    TextView Clickhere;
    Button joinnow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newhowitworks);
        joinnow= (Button) findViewById(R.id.bt_joinnow);
        joinnow.setVisibility(View.VISIBLE);
        Clickhere= (TextView) findViewById(R.id.txt_clickhere);
        String clicktext="Got Questions? CLICK HERE ";
        int i=clicktext.indexOf("CL");
        int j=clicktext.indexOf("E ");
        Clickhere.setMovementMethod(LinkMovementMethod.getInstance());
        Clickhere.setText(clicktext,TextView.BufferType.SPANNABLE);
        Spannable spannable= (Spannable) Clickhere.getText();
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(View widget) {
              //  Utility.message(getApplicationContext(),"Hello");
                ApiConfig.Builder configBuilder = new ApiConfig.Builder();
                configBuilder.setRequireEmail(true);
                Support.showFAQs(WhyJoinActivity.this
                        , configBuilder.build());

            }
        };
        spannable.setSpan(clickableSpan,i,j+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    public void signupGoDine(View v)
    {
        Intent intent=new Intent(WhyJoinActivity.this, Splash2.class);
        intent.putExtra("From","Outside");
        startActivity(intent);
        //startActivity(new Intent(WhyJoinActivity.this,NewSignUp.class));
    }
}
   /*try {
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
    }*/