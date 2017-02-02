package com.netreadystaging.godine.fragments;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.callbacks.DrawerLocker;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.DownloadImageTask;
import com.netreadystaging.godine.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sony on 10-01-2017.
 */

public class MemberVerification extends Fragment {
    EditText et_checkamount,et_memebersaving;
    Button checkout;
    View view;
    ImageView memberimg;
    AppGlobal appGlobal=AppGlobal.getInatance();
    final String id= String.valueOf(appGlobal.getMembershipId());
    TextView txt_membername,txt_member_id,txt_memberlevel,txt_membersince,txt_memberdate,txt_wrongpage, title,goodthrough;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.memberverification,container,false);
        memberimg= (ImageView) view.findViewById(R.id.memberimg);
        et_checkamount= (EditText) view.findViewById(R.id.Etcheckamount);
        et_memebersaving= (EditText) view.findViewById(R.id.Etcheckmembersaving);
        checkout= (Button) view.findViewById(R.id.checkout);
        String email=appGlobal.getEmailId().trim();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=et_checkamount.getText().toString();
                String membersaving= et_memebersaving.getText().toString();
                if(!amount.isEmpty() && !membersaving.isEmpty()) {
                    int amout=Integer.parseInt(amount);
                    int mem=Integer.parseInt(membersaving);
                    if(amout>mem)
                    {
                        SelectnearbyRestaurant frag=new SelectnearbyRestaurant();
                        Bundle restBundle = new Bundle();
                        restBundle.putSerializable("amount",amount);
                        restBundle.putSerializable("membersaving",membersaving);
                        restBundle.putSerializable("userId",id);
                        frag.setArguments(restBundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,frag).commit();
                    }
                    else
                    {
                        Utility.Alertbox(getContext(),"Info","Savings amount cannot be greater than check amount","OK");
                    }
                }
                else
                {
                    Utility.Alertbox(getContext(),"Info","Please enter your Total Check Amount and GoDine Member Savings amount to complete the checkout process.","Ok");
                }
            }
        });
       ((DrawerLocker)getActivity()).setDrawerLocked(true);
        new DownloadImageTask((ImageView) view.findViewById(R.id.memberimg)).execute("https://godineclub.com/Portals/0/Images/Verification%20images/"+email+".jpg");
        setupToolBar();
        setupTextviews();
        return view;
    }
    private void setupTextviews() {
        txt_membername= (TextView) view.findViewById(R.id.membername);
        txt_member_id= (TextView) view.findViewById(R.id.txt_memberid);
        txt_memberlevel=(TextView) view.findViewById(R.id.txt_membershipStatus);
        txt_membersince= (TextView) view.findViewById(R.id.memberSince);
        txt_memberdate= (TextView) view.findViewById(R.id.memberdate);
        goodthrough= (TextView) view.findViewById(R.id.goodthrough);
        goodthrough.setText(appGlobal.getMemberExpiryDate());
        txt_membername.setText(appGlobal.getFirstName());
        txt_member_id.setText(id);
        txt_memberlevel.setText(appGlobal.getMemberType());
        txt_membersince.setText(appGlobal.getMemberSince());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        String formattedDate = df.format(calendar.getTime());
        txt_memberdate.setText(formattedDate);
        Log.d("datd",appGlobal.getMemberExpiryDate());
        txt_wrongpage= (TextView) view.findViewById(R.id.wrongpage);
        txt_wrongpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=null;
                FragmentManager manager=getActivity().getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                fragment=new ProfilePageFragment();
                transaction.replace(R.id.flContent,fragment);
                transaction.commit();
            }
        });
    }


    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText("Member Verification");
    }
  @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DrawerLocker)getActivity()).setDrawerLocked(false);
    }
}
