package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.main.WhyJoinActivity;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class newHowItWorks extends Fragment implements View.OnClickListener {
    View view;
    TextView howit_works,nothanks;
    String SAffiliateId = "N/A";
    String Data = "N/A";
    private ImageView back;
    TextView Clickhere;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            SAffiliateId=getArguments().getString("Abc");
            //  Utility.message(getContext(),SAffiliateId);
        }
        catch (Exception e){

        }
        view = inflater.inflate(R.layout.newhowitworks, container, false);
        Activity activity = getActivity();
        Clickhere= (TextView)view. findViewById(R.id.txt_clickhere);
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
                Support.showFAQs(getActivity()
                        , configBuilder.build());

            }
        };
        spannable.setSpan(clickableSpan,i,j+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nothanks=(TextView) view.findViewById(R.id.nothanks);
        back= (ImageView) view.findViewById(R.id.ivBack);
        back.setOnClickListener(this);
        if(SAffiliateId.equals("From Payment"))
        {
            back.setVisibility(View.GONE);
            nothanks.setVisibility(View.VISIBLE);
            nothanks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),MainPageActivity.class);
                    startActivity(intent);
                }
            });
        }
        else
        {
            back.setVisibility(View.VISIBLE);
            nothanks.setVisibility(View.GONE);
            FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
            bottomToolBar.setVisibility(View.GONE);
            ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
            setupToolBar();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().getBackStackEntryCount();
        getActivity().getSupportFragmentManager().popBackStack();
        FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }
    private void setupToolBar() {

        Toolbar toolBar  =  (Toolbar) getActivity().findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }
}
