package com.netreadystaging.godine.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.utils.Utility;

/**
 * Created by sony on 01-12-2016.
 */

public class Howitworks extends Fragment implements View.OnClickListener{

    View view;
    TextView howit_works,nothanks;
    String SAffiliateId = "N/A";
    String Data = "N/A";
    private ImageView back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            SAffiliateId=getArguments().getString("Abc");
          //  Utility.message(getContext(),SAffiliateId);
        }
        catch (Exception e){

        }
        view = inflater.inflate(R.layout.how_it_works, container, false);
        Activity activity = getActivity();
        howit_works= (TextView) view.findViewById(R.id.howitworkstext);
        nothanks=(TextView) view.findViewById(R.id.nothanks);
        back= (ImageView) view.findViewById(R.id.ivBack);
        back.setOnClickListener(this);
        String st=getString(R.string.how_it_works);
        Spanned spanned= Html.fromHtml(st);
        ImageView ivToolBarBack = (ImageView)view.findViewById(R.id.ivToolBarBack) ;
        howit_works.setText(spanned);
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

    private void setupToolBar() {

        Toolbar toolBar  =  (Toolbar) getActivity().findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().getBackStackEntryCount();
        getActivity().getSupportFragmentManager().popBackStack();
        FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }
}
