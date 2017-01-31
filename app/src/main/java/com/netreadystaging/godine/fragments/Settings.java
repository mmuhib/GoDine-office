package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.BrowserActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.callbacks.DrawerLocker;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;



import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 23-01-2017.
 */

public class Settings extends Fragment implements View.OnClickListener {
    TextView title,changepassword,changeimage,cancelmemebership,customersupport,termsc;
    View view;
    int pos;
    public String Selectedmile,Notification;
    Spinner settingmiles;
    ArrayList<String> miles;
    Switch notificationstatus;
    Button donesettings;
    AppGlobal appGlobal=AppGlobal.getInatance();
        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.setting,container,false);
        changepassword= (TextView) view.findViewById(R.id.changepassword);
        cancelmemebership= (TextView) view.findViewById(R.id.cancelmembership);
        changeimage= (TextView) view.findViewById(R.id.changeimage);
            termsc= (TextView) view.findViewById(R.id.termscondition);
            termsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent intent  = new Intent(getActivity(), BrowserActivity.class);
                        intent.putExtra("url","file:///android_asset/terms.html");
                        intent.putExtra("title","Terms and Conditions");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
                }
            });
            customersupport= (TextView) view.findViewById(R.id.customersupport);
            customersupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

             //       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,)
                }
            });
        settingmiles= (Spinner) view.findViewById(R.id.settingmiles);
        notificationstatus= (Switch) view.findViewById(R.id.myswitch);
        donesettings= (Button) view.findViewById(R.id.donesettings);
            donesettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.showLoadingPopup(getActivity());
                    HashMap<String,String> params=new HashMap<String, String>();
                    params.put("Username",appGlobal.getUsername());
                    params.put("Password",appGlobal.getPassword());
                    params.put("MobileNotification",Notification);
                    params.put("Miles",Selectedmile);
                    new ServiceController(getActivity(), new HttpResponseCallback() {
                        @Override
                        public void response(boolean success, boolean fail, String data) {
                            if (success) {
                                Log.d("Muhib",data);
                                Utility.hideLoadingPopup();
                                appGlobal.setMiles(Selectedmile);
                                Utility.Alertbox(getContext(),"Info","Data Saved Successfully","OK");

                            } else {
                                ErrorController.showError(getActivity(), data, success);
                            }
                        }
                    }).request(ServiceMod.UPDATE_SETTINGS, params);
                }
            });
            notificationstatus.setChecked(true);

        changepassword.setOnClickListener(this);
        cancelmemebership.setOnClickListener(this);
        changeimage.setOnClickListener(this);
        ((DrawerLocker)getActivity()).setDrawerLocked(true);
        setupToolBar();
        setupMiles();
        setupnotification();
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
                ProfilePageFragment frag=new ProfilePageFragment();
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
        title.setText("Settings");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DrawerLocker)getActivity()).setDrawerLocked(false);
    }

    @Override
    public void onClick(View view) {
       int v=view.getId();
        switch (v)
        {
            case R.id.changepassword:
                final AlertDialog.Builder build=new AlertDialog.Builder(getContext());
                LayoutInflater inflater=getActivity().getLayoutInflater();
                View view1=inflater.inflate(R.layout.dialogbox_activity,null);
                build.setView(view1);
                TextView message= (TextView) view1.findViewById(R.id.message);
                message.setText("Enter Old Password and New Password");
                final EditText currentpassword= (EditText) view1.findViewById(R.id.entername);
                currentpassword.setHint("Old Password");
                final EditText newpassword= (EditText) view1.findViewById(R.id.enternumber);
                newpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                newpassword.setHint("New Password");
                build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String currentpass=currentpassword.getText().toString().trim();
                        final String newpass=newpassword.getText().toString().trim();
                        if(!currentpass.isEmpty())
                        {
                            if(!newpass.isEmpty())
                            {
                              if(currentpass.equals(appGlobal.getPassword()))
                                {
                                   Utility.showLoadingPopup(getActivity());
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("Username",""+appGlobal.getUsername());
                                    params.put("CurrentPassword",currentpass);
                                    params.put("Password",newpass);
                                    new ServiceController(getActivity(), new HttpResponseCallback() {
                                        @Override
                                        public void response(boolean success, boolean fail, String data) {
                                            if (success) {
                                                Log.d("Muhib",data);
                                                Utility.hideLoadingPopup();
                                                Utility.Alertbox(getContext(),"Info","Password Changed Successfully","OK");
                                               appGlobal.setPassword(newpass);
                                            } else {
                                                ErrorController.showError(getActivity(), data, success);

                                            }
                                        }
                                    }).request(ServiceMod.PASSWORD_UPDATE, params);
                                }
                                else
                                {
                                    Utility.Alertbox(getContext(),"Info","Please enter old password correct.","OK");
                                }

                            }
                            else
                            {
                                Utility.Alertbox(getContext(),"Info","Please enter New Password","OK");
                            }
                        }
                        else
                        {
                            Utility.Alertbox(getContext(),"Info","Please enter Old Password","OK");
                        }
                        dialogInterface.dismiss();
                    }
                });
                build.create();
                build.show();
                break;
            case R.id.changeimage:
                Utility.Alertbox(getContext(),"Info",getResources().getString(R.string.changeimage),"OK");
                break;
            case R.id.cancelmembership:
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Cancel Membership");
                builder.setMessage("Are you sure that you would like to cancel your membership?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(getContext());
                        builder1.setTitle("Cancel Membership");
                        builder1.setMessage(getResources().getString(R.string.cancelmember));
                        builder1.setPositiveButton("Cancel Membership", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("UserId",""+appGlobal.getUserId());
                                new ServiceController(getActivity(), new HttpResponseCallback() {
                                    @Override
                                    public void response(boolean success, boolean fail, String data) {
                                        if (success) {
                                            Log.d("Muhib",data);
                                            Utility.Alertbox(getContext(),"Info","Data Saved Successfully","OK");
                                        } else {
                                            ErrorController.showError(getActivity(), data, success);
                                        }
                                    }
                                }).request(ServiceMod.CancelMembership, params);
                            }
                        });
                        builder1.create();
                        builder1.show();
                    }
                });
                builder.create();
                builder.show();
                break;
        }
    }
    private void setupMiles() {
        miles=new ArrayList<>();
        
        miles.add("5");
        miles.add("15");
        miles.add("25");
        miles.add("50");
        miles.add("100");
        final ArrayAdapter<String> adapt=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,miles);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settingmiles.setAdapter(adapt);
        if(!appGlobal.getMiles().isEmpty())
        {
            pos=adapt.getPosition(appGlobal.getMiles());
            settingmiles.setSelection(pos);
        }

        settingmiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Selectedmile= (String) adapterView.getItemAtPosition(i);
                if(i<0)
                {
                    ((TextView)view).setText("");
                }
                else
                {
                    Selectedmile=(String) adapterView.getItemAtPosition(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setupnotification() {
        if(notificationstatus.isChecked())
        {
            Notification="1";
           // Utility.message(getContext(),Notification);
        }
        else
        {
            Notification="0";
         //   Utility.message(getContext(),Notification);
        }
        notificationstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(notificationstatus.isChecked())
                {
                    Notification="1";
                    //  Utility.message(getContext(),Notification);
                }
                else
                {
                    Notification="0";
                    //Utility.message(getContext(),Notification);
                }
            }
        });
    }
}
