package com.netreadystaging.godine.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.BrowserActivity;
import com.netreadystaging.godine.activities.main.Join_GoDine;
import com.netreadystaging.godine.activities.main.PaymentView;
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

import static com.netreadystaging.godine.activities.main.Join_GoDine.ProductVariantIDD;
import static com.netreadystaging.godine.activities.main.Join_GoDine.Values;


/**
 * Created by sony on 13-12-2016.
 */

public class Signup_Member_Details extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {
    View view;
    Spinner sp_memberstate;
    EditText et_name,et_password,et_confirmPass,et_firstname,et_lastname,et_telephone,et_memberstreet,et_membercity,et_zipcode;
    Button bt_register,bt_cancel;
    RadioButton rb_justone,rb_plusone,rb_plusthree;
    CheckBox ch_terms;
    String SAffiliateId = "N/A";
    String StaffName = "N/A";
    String ProductVariantID="N/A";
    String UserId;
    TextView planame,Step;

    public   String Sstate;
    public  String Sname,Spassword,Sconfirmpass,Sfirstname,Slname,Stelephone, Sstreet,Scity,Szipcode,SMiles;
    AppGlobal appGlobal=AppGlobal.getInatance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            SAffiliateId = getArguments().getString("AffiliateId");
            StaffName = getArguments().getString("StaffName");
            ProductVariantID= getArguments().getString("ProductVariantID");
            Log.d("Affe",SAffiliateId);
            Log.d("name",StaffName);
            Log.d("Product",ProductVariantID);
            if((StaffName.equalsIgnoreCase("Staff Member Name or Number(Optional)")))
            {
                StaffName="";
                      }
            else {
          //      Utility.message(getContext(), StaffName);
            }
        } catch (Exception e) {
            Log.d("Error",""+e);
        }
        view = inflater.inflate(R.layout.signup_member_details, container, false);
        Join_GoDine.bt_chooseplan.setSelected(false);

        Join_GoDine.bt_refferdetails.setSelected(false);
        Join_GoDine.bt_memberdetails.setSelected(true);
        handleedittexts();
        handlebutons();
        handleradiobutons();
        ch_terms = (CheckBox) view.findViewById(R.id.ch_agree);

        if (ProductVariantIDD==20)
        {
            rb_justone.setChecked(true);
        }
        else if(ProductVariantIDD==21)
        {
            rb_plusone.setChecked(true);
        }
        else if(ProductVariantIDD==22)
        {
            rb_plusthree.setChecked(true);
        }

        final TextView tvTermCondition = (TextView) view.findViewById(R.id.tvTermCondition);
        tvTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra("url","file:///android_asset/terms.html");
                intent.putExtra("title","Terms and Conditions");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });
            return view;
    }
    public void handleedittexts()
    {
        et_name= (EditText) view.findViewById(R.id.et_memberEmail);
        et_password=(EditText) view.findViewById(R.id.et_memberPass);
        et_confirmPass=(EditText) view.findViewById(R.id.et_memberConfirmPass);
        et_firstname=(EditText) view.findViewById(R.id.et_memberFirstname);
        et_lastname=(EditText) view.findViewById(R.id.et_memberLastname);
        et_telephone=(EditText) view.findViewById(R.id.et_memberTelephone);
        et_telephone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        et_memberstreet=(EditText) view.findViewById(R.id.et_memberStreet);
        et_membercity=(EditText) view.findViewById(R.id.et_memberCity);
        et_zipcode=(EditText) view.findViewById(R.id.et_memberZipcode);
        sp_memberstate= (Spinner) view.findViewById(R.id.sp_memberstate);
        sp_memberstate.setOnItemSelectedListener(this);

        et_name.setOnEditorActionListener(this);
        et_confirmPass.setOnEditorActionListener(this);
    }

    private void handleradiobutons()
    {
        rb_justone= (RadioButton) view.findViewById(R.id.rb_justone);
        rb_plusone= (RadioButton) view.findViewById(R.id.rb_plusone);
        rb_plusthree= (RadioButton) view.findViewById(R.id.rb_plusthree);
        rb_justone.setOnClickListener(this);
        rb_plusone.setOnClickListener(this);
        rb_plusthree.setOnClickListener(this);
    }

    private void handlebutons()
    {
        bt_register= (Button) view.findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Utility.message(getContext(),SAffiliateId);
               // Utility.message(getContext(),ProductVariantID);

                Sname=et_name.getText().toString();
                Spassword=et_password.getText().toString();
                Sconfirmpass=et_confirmPass.getText().toString();
                Sfirstname=et_firstname.getText().toString();
               Slname=et_lastname.getText().toString();
                Stelephone=et_telephone.getText().toString();
            //    PhoneNumberUtils.formatNumber(Stelephone);
               Sstreet=et_memberstreet.getText().toString();
                Scity=et_membercity.getText().toString();
              Szipcode=et_zipcode.getText().toString();
                 SMiles="25";
                final String SdeviceId="31cf203f8d4469ab59f397be5f6df49ac1b2f6e682f6cf15b8d1aeafc72e7c25";

                if(!Sname.isEmpty() && !Spassword.isEmpty() &&!Sconfirmpass.isEmpty() && !Sfirstname.isEmpty() && !Slname.isEmpty() && !Sstreet.isEmpty() && !Scity.isEmpty() && !Szipcode.isEmpty() &&!Stelephone.isEmpty() )
                {
                    if (ch_terms.isChecked()) {
                        if(Utility.checkValidEmail(Sname))
                        {
                            if (Spassword.equals(Sconfirmpass))
                            {
                                        HashMap<String,String> params=new HashMap<>();
                                        params.put("MembershipProductVariantId",ProductVariantID);
                                        params.put("AffiliateId",SAffiliateId);
                                        params.put("ServerName",StaffName);
                                        params.put("Email",Sname);
                                        params.put("Password",Spassword);
                                        params.put("FirstName",Sfirstname);
                                        params.put("LastName",Slname);
                                        params.put("Telephone",Stelephone);
                                        params.put("Street",Sstreet );
                                        params.put("City" ,Scity );
                                        params.put("Country","United States");
                                        params.put("Region",Sstate );
                                        params.put("PostalCode",Szipcode);
                                        params.put("DeviceId",SdeviceId);
                                        params.put("DeviceType","Android");
                                        params.put("Miles",SMiles);
                                        Utility.showLoadingPopup(getActivity());

                                        new ServiceController(getContext(), new HttpResponseCallback()
                                        {
                                            @Override
                                            public void response(boolean success, boolean fail, String data)
                                            {
                                                if(success)
                                                {
                                                    Utility.hideLoadingPopup();
                                                    JSONArray jsonArray=null;
                                                    try {
                                                        String Result="";
                                                        String Message="";
                                                        Log.d("Muhib",data);
                                                        jsonArray = new JSONArray(data);
                                                        for (int i=0;i<jsonArray.length();i++)
                                                        {
                                                            JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                                            Result=jsonObjects.getString("Result");
                                                            Message=jsonObjects.getString("Message");
                                                            if(Result.equalsIgnoreCase("Success"))
                                                            {
                                                                UserId=  jsonObjects.getString("UserId");
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                                builder.setTitle("Info");
                                                                builder.setCancelable(false);
                                                                builder.setMessage("Congratulations! Your account has been successfully created with GoDine."+"\n"+"You will now be re-direct to our website to complete the payment portion of your membership. Once you have completed your payment, you will be redirected to the App."+"\n"+"If payment fails for some reason, don’t worry you can simply login to your GoDine App using the user name and password that you created, and it will give you the option to complete your payment.");
                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.dismiss();
                                                                        Intent intent=new Intent(getContext(), PaymentView.class);
                                                                        intent.putExtra("username",Sname);
                                                                        appGlobal.setPassword(Spassword);
                                                                        appGlobal.setMiles(SMiles);
                                                                        intent.putExtra("password",Spassword);
                                                                        intent.putExtra("productvariantid",ProductVariantID);
                                                                        intent.putExtra("UserD",UserId);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                                builder.create();
                                                                builder.show();
                                                            }
                                                            else
                                                            {
                                                                Utility.Alertbox(getActivity(),"Info",Message,"OK");
                                                            }

                                                        }

                                                    } catch (JSONException e) {
                                                    /*    Utility.Alertbox(getActivity(),"Info",Message,"OK");
                                                        final AlertDialog.Builder aBuilder1=new AlertDialog.Builder(getContext());
                                                        aBuilder1.setTitle("Info");
                                                        aBuilder1.setMessage("Username already available");
                                                        aBuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        });
                                                        aBuilder1.create();
                                                        aBuilder1.show();*/
                                                        Utility.Alertbox(getContext(),"Info","Please Try again","OK");
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else
                                                {
                                                    ErrorController.showError(getContext(),data,success);

                                                }
                                            }
                                        }).request(ServiceMod.SIGN_UP,params);
                            }
                            else
                            {
                                Utility.Alertbox(getContext(),"Error","Please check Password","OK");
                            }
                        }
                        else {
                            Utility.Alertbox(getContext(),"Error","Provide Valid Email","OK");

                        }
                    }
                    else {
                        Utility.Alertbox(getContext(),"Error","Please Agree to Terms and Conditions","OK");
                       }
                }
                else
                {
                    Utility.Alertbox(getContext(),"Error","Please enter all mandatory fields","OK");
                }
            }
        });
        bt_cancel= (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id)
        {
            case R.id.rb_justone:
                ProductVariantID="20";

                break;
            case R.id.rb_plusone:
                ProductVariantID="21";

                break;
            case R.id.rb_plusthree:
                ProductVariantID="22";
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        int id=textView.getId();
        Sname=et_name.getText().toString();
        Spassword=et_password.getText().toString();
        Sconfirmpass=et_confirmPass.getText().toString();
        switch (id)
        {
            case R.id.et_memberEmail:
                if(i== EditorInfo.IME_ACTION_NEXT)
                {
                    if(Utility.checkValidEmail(Sname))
                    {

                    }
                    else
                    {
                        Utility.Alertbox(getContext(),"Error","Please enter valid email","OK");
                        et_name.requestFocus();
                    }
                }
                break;
            case R.id.et_memberConfirmPass:
                if(i== EditorInfo.IME_ACTION_NEXT)
                {

                  if(!Spassword.isEmpty() &&!Sconfirmpass.isEmpty()) {
                      if (Spassword.equals(Sconfirmpass))
                      {

                      } else {
                          Utility.Alertbox(getContext(), "Error", "Password and Confirm Does not match.", "OK");
                          et_confirmPass.setFocusable(true);
                          et_confirmPass.requestFocus();
                      }
                  }
                    else
                  {
                      Utility.Alertbox(getContext(),"Error","Please enter Password and Confirm Password ","OK");
                  }
                }
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
       String abc[]= getResources().getStringArray(R.array.States);
        if (i <=0) {
            Sstate=abc[1];
       //     Utility.message(getActivity(), Sstate);
        }
        else
        {
            Sstate=abc[i];
       //     Utility.message(getActivity(), Sstate);
        }
  //       Sstate=abc[i];
       // Utility.message(getContext(),Sstate);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        planame= (TextView) getView().findViewById(R.id.planname);
        Step= (TextView) getView().findViewById(R.id.step);
        Step.setText("STEP #4");
        planame.setText("Enter Member Details ");
    }
}
