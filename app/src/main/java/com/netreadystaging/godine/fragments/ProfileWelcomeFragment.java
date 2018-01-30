package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.ChoosePlanActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.main.NewSignUp;
import com.netreadystaging.godine.activities.main.PaymentView;
import com.netreadystaging.godine.activities.onboard.Splash2;
import com.netreadystaging.godine.callbacks.DrawerLocker;
import com.netreadystaging.godine.callbacks.ImageSelectCallBack;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import in.technobuff.helper.http.HttpResponseCallback;


/**
 * Created by sony on 19-07-2016.
 */
public class ProfileWelcomeFragment extends ImageSelectFragment {

    AppGlobal appGlobal = AppGlobal.getInatance() ;
    View view  ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.profile_welcome_fragment,container,false);

        TextView tvWelcomeText = (TextView) view.findViewById(R.id.tvWelcomeText) ;
        tvWelcomeText.setText(appGlobal.welcomeText);
        TextView tvWelcomeUsername = (TextView) view.findViewById(R.id.tvWelcomeUsername) ;

        String fullname =  appGlobal.getFirstName()+" "+appGlobal.getLastName() ;
        tvWelcomeUsername.setText(fullname);
        String data=getResources().getString(R.string.info);
        String  isVerificationImageUploaded=appGlobal.getIsVerificationImageUploaded();
        //   Utility.message(getContext(),appGlobal.getIsVerificationImageUploaded());
        if(isVerificationImageUploaded.equalsIgnoreCase("0"))
        {

            try {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Almost There!");
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
                                Utility.showLoadingPopup(getActivity());
                                HashMap<String,String> params =  new HashMap<>();
                                String  bitmapString= Utility.BitMapToString(bitmap);
                                params.put("Base64String",bitmapString);
                                params.put("Username",appGlobal.getUsername()+"");

                                new ServiceController(getActivity(), new HttpResponseCallback() {
                                    @Override
                                    public void response(boolean success, boolean fail, String data) {
                                        if(success)
                                        {
                                            Utility.hideLoadingPopup();
                                            JSONArray jsonArray=null;
                                            String Result="";
                                            String Message="";
                                            try {
                                                jsonArray=new JSONArray(data);
                                                for (int i=0;i<jsonArray.length();i++) {
                                                    JSONObject jsonObject=null;
                                                    jsonObject=jsonArray.getJSONObject(i);
                                                    Result=jsonObject.getString("Result");
                                                    Message=jsonObject.getString("Message");
                                                    Log.d("Muhib",Result);
                                                    Log.d("Muhib",Message);
                                                    if(Result.equalsIgnoreCase("Success"))
                                                    {
                                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                        byte[] byteArray = stream.toByteArray();
                                                        Bundle bundle=new Bundle();
                                                        bundle.putByteArray("Pic",byteArray);
                                                        Fragment fragment=null;
                                                        fragment=new ProfilePageFragment();
                                                        FragmentManager manager=getActivity().getSupportFragmentManager();
                                                        FragmentTransaction transaction=manager.beginTransaction();
                                                        fragment.setArguments(bundle);
                                                        transaction.replace(R.id.flContent,fragment);
                                                        appGlobal.setIsVerificationImageUploaded("1");
                                                        transaction.commit();
                                                    }
                                                    else if(Result.equalsIgnoreCase("Failed"))
                                                    {
                                                        Utility.hideLoadingPopup();
                                                        Log.d("Mui",Message);
                                                        // Utility.message(getActivity(),Message);
                                                        Fragment fragment=null;
                                                        FragmentManager manager=getActivity().getSupportFragmentManager();
                                                        FragmentTransaction transaction=manager.beginTransaction();
                                                        fragment=new ProfilePageFragment();
                                                        transaction.replace(R.id.flContent,fragment);
                                                        transaction.commit();
                                                        Utility.Alertbox(getContext(),"Info",Message,"Ok");
                                                    }

                                                }
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                                Log.d("Mui",""+e);
                                            }
                                            //   Utility.message(getContext(),data);


                                            // ErrorController.showError(getActivity(),data,true);
                                        }
                                        else
                                        {
                                            ErrorController.showError(getActivity(),data,false);
                                        }

                                    }
                                }).request(ServiceMod.UploadVerificationImage,params);


                            }
                            @Override
                            public void fail(String error) {
                                Fragment fragment=null;
                                FragmentManager manager=getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction=manager.beginTransaction();
                                fragment=new ProfilePageFragment();
                                transaction.replace(R.id.flContent,fragment);
                                transaction.commit();
                            }
                        });
                    }
                });
                builder.create();
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Error",""+e);
            }

        }
        else
        {

        }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Up Welcome Image
        setupWelcomeImage();

//  checkForMemberShipType();
        checkmembership();
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

                            String ExpiryDate=jsonObject.getString("ExpiryDate");

                            if(MembershipType.equalsIgnoreCase("Expired") )
                            {
                                checkForMemberShipType();
                            }
                            else if(MembershipType.equalsIgnoreCase("Inactive") )
                            {
                                checkForMemberShipType();
                            }
                            else if(MembershipType.equalsIgnoreCase("Cancelled"))
                            {
                                checkForCancelledMemberShipType();

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

    private void checkForCancelledMemberShipType()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Info");

        builder.setMessage("oppss! Our system has found you as Cancelled member, Please reactive your profile.");

        builder.setPositiveButton("Reactivate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Reactivate();
            }
        });
        builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(),Splash2.class));
                getActivity().finish();
                appGlobal.resetAppGlobalParams();
                dialog.dismiss();
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
                Intent intent =  new Intent(getActivity(), PaymentView.class);
                intent.putExtra("username",appGlobal.getUsername());
                intent.putExtra("password",appGlobal.getPassword());
                intent.putExtra("productvariantid","94");
                intent.putExtra("UserD",appGlobal.getUserId()+"");
                startActivity(intent);
               /* Intent intent =  new Intent(getActivity(), ChoosePlanActivity.class);
                startActivityForResult(intent,200);*/


            }
        });
        builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(),Splash2.class));
                getActivity().finish();
                appGlobal.resetAppGlobalParams();
                dialog.dismiss();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200)
        {
            if(resultCode==201)
            {
                Intent intent =  new Intent(getActivity(), PaymentView.class);
                intent.putExtra("username",appGlobal.getUsername());
                intent.putExtra("password",appGlobal.getPassword());
                intent.putExtra("productvariantid",data.getStringExtra("product_id"));
                intent.putExtra("UserD",appGlobal.getUserId()+"");
                startActivity(intent);
            }
        }
    }

    private void setupWelcomeImage() {

        AssetManager am  = getResources().getAssets() ;
        // load image
        try {
            // get input stream
            Random random = new Random();
            int indexImage  = random.nextInt(5)+1 ;
            InputStream ims = am.open(indexImage+".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            ImageView welcomeImage = (ImageView) view.findViewById(R.id.welcomeImage) ;
            welcomeImage.setImageDrawable(d);
        }
        catch(IOException ex) {
        }
    }
    public void Reactivate()
    {
        Utility.showLoadingPopup(getActivity());
        HashMap<String,String> params =  new HashMap<>();
        params.put("Username",appGlobal.getEmailId());
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
                            String AffiliateID=jsonObject.getString("AffiliateID");
                            String Username=jsonObject.getString("Username");
                            String FirstName=jsonObject.getString("FirstName");
                            String LastName=jsonObject.getString("LastName");
                            String Email=jsonObject.getString("Email");
                            String Cell=jsonObject.getString("Cell");
                            String Telephone=jsonObject.getString("Telephone");
                            String Street=jsonObject.getString("Street");
                            String City=jsonObject.getString("City");
                            String State=jsonObject.getString("State");
                            String Country=jsonObject.getString("Country");
                            String ZipCode=jsonObject.getString("ZipCode");
                            String RestaurantName=jsonObject.getString("RestaurantName");
                            String BusinessName=jsonObject.getString("BusinessName");
                            String AffiliateMemberType=jsonObject.getString("AffiliateMemberType");
                            String RecruiterID=jsonObject.getString("RecruiterID");
                            String AffiliateFirstName=jsonObject.getString("AffiliateFirstName");
                            String AffiliateLastName=jsonObject.getString("AffiliateLastName");

                            Intent intent=new Intent(getActivity(), NewSignUp.class);
                            intent.putExtra("Email",Email);
                            intent.putExtra("AffiliateID",AffiliateID);
                            intent.putExtra("RecruiterID",RecruiterID);
                            intent.putExtra("RestaurantName",RestaurantName);
                            intent.putExtra("City",City);
                            intent.putExtra("From","Reactivate");

                            startActivity(intent);

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
        }).request(ServiceMod.ReactivationUserDetails,params);

    }
}
