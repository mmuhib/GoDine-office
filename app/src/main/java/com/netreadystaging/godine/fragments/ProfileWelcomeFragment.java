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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.ChoosePlanActivity;
import com.netreadystaging.godine.activities.main.PaymentView;
import com.netreadystaging.godine.callbacks.ImageSelectCallBack;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

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
                                     //   Utility.message(getContext(),data);
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        Bundle bundle=new Bundle();
                                        bundle.putByteArray("Pic",byteArray);
                                        Fragment fragment=null;
                                        FragmentManager manager=getActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction=manager.beginTransaction();
                                        fragment=new ProfilePageFragment();
                                        fragment.setArguments(bundle);
                                        transaction.replace(R.id.flContent,fragment);
                                        appGlobal.setIsVerificationImageUploaded("1");
                                        transaction.commit();

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

   //    checkForMemberShipType();

    }

    private void checkForMemberShipType() {
        if(appGlobal.getMemberType().toLowerCase().trim().equals("expired"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Info");

            builder.setMessage("We're sorry but our system indicates your membership " +
                    "has either expired or has been cancelled. To renew your membership " +
                    "now, click on \"Reactivate\" below");

            builder.setPositiveButton("Reactivate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent =  new Intent(getActivity(), ChoosePlanActivity.class);
                    startActivityForResult(intent,200);

                }
            });

            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

            if(b != null) {
                b.setBackgroundResource(R.drawable.btn_bg);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    b.setTextAppearance(R.style.GDAppButtonBaseTheme);
                }else
                {
                    b.setTextAppearance(getActivity(),R.style.GDAppButtonBaseTheme);
                }
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
}
