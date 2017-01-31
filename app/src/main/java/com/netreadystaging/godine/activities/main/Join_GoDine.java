package com.netreadystaging.godine.activities.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.fragments.Signup_Chosoeplan;
import com.netreadystaging.godine.fragments.Signup_Referrer_Details;
import com.netreadystaging.godine.utils.Utility;

public class Join_GoDine extends AppBaseActivity implements View.OnClickListener {
    public static int ProductVariantIDD=0;
    public static int Values=0;
    TextView mTitle;
    public static Button bt_chooseplan,bt_refferdetails,bt_memberdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join__go_dine);
        bt_chooseplan= (Button) findViewById(R.id.btchosseplan);
        bt_refferdetails= (Button) findViewById(R.id.btrefferdetails);
        bt_memberdetails= (Button) findViewById(R.id.btmemberdetails);
        bt_chooseplan.setOnClickListener(this);
        bt_refferdetails.setOnClickListener(this);
       // bt_memberdetails.setOnClickListener(this);
        setupToolBar();
        selectScreenPart(R.id.btchosseplan);
    }
    private void setupToolBar()
    {
        Toolbar toolbar_gd_rest_search = (Toolbar) findViewById(R.id.toolbar_gd_rest_search);
        mTitle = (TextView) toolbar_gd_rest_search.findViewById(R.id.tvToolBarMiddleLabel) ;
        mTitle.setText(getResources().getText(R.string.signup));
        ImageView ivToolBarNavigationIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView ivToolBarEndIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        selectScreenPart(view.getId());
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void selectScreenPart(int id)
    {
        Fragment fragment=null;
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction= manager.beginTransaction();
        switch(id)
        {
            case R.id.btchosseplan:
               ProductVariantIDD=0;
               Values=0;
              //  Utility.message(getApplicationContext(),""+ Join_GoDine.ProductVariantIDD);
                bt_chooseplan.setSelected(true);
                bt_refferdetails.setSelected(false);
                bt_memberdetails.setSelected(false);
                fragment = new Signup_Chosoeplan();
                break;
            case R.id.btrefferdetails:

                    bt_refferdetails.setSelected(false);
                    bt_chooseplan.setSelected(false);
                    bt_memberdetails.setSelected(false);
                    fragment = new Signup_Referrer_Details();
                    break;
        }
        transaction.replace(R.id.frag,fragment);
        transaction.commit();
    }
}
