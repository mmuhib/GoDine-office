package com.netreadystaging.godine.fragments;



import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 19-07-2016.
 */
public class FeedBackPageFragment extends Fragment implements View.OnClickListener {
    Button buttonverysatisfied,buttonsatisfied,buttonneutral,buttondissatisfied,buttonverydissatisfied,buttonsubmit;
    EditText et_comments,et_restaurantname1,et_restaurantnamme2,et_restaurantname3;
    View view ;
    TextView title ;
    String satisficationwithapp="";
    String ClientComment="";
    String restcity1="", restcity2="", restcity3="";

    AppGlobal appGlobal=AppGlobal.getInatance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.feedback_page_fragment,container,false);
        setupToolBar();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setupView();
        return view ;
    }

    private void setupView() {
        et_comments= (EditText) view.findViewById(R.id.edityourcmments);
        buttonverysatisfied= (Button) view.findViewById(R.id.buttonverysatisfied);
        buttonsatisfied= (Button) view.findViewById(R.id.buttonsatisfied);
        buttonneutral= (Button) view.findViewById(R.id.buttonnetural);
        buttondissatisfied= (Button) view.findViewById(R.id.buttondissatisfied);
        buttonverydissatisfied= (Button) view.findViewById(R.id.buttonverydissatisfied);
        et_restaurantname1= (EditText) view.findViewById(R.id.restaurantname1);
        et_restaurantnamme2= (EditText) view.findViewById(R.id.restaurantname2);
        et_restaurantname3= (EditText) view.findViewById(R.id.restaurantname3);
        buttonsubmit= (Button) view.findViewById(R.id.btsubmit);
        buttonsubmit.setOnClickListener(this);
        buttonverysatisfied.setOnClickListener(this);
        buttonsatisfied.setOnClickListener(this);
        buttonneutral.setOnClickListener(this);
        buttondissatisfied.setOnClickListener(this);
        buttonverydissatisfied.setOnClickListener(this);
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
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
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
        title.setText("FeedBack");
    }



    @Override
    public void onClick(View v) {

        ClientComment=et_comments.getText().toString();
        restcity1=et_restaurantname1.getText().toString();
        restcity2=et_restaurantnamme2.getText().toString();
        restcity3=et_restaurantname3.getText().toString();
        switch (v.getId())
        {
            case R.id.buttonverysatisfied:
                buttonverysatisfied.setBackgroundColor(Color.parseColor("#0080ff"));
                buttonsatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonneutral.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttondissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonverydissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                satisficationwithapp = buttonverysatisfied.getText().toString();
                break;
            case R.id.buttonsatisfied:
                buttonverysatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonsatisfied.setBackgroundColor(Color.parseColor("#0080ff"));
                buttonneutral.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttondissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonverydissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                satisficationwithapp=buttonsatisfied.getText().toString();
                break;
            case R.id.buttonnetural:
                buttonverysatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonsatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonneutral.setBackgroundColor(Color.parseColor("#0080ff"));
                buttondissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonverydissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                satisficationwithapp=buttonneutral.getText().toString();
                break;
            case R.id.buttondissatisfied:

                buttonverysatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonsatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonneutral.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttondissatisfied.setBackgroundColor(Color.parseColor("#0080ff"));
                buttonverydissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                satisficationwithapp=buttondissatisfied.getText().toString();
               break;
            case R.id.buttonverydissatisfied:
                buttonverysatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonsatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonneutral.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttondissatisfied.setBackgroundColor(Color.parseColor("#d3d3d3"));
                buttonverydissatisfied.setBackgroundColor(Color.parseColor("#0080ff"));
                satisficationwithapp=buttonverydissatisfied.getText().toString();
                break;
            case R.id.btsubmit:
                Toast.makeText(getActivity(),"Sending...",Toast.LENGTH_LONG).show();
                HashMap<String,String> params=new HashMap<>();
                params.put("UserId",appGlobal.getUserId()+"");
                params.put("Commments",ClientComment);
                params.put("Satisficationwithapp",satisficationwithapp);
                params.put("Restcity1",restcity1);
                params.put("Restcity2",restcity2);
                params.put("Restcity3",restcity3);

                new ServiceController(getActivity(), new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data)
                    {

                        if(success)
                        {
                            Toast.makeText(getActivity(),"Your Feedback has been recorded",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            ErrorController.showError(getActivity(),data,success);
                        }
                    }
                }).request(ServiceMod.FEEDBACK,params);
                if(!Utility.isNetworkConnected(getActivity()))
                {
                    Toast.makeText(getActivity(),"Check your internet Connection",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FrameLayout bottomToolBar = (FrameLayout)getActivity().findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }
}
