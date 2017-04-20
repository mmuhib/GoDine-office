package com.netreadystaging.godine.activities.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.main.GoDineRestaurantSearchActivity;
import com.netreadystaging.godine.activities.main.Join_GoDine;
import com.netreadystaging.godine.activities.main.WhyJoinActivity;
import com.netreadystaging.godine.utils.AppGlobal;

/**
 * Created by sony on 17-03-2017.
 */

public class Splash2  extends AppBaseActivity {
    AppGlobal appGlobal = AppGlobal.getInatance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }
    public void goToWhyJoin(View view) {
        try {
            Intent intent = new Intent(Splash2.this, WhyJoinActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
    public void goToRestaurantSearch(View view) {
        try {
            Intent intent = new Intent(Splash2.this, GoDineRestaurantSearchActivity.class);
            intent.putExtra("From","Login");
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
    public void  signupGoDine(View view)
    {
        try {
            Intent intent = new Intent(Splash2.this, Join_GoDine.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }
    public void loginact(View view) {
        try {
            Intent intent = new Intent(Splash2.this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


}
