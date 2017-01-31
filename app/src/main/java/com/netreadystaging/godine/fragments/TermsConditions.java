package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.callbacks.DrawerLocker;

/**
 * Created by sony on 27-01-2017.
 */

public class TermsConditions extends Fragment implements View.OnClickListener {
    TextView title;
    View view;
    WebView webView;
    private ImageView back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_browser,container,false);
        webView= (WebView) view.findViewById(R.id.browser);
        ((DrawerLocker)getActivity()).setDrawerLocked(true);
        webView.loadUrl("file:///android_asset/terms.html");
        setupToolBar();
        return view;
    }
    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        final ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarEndIcn.setImageResource(R.drawable.search_icn_toolbar);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          Settings frag=new Settings();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,frag).commit();
            }
        });
        ivToolBarEndIcn.setVisibility(View.GONE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.GONE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText("Terms and Conditions");
    }
    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().getBackStackEntryCount();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
