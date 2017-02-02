package com.netreadystaging.godine.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.Utility;


import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 19-07-2016.
 */
public class ContactPageFragment extends Fragment {

    View view ;
    TextView help;
    Button sendmesage;
    EditText editmessage;
    String message;
    AppGlobal appGlobal= AppGlobal.getInatance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.contact_page_fragment,container,false);
        setupToolBar();
        setupView();

        return view ;
    }

    private void setupView() {
        help= (TextView) view.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiConfig.Builder configBuilder = new ApiConfig.Builder();
                configBuilder.setRequireEmail(true);
                Support.showFAQs(getActivity()
                        , configBuilder.build());
            }
        });
        sendmesage= (Button) view.findViewById(R.id.sendmessage);
        editmessage= (EditText) view.findViewById(R.id.editmessage);
        message=editmessage.getText().toString();
        sendmesage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message=editmessage.getText().toString().trim();
                if(!message.isEmpty()) sendMessageToGoDine(message);
                else Toast.makeText(getActivity(),"Enter Message",Toast.LENGTH_LONG).show();
            }
        });
        if(!Utility.isNetworkConnected(getActivity()))
        {
            Toast.makeText(getActivity(),"Check your internet Connection",Toast.LENGTH_LONG).show();
        }

    }

    private void sendMessageToGoDine(String message) {
        Toast.makeText(getActivity(),"Sending...",Toast.LENGTH_LONG).show();
        HashMap<String,String> params=new HashMap<>();
        params.put("UserId",appGlobal.getUserId()+"");
        params.put("Message",message);
        new ServiceController(getActivity(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {

                if(success)
                {
                  Utility.message(getContext(),"Your message has been sent");
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.CALL_BACK_REQUEST,params);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        // Bottom Menu
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.GONE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
    }

TextView title ;
    @Override
    public void onResume() {
        super.onResume();
        title.setText("Contact GoDine");
    }
}
