package com.netreadystaging.godine.activities.onboard;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.utils.AppGlobal;

public class SplashActivity extends AppBaseActivity {

    AppGlobal appGlobal = AppGlobal.getInatance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    appGlobal.context =  getApplicationContext() ;
                    if(!appGlobal.copyLStoAppGlobal()) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch(Exception  ex)
                {
                    ex.printStackTrace();
                    finish();
                }

            }
        },3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
