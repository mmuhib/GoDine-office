package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.main.PaymentView;
import com.netreadystaging.godine.callbacks.DrawerLocker;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.DownloadImageTask;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class NewMemberVerification extends Fragment {

    Button checkout;
    LinearLayout ll_memberStatus;
    View view;
    ImageView memberimg;
    AppGlobal appGlobal=AppGlobal.getInatance();
    final String id= String.valueOf(appGlobal.getMembershipId());
    TextView txt_membername,txt_member_id,txt_memberlevel,txt_membersince,txt_memberdate,txt_validity, title,txt_goodthrough,txt_reactivate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.newmemberverification,container,false);
        memberimg= (ImageView) view.findViewById(R.id.memberimg);
        ll_memberStatus= (LinearLayout) view.findViewById(R.id.ll_memberStatus);
        String email=appGlobal.getEmailId().trim();
        ((DrawerLocker)getActivity()).setDrawerLocked(true);
        final ProgressBar progressBar =  (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new DownloadImageTask((ImageView) view.findViewById(R.id.memberimg),progressBar).execute("http://gdcomp.godineclub.com/Portals/0/Images/Verification%20images/"+email+".jpg");
        Log.d("Img",""+"http://gdcomp.godineclub.com/Portals/0/Images/Verification%20images/"+email+".jpg");
        Log.d("Email",email);
        setupToolBar();
        setupTextviews();
        return view;

    }

    @Override
    public void onResume() {
        title.setText("\tMember Verification");
        checkmembership();
        super.onResume();
    }

    private void setupTextviews() {
        txt_membername= (TextView) view.findViewById(R.id.membername);
        txt_member_id= (TextView) view.findViewById(R.id.txt_memberid);
        txt_goodthrough=(TextView) view.findViewById(R.id.txt_goodthrough);
        txt_membersince= (TextView) view.findViewById(R.id.memberSince);
        txt_memberdate= (TextView) view.findViewById(R.id.memberdate);
        txt_reactivate= (TextView) view.findViewById(R.id.txt_reactivate);
        txt_validity= (TextView) view.findViewById(R.id.txt_validity);
        txt_membername.setText(appGlobal.getFirstName());
        txt_member_id.setText(id);
//        txt_memberlevel.setText(appGlobal.getMemberType());
        txt_membersince.setText(appGlobal.getMemberSince());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formattedDate = df.format(calendar.getTime());
        txt_memberdate.setText(formattedDate);
        Log.d("datd",appGlobal.getMemberExpiryDate());

    }
    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setVisibility(View.GONE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.GONE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    getActivity().getSupportFragmentManager().popBackStack();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void checkmembership() {
        Utility.showLoadingPopup(getActivity());
        HashMap<String,String> params =  new HashMap<>();
        params.put("UserId",appGlobal.getUserId()+"");
        new ServiceController(getActivity(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                Utility.hideLoadingPopup();
                if(success)
                {
                    JSONArray jsonArray=null;
                    try {
                        Log.d("Data",data);
                        jsonArray=new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = null;
                            jsonObject = jsonArray.getJSONObject(i);
                            String MembershipType=jsonObject.getString("MembershipType");
//                            txt_memberlevel.setText(MembershipType);
                            String ExpiryDate=jsonObject.getString("ExpiryDate");

                            if(MembershipType.equalsIgnoreCase("Expired") | ExpiryDate.isEmpty())
                            {
                                txt_goodthrough.setText(MembershipType);
                                txt_validity.setText("INVALID MEMBERSHIP");
                                ll_memberStatus.setBackgroundColor(Color.parseColor("#fe0000"));
                                txt_reactivate.setVisibility(View.VISIBLE);
                                txt_reactivate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent =  new Intent(getActivity(), PaymentView.class);
                                        intent.putExtra("username",appGlobal.getUsername());
                                        intent.putExtra("password",appGlobal.getPassword());
                                        intent.putExtra("productvariantid","94");
                                        intent.putExtra("UserD",appGlobal.getUserId()+"");
                                        startActivity(intent);
                                    }
                                });
                            }
                            else
                            {
                                txt_validity.setText("VALID MEMBERSHIP");
                                txt_goodthrough.setText("GOOD THRU \t"+appGlobal.getMemberExpiryDate());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    ErrorController.showError(getActivity(),data,false);
                }
            }
        }).request(ServiceMod.MembershipValidation,params);

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
