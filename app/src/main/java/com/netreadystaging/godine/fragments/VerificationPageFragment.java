package com.netreadystaging.godine.fragments;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.callbacks.DrawerLocker;

/**
 * Created by sony on 19-07-2016.
 */
public class VerificationPageFragment extends Fragment {
    RelativeLayout layout;
    Button by_verification;
    TextView txt_cancel;
    TextView title ;
    View view ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.verification_page_fragment, container, false);


        by_verification= (Button) view.findViewById(R.id.Verify);
        ((DrawerLocker)getActivity()).setDrawerLocked(true);
       /*layout= (RelativeLayout) view.findViewById(R.id.backimageload);
        Drawable drawable=getResources().getDrawable(R.drawable.splash);
        layout.setBackground(drawable);*/
        txt_cancel= (TextView) view.findViewById(R.id.cancel);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().getBackStackEntryCount();
                getActivity().getSupportFragmentManager().popBackStack();
                FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
                bottomToolBar.setVisibility(View.VISIBLE);
                ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
            }
        });
        by_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberVerification frag=new MemberVerification();
                FragmentManager fm =  getActivity().getSupportFragmentManager() ;
                fm.beginTransaction().replace(R.id.flContent, frag).addToBackStack("Muhib").commit();
            }
        });
             setupToolBar();
        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.GONE);
      ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.GONE);
        ivToolBarEndIcn.setVisibility(View.GONE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.GONE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DrawerLocker)getActivity()).setDrawerLocked(false);

    }
}
