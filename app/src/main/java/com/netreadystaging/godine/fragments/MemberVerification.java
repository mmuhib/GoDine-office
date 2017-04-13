package com.netreadystaging.godine.fragments;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.ChoosePlanActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.onboard.Splash2;
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
                    Float amout= Float.valueOf(amount);
                    Float mem= Float.valueOf(membersaving);
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
                    Utility.Alertbox(getContext(),"Info","Please enter your Total Check Amount and GoDine Member Savings amount to complete the checkout process.","OK");
                }
            }
        });
       ((DrawerLocker)getActivity()).setDrawerLocked(true);
       final ProgressBar progressBar =  (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new DownloadImageTask((ImageView) view.findViewById(R.id.memberimg),progressBar).execute("https://godineclub.com/Portals/0/Images/Verification%20images/"+email+".jpg");
        Log.d("Img",""+"https://godineclub.com/Portals/0/Images/Verification%20images/"+email+".jpg");
        Log.d("Email",email);
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
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
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
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
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
    public void onResume() {
        super.onResume();
        title.setText("Member Verification");
        checkmembership();
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
                            txt_memberlevel.setText(MembershipType);
                            String ExpiryDate=jsonObject.getString("ExpiryDate");
                            goodthrough.setText(ExpiryDate);
                            if(MembershipType.equalsIgnoreCase("Expired") | ExpiryDate.isEmpty())
                            {
                                checkForMemberShipType();
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
    }
    private void checkForMemberShipType() {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Info");

            builder.setMessage("oppss! Our system has found you as Inactive member, Please reactive your profile.");

            builder.setPositiveButton("Reactivate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent =  new Intent(getActivity(), ChoosePlanActivity.class);
                    startActivityForResult(intent,200);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(),MainPageActivity.class) ;
                    startActivity(intent);

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


}
