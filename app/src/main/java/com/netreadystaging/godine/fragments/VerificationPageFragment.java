package com.netreadystaging.godine.fragments;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.ChoosePlanActivity;
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
                setupMessage();

            }
        });
             setupToolBar();
        setupoverlay();
        return view ;
    }

    private void setupoverlay() {
        final Dialog dialog=new Dialog(getContext(),   android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.overlaypopup);

        TextView titlee= (TextView) dialog.findViewById(R.id.txt_msg);
        titlee.setText("By clicking \"Validate Member\", you will be redirected to the validation and checkout screen. Do not do so unless you are currently in a GoDine Partner Restaurant, and are ready to check out, as you will not be able to go back without completing the checkout process, or exiting the App.");
       Button yes= (Button) dialog.findViewById(R.id.overyes);
        Button no= (Button) dialog.findViewById(R.id.overno);

        dialog.setCancelable(false);
        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
       no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().getBackStackEntryCount();
                getActivity().getSupportFragmentManager().popBackStack();
                FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
                bottomToolBar.setVisibility(View.VISIBLE);
                ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
            }
        });
        dialog.show();
    }

    private void setupMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Info");

        builder.setMessage("Are you sure you want to validate yourself? Please select yes if you are within a partner restaurant else the app will not allow you to go back.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MemberVerification frag=new MemberVerification();
                FragmentManager fm =  getActivity().getSupportFragmentManager() ;
                fm.beginTransaction().replace(R.id.flContent, frag).addToBackStack(null).commit();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
             /*   Intent intent = new Intent(getActivity(),MainPageActivity.class) ;
                startActivity(intent);
                getActivity().finish();*/
            }
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button c = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        if(b != null && c!=null) {
            b.setBackgroundResource(R.drawable.alertbuttondesign);
            c.setBackgroundResource(R.drawable.alertbuttondesign);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                b.setTextAppearance(R.style.GDAppButtonBaseTheme);
                c.setTextAppearance(R.style.GDAppButtonBaseTheme);
            }else
            {
                b.setTextAppearance(getActivity(),R.style.GDAppButtonBaseTheme);
                c.setTextAppearance(getActivity(),R.style.GDAppButtonBaseTheme);
            }
        }
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
        FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }

}
