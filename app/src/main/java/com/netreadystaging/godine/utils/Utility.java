package com.netreadystaging.godine.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.netreadystaging.godine.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by sony on 17-07-2016.
 */
public class Utility {
        public static boolean b;
    static ProgressDialog progressDialog;
    public static boolean checkValidEmail(final String email)
    {
        if(email!=null){
            String regularExpression = "^(<([a-z0-9_\\.\\-]+)\\@([a-z0-9_\\-]+\\.)+[a-z]{2,6}>|([a-z0-9_\\.\\-]+)\\@([a-z0-9_\\-]+\\.)+[a-z]{2,6})$";
            return email.trim().matches(regularExpression) ;
        }
        return false ;
    }

    public static void showLoadingPopup(Activity activity) {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        try {
//            int i = 10 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideLoadingPopup() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public static boolean isNetworkConnected(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        return false;
    }

    public static String BitMapToString(Bitmap bitmap){
        String temp="";
        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return temp;
    }
    public static boolean checkGooglePlayService(Activity activity)
    {
        int checkGooglePlayService= GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        int Requestcode=200;
        if(checkGooglePlayService!= ConnectionResult.SUCCESS)
        {
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayService,activity,Requestcode);
            Toast.makeText(activity," not working",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public static void message(Context context,String Msg)
    {
        Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
    }
    public static void Alertbox(Context context,String title,String Message,String Positve)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton(Positve, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }



}
