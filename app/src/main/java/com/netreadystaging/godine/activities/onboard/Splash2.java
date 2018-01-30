package com.netreadystaging.godine.activities.onboard;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.main.GoDineRestaurantSearchActivity;
import com.netreadystaging.godine.activities.main.Join_GoDine;
import com.netreadystaging.godine.activities.main.NewSignUp;
import com.netreadystaging.godine.activities.main.WhyJoinActivity;
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
 * Created by sony on 17-03-2017.
 */

public class Splash2  extends AppBaseActivity {
    AppGlobal appGlobal = AppGlobal.getInatance() ;
    TextView txtspl;
    Dialog dialog;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        txtspl= (TextView) findViewById(R.id.txt_spl);
        button= (Button) findViewById(R.id.bt_join);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    signupGoDine();
                }
            }
        });
     /*   Typeface cfon=Typeface.createFromAsset(getAssets(),"fonts/Fenotype - PeachesandCreamBold.ttf");
        txtspl.setTypeface(cfon);*/
     Intent intent=getIntent();
        String get=intent.getStringExtra("From");
        if(get!=null) {
            if (get.equalsIgnoreCase("Outside")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    signupGoDine();
                }
            }
        }
    }
    public void goToWhyJoin(View view) {
        try {
            Intent intent = new Intent(Splash2.this, WhyJoinActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void goToRestaurantSearch(View view) {
        try {
            Intent intent = new Intent(Splash2.this, GoDineRestaurantSearchActivity.class);
            intent.putExtra("From","Login");
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void  signupGoDine()
    {
        try {

            AlertDialog.Builder builder=new AlertDialog.Builder(Splash2.this);
            LayoutInflater inflater=Splash2.this.getLayoutInflater();
            View view1=inflater.inflate(R.layout.newsignupstafflayout,null);
            builder.setView(view1);
            RadioGroup radioGroup= (RadioGroup) view1.findViewById(R.id.staff_radiogroup);
            radioGroup.setVisibility(View.GONE);
            final EditText enteremail= (EditText) view1.findViewById(R.id.et_enterstaff);
            enteremail.setHint("Enter Email");
            enteremail.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            final ListView stafflist= (ListView) view1.findViewById(R.id.stafflist);
            stafflist.setVisibility(View.GONE);
            FrameLayout title= (FrameLayout) view1.findViewById(R.id.frame_titile);
            title.setVisibility(View.GONE);
            TextView txt_msg= (TextView) view1.findViewById(R.id.txt_message);
            txt_msg.setText("Please enter your Email id");
            TextView txt_search= (TextView) view1.findViewById(R.id.txt_staffsearch);
            txt_search.setText("OK");
            dialog=builder.create();
            txt_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                    String Email=enteremail.getText().toString().trim();
                    checkemail(Email);
                }
            });
            builder.setCancelable(false);
            dialog.show();
           /* Intent intent = new Intent(Splash2.this, NewSignUp.class);
         //   Intent intent = new Intent(Splash2.this, Join_GoDine.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public void loginact(View view) {
        try {
            Intent intent = new Intent(Splash2.this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
    private void checkemail(final String Email) {
        Utility.showLoadingPopup(this);
        if(!Email.isEmpty()) {
            if (Utility.checkValidEmail(Email)) {
                HashMap<String, String> params = new HashMap<>();
                params.put("Email", Email);
                new ServiceController(Splash2.this, new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data) {
                        if (success) {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(data);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                        String mesg = jsonObjects.getString("OutputResult");
                                        if (mesg.equalsIgnoreCase("EmailExists")) {
                                            AlertDialog.Builder builder=new AlertDialog.Builder(Splash2.this);
                                            builder.setTitle("\tMessage");
                                            builder.setMessage("It appears that you either were not able to complete your sign-up process, or you are a returning member.To reactivate your account simply login with your original login details.");
                                            builder.setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(Splash2.this,LoginActivity.class));

                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.create();
                                            builder.show();

                                          //  Utility.message(Splash2.this, "Email not available");
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(Splash2.this, NewSignUp.class);
                                            intent.putExtra("Email",Email);
                                            intent.putExtra("From","Splash");
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
                                            dialog.dismiss();

                                        }

                                    }
                                } else {
                                    Utility.message(Splash2.this, "Error");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            ErrorController.showError(Splash2.this, data, success);
                        }
                        Utility.hideLoadingPopup();

                    }
                }).request(ServiceMod.EmailCheck, params);
            }
            else {
                Utility.hideLoadingPopup();
                Utility.Alertbox(Splash2.this,"Error","Provide Valid Email","OK");
            }
        }
        else
        {
            Utility.message(getApplicationContext(),"Please enter email");
            Utility.hideLoadingPopup();

        }

    }



}
