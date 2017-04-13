package com.netreadystaging.godine.activities.main;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.fragments.Howitworks;
import com.netreadystaging.godine.fragments.Signup_Referrer_Details;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

public class PaymentView extends AppCompatActivity {
    AppGlobal appGlobal = AppGlobal.getInatance() ;
    TextView mTitle;
    WebView paymentview;
    String UserID;
    String password;
    String ProductVariantID;
    String username;
    Fragment fragment=null;
    FragmentManager fragmentManager=getSupportFragmentManager();
    FragmentTransaction transaction=fragmentManager.beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_view);
        paymentview= (WebView) findViewById(R.id.paymentview);
        Bundle intent=getIntent().getExtras();
        username=intent.getString("username");
        password=intent.getString("password");
        ProductVariantID=intent.getString("productvariantid");
        UserID=intent.getString("UserD");
     // Utility.message(getApplicationContext(),username+" "+password+""+ProductVariantID);
        paymentview.setWebViewClient(new myWebViewClient());
        paymentview.setWebChromeClient(new WebChromeClient());
        paymentview.getSettings().setJavaScriptEnabled(true);
        paymentview.loadUrl("https://godineclub.com/api/CheckoutURL?productvariantid="+ProductVariantID+"&username="+username+"&password="+password);
        setupToolBar();
    }
    private void setupToolBar()
    {
        Toolbar toolbar_gd_rest_search = (Toolbar) findViewById(R.id.toolbar_gd_rest_search);
        mTitle = (TextView) toolbar_gd_rest_search.findViewById(R.id.tvToolBarMiddleLabel) ;
        mTitle.setText("Payment View");
        ImageView ivToolBarNavigationIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView ivToolBarEndIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarEndIcn) ;
        ImageView ivToolDoneIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarDone) ;
        ivToolBarEndIcn.setImageResource(R.drawable.donebutton);
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setVisibility(View.GONE);
        ivToolDoneIcn.setVisibility(View.GONE);
        ivToolDoneIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Utility.message(getApplicationContext(),UserID);
                Utility.showLoadingPopup(PaymentView.this);
                Checkpay();
            }
        });
    }

    private void Checkpay() {
        HashMap<String,String> params =new HashMap<>();
        params.put("UserId",UserID);
        params.put("ProductVariantId",ProductVariantID);
        new ServiceController(getApplicationContext(), new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                if (success)
                {
                    Utility.hideLoadingPopup();
                    Log.d("Muhib",data);
                    JSONArray jsonArray=null;
                    try {
                        String Result="";
                        String Message="";
                        jsonArray=new JSONArray(data);
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObjects = null;
                            jsonObjects = jsonArray.getJSONObject(i);
                            Result=jsonObjects.getString("Result");
                            Message=jsonObjects.getString("Message");
                            Log.d("Muhib",Result);
                            Log.d("Muhib",Message);
                            if(Result.equalsIgnoreCase("0"))
                            {
                                Utility.Alertbox(PaymentView.this,"Info",Message,"OK");
                            }
                            else if(Result.equalsIgnoreCase("1"))
                            {
                                //    Utility.Alertbox(PaymentView.this,"Info",Message,"OK");
                                HashMap<String,String> params =new HashMap<>();
                                params.put("UserId",UserID);
                                new ServiceController(getApplicationContext(),   new HttpResponseCallback() {
                                    @Override
                                    public void response(boolean success, boolean fail, String data)
                                    {
                                        if(success)
                                        {
                                            Utility.hideLoadingPopup();
                                            JSONArray jsonArray=null;
                                            try {
                                                jsonArray = new JSONArray(data);
                                                for(int i=0;i<jsonArray.length();i++)
                                                {
                                                    JSONObject jsonObjects = null;
                                                    jsonObjects = jsonArray.getJSONObject(i);
                                                    int userId = jsonObjects.getInt("UserId");
                                                    String firstName = jsonObjects.getString("FirstName");
                                                    String lastName = jsonObjects.getString("LastName");
                                                    String emailId = jsonObjects.getString("Email");
                                                    String mobile = jsonObjects.getString("Mobile");
                                                    String telephone = jsonObjects.getString("Telephone");
                                                    String address = jsonObjects.getString("Address");
                                                    String city = jsonObjects.getString("City");
                                                    String state = jsonObjects.getString("State");
                                                    String zipcode = jsonObjects.getString("ZipCode");
                                                    int membershipId = jsonObjects.getInt("MembershipId");
                                                    String memberExpiryDate = jsonObjects.getString("MemberExpiryDate");
                                                    String memberSince = jsonObjects.getString("MemberSince");
                                                    String ParentRestaurantIdOrUserId=jsonObjects.getString("ParentRestaurantIdOrUserId");
                                                    String isVerificationImageUploaded = jsonObjects.getString("IsVerificationImageUploaded");
                                                    String username = jsonObjects.getString("Username");
                                                    String mobileNotification = jsonObjects.getString("MobileNotification");
                                                    String miles = jsonObjects.getString("Miles");
                                                    String memberType = jsonObjects.getString("MembershipType");

                                                       /*/* *//***********************************
                                                 INITIALISE APP GLOBAL PARAM
                                                 **********************************/
                                                    appGlobal.setUserId(userId) ;
                                                    appGlobal.setPassword(password);
                                                    appGlobal.setUsername(username) ;
                                                    appGlobal.setFirstName(firstName) ;
                                                    appGlobal.setLastName(lastName);
                                                    appGlobal.setMobile(mobile );
                                                    appGlobal.setTelephone(telephone);
                                                    appGlobal.setEmailId(emailId);
                                                    appGlobal.setAddress(address);
                                                    appGlobal.setCity(city) ;
                                                    appGlobal.setState(state);
                                                    appGlobal.setZipcode(zipcode);
                                                    appGlobal.setMobileNotification(mobileNotification);
                                                    if(miles.equalsIgnoreCase(""))
                                                    {
                                                        appGlobal.setMiles("75");
                                                    }
                                                    else {
                                                        appGlobal.setMiles(miles);
                                                    }
                                                    appGlobal.setMembershipId(membershipId);
                                                    appGlobal.setMemberType(memberType);
                                                    appGlobal.setMemberExpiryDate(memberExpiryDate); ;
                                                    appGlobal.setMemberSince(memberSince);
                                                    appGlobal.setParentRestaurantIdOrUserId(ParentRestaurantIdOrUserId);
                                                    appGlobal.setIsVerificationImageUploaded(isVerificationImageUploaded);
                                                    appGlobal.welcomeText = "Welcome" ;
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(PaymentView.this);
                                                    builder.setTitle("Info");
                                                    builder.setCancelable(false);
                                                    builder.setMessage("Payment Done Successfully");
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Bundle bundle=new Bundle();
                                                            fragment=new Howitworks();
                                                            String Abc="From Payment";
                                                            bundle.putString("Abc",Abc);
                                                            transaction.replace(R.id.activity_p,fragment);
                                                            fragment.setArguments(bundle);
                                                            transaction.commit();
                                                            paymentview.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                                    builder.create();
                                                    builder.show();
                                                }
                                            }
                                            catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            ErrorController.showError(getApplicationContext(),data,success);
                                        }
                                    }
                                }).request(ServiceMod.USER_PROFILE,params);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(getApplicationContext(),data,success);
                }

            }
        }).request(ServiceMod.CHECK_PAYMENT, params);
    }

    class myWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utility.showLoadingPopup(PaymentView.this);
            super.onPageStarted(view, url, favicon);
        }
       @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           String s="";
           try {
               s=url.substring(0,url.indexOf("?"));
           }
           catch (Exception e)
           {
            e.printStackTrace();
           }
           if(s.equalsIgnoreCase("https://godineclub.com/confirmation"))
           {
               Checkpay();
            //   Utility.message(getApplicationContext(),"Payment done");
               Log.d("In if",url);
           }
           else if(s.equalsIgnoreCase("https://godineclub.com/products/checkout"))
           {
               Log.d("In else",url);
           }
           return super.shouldOverrideUrlLoading(view, url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url= String.valueOf(request.getUrl());
            String s="";
            try {
                s=url.substring(0,url.indexOf("?"));
            }
            catch (Exception e)
            {

            }
              if(s.equalsIgnoreCase("https://godineclub.com/confirmation"))
            {
                Checkpay();
                Utility.message(getApplicationContext(),"Payment done");
                Log.d("In if",url);
            }
            else if(s.equalsIgnoreCase("https://godineclub.com/products/checkout"))
            {
                Log.d("In else",url);
            }
            Log.d("Naughaut",s);
            return super.shouldOverrideUrlLoading(view,request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Utility.hideLoadingPopup();
        }
    }
}
