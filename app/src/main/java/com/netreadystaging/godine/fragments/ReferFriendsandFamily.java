package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;


/**
 * Created by sony on 24-01-2017.
 */

public class ReferFriendsandFamily extends Fragment {
    View view;
    EditText et_email1,et_email2,et_email3;
    TextView title ;
    Button sendmail;
    String emailA="",emailB="",emailC="";
    AppGlobal appGlobal=AppGlobal.getInatance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.referfriends,container,false);
        et_email1= (EditText) view.findViewById(R.id.email1);
        et_email2= (EditText) view.findViewById(R.id.email2);
        et_email3= (EditText) view.findViewById(R.id.email3);
        sendmail= (Button) view.findViewById(R.id.sendemail);

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder shareContent = new StringBuilder();
                shareContent.append("I went to ").append("www.godineclub.com/").append(AppGlobal.getInatance().getUserId()).append(" and joined GoDine. Now I get 50% OFF of my entree's every time that I dine out at any GoDine Premier Partner Restaurant in the Spokane / Coeur d'Alene area. You can too by just clicking the link above in this message.");

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share"));
            /*    emailA=et_email1.getText().toString();
                emailB=et_email2.getText().toString();
                emailC=et_email3.getText().toString();
           if(!emailA.isEmpty() || !emailB.isEmpty() || !emailC.isEmpty())
            {
                Utility.message(getContext(),"Sending...");
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("Email1",emailA);
                params.put("Email2",emailB);
                params.put("Email3",emailC);
                params.put("UserId",""+appGlobal.getUserId());
                new ServiceController(getActivity(), new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data)
                    {
                        if(success)
                        {
                            JSONArray jsonArray=null;
                            try {
                                jsonArray=new JSONArray(data);
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    String Result=object.getString("Result");
                                    String Message=object.getString("Message");
                                    if(Result.equalsIgnoreCase("Success"))
                                    {
                                        Utility.message(getContext(),Message);
                                    }
                                    else if(Result.equalsIgnoreCase("Error"))
                                    {
                                        Utility.Alertbox(getContext(),Result,Message,"OK");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            ErrorController.showError(getActivity(),data,success);
                        }
                    }
                }).request(ServiceMod.SEND_REFERRAL_MAIL,params);
            }
                else
           {
               Utility.Alertbox(getContext(),"Info","Please enter Email","OK");
           }*/
            }
        });
        setupToolBar();
       return view;
    }
    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.VISIBLE);
        ivToolBarBack.setVisibility(View.GONE);
        ivToolBarEndIcn.setVisibility(View.GONE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        title.setText("Refer Friends and Family");
    }
}
