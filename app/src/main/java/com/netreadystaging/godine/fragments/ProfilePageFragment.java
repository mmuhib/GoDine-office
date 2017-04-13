package com.netreadystaging.godine.fragments;



import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.ChoosePlanActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.callbacks.ImageSelectCallBack;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.DownloadImageTask;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 19-07-2016.
 */
public class ProfilePageFragment extends ImageSelectFragment {

    View view ;
    ImageView ivProfileImage,imgUploadProfile ;
    TextView title ,tvMembershipId ,tvMembershipLevel,tvMemberSince;
    Bitmap bmp;
    AppGlobal appGlobal = AppGlobal.getInatance() ;
    private TextView tvGoodThrough;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view =  inflater.inflate(R.layout.profile_page_activity,container,false);
        setupToolBar();
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        try {
            byte[] byteArray = getArguments().getByteArray("Pic");
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageSignup(bmp);
            ivProfileImage.setImageBitmap(bmp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String  isVerificationImageUploaded=appGlobal.getIsVerificationImageUploaded();
        if(isVerificationImageUploaded.equalsIgnoreCase("0")) {
            Imageverification();
        }
        else
        {
            //Utility.message(getContext(),isVerificationImageUploaded);
            //loadProfilePic();
        }

        setupGUI() ;

        return view ;
    }

    private void setupGUI() {
        TextView tvWelcomeUsername = (TextView) view.findViewById(R.id.tvWelcomeUsername) ;
        String fullname =  appGlobal.getFirstName()+" "+appGlobal.getLastName() ;
        tvWelcomeUsername.setText(fullname);
        tvMembershipId = (TextView)view.findViewById(R.id.tvMembershipId);
        tvMembershipLevel = (TextView)view.findViewById(R.id.tvMembershipLevel);
        tvMemberSince = (TextView) view.findViewById(R.id.tvMemberSince);
        tvGoodThrough = (TextView) view.findViewById(R.id.tvGoodThrough);
        imgUploadProfile = (ImageView) view.findViewById(R.id.imgUploadProfile);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        imgUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
              //  appGlobal.setIsVerificationImageUploaded("1");
                selectImage("bitmap",new ImageSelectCallBack()
                {
                    @Override
                    public void success(String data) {
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void success(final Bitmap bitmap) {

                        HashMap<String,String> params =  new HashMap<>();
                        String  bitmapString= Utility.BitMapToString(bitmap);
                        params.put("Base64String",bitmapString);
                        params.put("UserId",appGlobal.getUserId()+"");
                        new ServiceController(getActivity(), new HttpResponseCallback() {
                            @Override
                            public void response(boolean success, boolean fail, String data) {
                                progressBar.setVisibility(View.GONE);
                                if(success)
                                {

                                    JSONArray jsonArray=null;
                                    String Result="";
                                    String Message="";
                                    try {
                                        jsonArray=new JSONArray(data);
                                        for (int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject jsonObject=null;
                                            jsonObject=jsonArray.getJSONObject(i);
                                            Result=jsonObject.getString("Result");
                                            Message=jsonObject.getString("Message");
                                            Log.d("Muhib",Result);
                                            Log.d("Muhib",Message);
                                            if(Result.equalsIgnoreCase("Success"))
                                            {
                                                ivProfileImage.setImageBitmap(bitmap);
                                            }
                                            if(getActivity()!=null)
                                                Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("In Setup",data);
                                }
                                else
                                {
                                    ErrorController.showError(getActivity(),data,false);
                                }

                            }
                        }).request(ServiceMod.PROFILE_IMAGE_UPLOAD,params);
                    }
                    @Override
                    public void fail(String error) {
                        progressBar.setVisibility(View.GONE);
                        Utility.message(getContext(),"Error"+error);
                    }
                });
            }
        });
        if(Utility.isNetworkConnected(getActivity()))
        {
            loadProfilePic();
            loadMemberShipSavings();
        }
        title.setText("My Profile");
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
        ivToolBarEndIcn.setImageResource(R.drawable.settings);
        ivToolBarEndIcn.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings fragmen=new Settings();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragmen).commit();
            }
        });
        /**** ToolBar Setting ****/
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        bindModelToView();
        checkmembership();
    }



    private void loadMemberShipSavings() {
        HashMap<String,String> params =  new HashMap<>();
        params.put("UserId",appGlobal.getUserId()+"");
        new ServiceController(getActivity(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                if(success)
                {
                    try {
                        JSONArray jArray = new JSONArray(data);
                        for (int index = 0; index < jArray.length(); index++) {
                            JSONObject jObj = jArray.getJSONObject(index) ;
                            final TextView tvMemberMonthlySavings = (TextView) view.findViewById(R.id.tvMemberMonthlySavings) ;
                            final TextView tvMemberYearlySavings = (TextView) view.findViewById(R.id.tvMemberYearlySavings) ;
                            final  TextView tvMemberLifetimeSavings = (TextView) view.findViewById(R.id.tvMemberLifetimeSavings) ;
                            tvMemberMonthlySavings.setText("$"+jObj.getString("ThisMonthSavings"));
                            tvMemberYearlySavings.setText("$"+jObj.getString("ThisYearSavings"));
                            tvMemberLifetimeSavings.setText("$"+jObj.getString("OverallSavings"));
                        }
                    } catch (JSONException e) {
                        ErrorController.showError(getActivity(),data,success);
                    }
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.GET_RESTAURANT_SAVINGS,params);
    }

    private void loadProfilePic() {
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> params =  new HashMap<>();
        params.put("Username",appGlobal.getUsername());
        params.put("Password",appGlobal.getPassword());
        Utility.showLoadingPopup(getActivity());
        Log.d("Data",""+params);
        new ServiceController(getActivity(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                if(success)
                {

                    try {
                        JSONArray jArray = new JSONArray(data);

                        for (int index = 0; index < jArray.length(); index++) {
                            JSONObject jObj = jArray.getJSONObject(index) ;
                            String profilePicUrl  ="https://" +jObj.getString("Url") ;
                            //   String s=profilePicUrl.substring(0,profilePicUrl.indexOf("?"));
                            //  Log.d("Image",s);
                            Log.d("In Load",profilePicUrl);
                            if(getActivity()!=null) {
                                new DownloadImageTask(ivProfileImage,progressBar).execute(profilePicUrl);
                                Utility.hideLoadingPopup();
                            }
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        ErrorController.showError(getActivity(),data,success);
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.ProfilePicDownload,params);
    }

    private void bindModelToView()
    {
        tvMembershipId.setText(appGlobal.getMembershipId()+"");
     //   tvMembershipLevel.setText(appGlobal.getMemberType());
        tvMemberSince.setText(appGlobal.getMemberSince());
       // tvGoodThrough.setText(appGlobal.getMemberExpiryDate());
    }
    private void Imageverification()
    {
        String data=getResources().getString(R.string.info);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Info");
        builder.setMessage(data);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectImage("bitmap",new ImageSelectCallBack()
                {
                    @Override
                    public void success(String data) {

                    }
                    @Override
                    public void success(final Bitmap bitmap) {
                        ImageSignup(bitmap);
                    }
                    @Override
                    public void fail(String error) {
                    }
                });
            }
        });
        builder.create();
        builder.show();

    }
    public void ImageSignup(final Bitmap bitmap)
    {
        HashMap<String,String> params =  new HashMap<>();
        String  bitmapString= Utility.BitMapToString(bitmap);
        params.put("Base64String",bitmapString);
        params.put("UserId",appGlobal.getUserId()+"");
        new ServiceController(getActivity(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                if(success)
                {
                    Utility.showLoadingPopup(getActivity());
                    JSONArray jsonArray=null;
                    String Result="";
                    String Message="";
                    try {
                        jsonArray=new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=null;
                            jsonObject=jsonArray.getJSONObject(i);
                            Result=jsonObject.getString("Result");
                            Message=jsonObject.getString("Message");
                            Log.d("Muhib",Result);
                            Log.d("Muhib",Message);
                            Utility.Alertbox(getContext(),"Info",Message,"OK");
                            if(Result.equalsIgnoreCase("Success"))
                            {
                                verificationOfImage();
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

            private void verificationOfImage() {
                HashMap<String,String> params =  new HashMap<>();
                String  bitmapString= Utility.BitMapToString(bitmap);
                params.put("Base64String",bitmapString);
                params.put("Username",appGlobal.getUsername()+"");

                new ServiceController(getActivity(), new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data) {
                        if(success)
                        {
                            JSONArray jsonArray=null;
                            String Result="";
                            String Message="";
                            try {
                                jsonArray=new JSONArray(data);
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=null;
                                    jsonObject=jsonArray.getJSONObject(i);
                                    Result=jsonObject.getString("Result");
                                    Message=jsonObject.getString("Message");
                                    Log.d("Muhib",Result);
                                    Log.d("Muhib",Message);
                                    if(Result.equalsIgnoreCase("Success"))
                                    {
                                        appGlobal.setIsVerificationImageUploaded("1");
                                        //  Utility.Alertbox(getContext(),"Info",Message,"Ok");
                                        ivProfileImage.setImageBitmap(bitmap);
                                        Utility.hideLoadingPopup();
                                    }
                                    else if(Result.equalsIgnoreCase("Failed"))
                                    {
                                        // Utility.Alertbox(getContext(),"Info","Profile picture stored successfully","Ok");
                                        // Utility.Alertbox(getContext(),"Info",Message,"Ok");
                                        ivProfileImage.setImageBitmap(bitmap);
                                        Utility.hideLoadingPopup();
                                        if(appGlobal.getIsVerificationImageUploaded().equalsIgnoreCase("0"))
                                        {

                                        }
                                    }
                                    Utility.message(getContext(),Message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("In imageVerification",data);

                        }
                        else
                        {
                            ErrorController.showError(getActivity(),data,false);
                        }
                    }
                }).request(ServiceMod.UploadVerificationImage,params);

            }
        }).request(ServiceMod.PROFILE_IMAGE_UPLOAD,params);

        loadProfilePic();

    }

    private void checkmembership() {
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
                        jsonArray=new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = null;
                            jsonObject = jsonArray.getJSONObject(i);
                            String MembershipType=jsonObject.getString("MembershipType");
                            tvMembershipLevel.setText(MembershipType);
                            String ExpiryDate=jsonObject.getString("ExpiryDate");
                            tvGoodThrough.setText(ExpiryDate);
                            if(MembershipType.equalsIgnoreCase("Expired") | ExpiryDate.isEmpty())
                            {
                                checkForMemberShipType();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Data",data);
                }
                else
                {
                    ErrorController.showError(getActivity(),data,false);
                }
            }
        }).request(ServiceMod.MembershipValidation,params);

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

