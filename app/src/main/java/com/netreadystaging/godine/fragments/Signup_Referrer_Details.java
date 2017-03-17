package com.netreadystaging.godine.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.GoDineRestaurantSearchActivity;
import com.netreadystaging.godine.activities.main.Join_GoDine;
import com.netreadystaging.godine.activities.main.Members;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;

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
 * Created by Muhib on 05-12-2016.
 */

public class Signup_Referrer_Details extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener{
    RadioGroup radioGroup;
    RadioButton Byrestaurant,Bymember;
    EditText godinememberid,et_RestaurantmemberNumber;
    TextView txtrestaurentname,txtcityname,txtname,txtcellname,bt_findsponsorbymember,bt_findsponsorbyrest;
    Button notrefered,btrefNext,bt_staff,bt_godinememberid,bt_RestaurantmemberNumber;
    View view;
    public  String AffiliateId="N/A",ProductVariant="N/A",StaffName="";
    LinearLayout layout_referedbyrestaurant,layout_referedbymember;
    TextView planame,Step;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (Values == 0) {
            try {

                ProductVariant = getArguments().getString("Product");
                Join_GoDine.bt_refferdetails.setSelected(true);
                //   Utility.message(getContext(),ProductVariant);
                Values = 1;
            } catch (Exception e) {
                Utility.Alertbox(getContext(), "Error", "Please Select your Plan", "OK");
                Join_GoDine.bt_chooseplan.setSelected(true);
                Values = 0;
                Fragment fragment = null;
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                fragment = new Signup_Chosoeplan();
                transaction.replace(R.id.frag, fragment);
                transaction.commit();
                return inflater.inflate(R.layout.signup_choose_plan,container,false);
            }
            view = inflater.inflate(R.layout.signup_referrer_details, container, false);
        }
        else
        {
            Join_GoDine.bt_chooseplan.setSelected(false);
            Join_GoDine.bt_refferdetails.setSelected(true);
            Values=1;
            ProductVariant = String.valueOf(ProductVariantIDD);
/*                Utility.message(getContext(),"Varient"+ProductVariant);
                Utility.message(getContext(),"Varient ID"+ProductVariantIDD);*/
            view = inflater.inflate(R.layout.signup_referrer_details, container, false);
        }

        Join_GoDine.bt_chooseplan.setSelected(false);
        Join_GoDine.bt_refferdetails.setSelected(true);
        Join_GoDine.bt_memberdetails.setSelected(false);
        setupradiobuttons();
        handlebuttonclicks();
        handleEdittexts();

        txtrestaurentname= (TextView) view.findViewById(R.id.txt_restaurantname);
        txtcityname= (TextView) view.findViewById(R.id.txt_cityname);
        txtname= (TextView) view.findViewById(R.id.txt_name);
        txtcellname= (TextView) view.findViewById(R.id.txt_cellname);

        return view;
    }

    private void handleEdittexts() {
        godinememberid= (EditText) view.findViewById(R.id.et_godine_memberId);
        et_RestaurantmemberNumber= (EditText) view.findViewById(R.id.Et_restaurantmembernumber);
        godinememberid.setOnEditorActionListener(this);
        et_RestaurantmemberNumber.setOnEditorActionListener(this);
    }

    private void handlebuttonclicks()
    {
        notrefered= (Button) view.findViewById(R.id.bt_notReffered);
        bt_findsponsorbyrest= (TextView) view.findViewById(R.id.bt_findsponsorbyrest);
        bt_findsponsorbymember= (TextView) view.findViewById(R.id.bt_findsponsorbymember);
        bt_staff=(Button) view.findViewById(R.id.bt_staffname);
        btrefNext= (Button) view.findViewById(R.id.bt_refNext);
        bt_godinememberid= (Button) view.findViewById(R.id.bt_godine_memberId);
        bt_RestaurantmemberNumber=(Button) view.findViewById(R.id.bt_restaurantmembe);
        btrefNext.setOnClickListener(this);
        notrefered.setOnClickListener(this);
        bt_findsponsorbyrest.setOnClickListener(this);
        bt_findsponsorbymember.setOnClickListener(this);
        bt_staff.setOnClickListener(this);
        bt_godinememberid.setOnClickListener(this);
        bt_RestaurantmemberNumber.setOnClickListener(this);
    }
    private void setupradiobuttons() {
        radioGroup= (RadioGroup) view.findViewById(R.id.radiogroup);
        radioGroup.setOnClickListener(this);
        Byrestaurant= (RadioButton) view.findViewById(R.id.ch_Refferedbyrestauarnt);
        Bymember= (RadioButton) view.findViewById(R.id.ch_Refferedbymember);
        Byrestaurant.setOnClickListener(this);
        Bymember.setOnClickListener(this);
        layout_referedbyrestaurant= (LinearLayout) view.findViewById(R.id.layout_referedbyRestaurant);
        layout_referedbymember= (LinearLayout) view.findViewById(R.id.layout_referedbyMember);
    }

    @Override
    public void onClick(final View view) {

        int id=view.getId();

        if(id==R.id.ch_Refferedbyrestauarnt)
        {
            if(btrefNext.getVisibility()==View.VISIBLE)
            {
                btrefNext.setVisibility(View.GONE);
            }
            Step.setText("STEP #3");
            planame.setText("Enter Referrer Restaurant#");
            layout_referedbyrestaurant.setVisibility(View.VISIBLE);
            layout_referedbymember.setVisibility(View.GONE);
        }
        if(id==R.id.ch_Refferedbymember)
        {
            if(btrefNext.getVisibility()==View.VISIBLE)
            {
                btrefNext.setVisibility(View.GONE);
            }
            Step.setText("STEP #3");
            planame.setText("Enter Referrer Member#");
            layout_referedbyrestaurant.setVisibility(View.GONE);
            layout_referedbymember.setVisibility(View.VISIBLE);
        }
        if(id==R.id.bt_notReffered)
        {
            Utility.showLoadingPopup(getActivity());
            final HashMap<String,String> params=new HashMap<>();
            new ServiceController(getActivity(), new HttpResponseCallback()
            {
                @Override
                public void response(boolean success, boolean fail, String data){
                    if(success){
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=new JSONArray(data);
                            if (jsonArray.length()>0)
                            {
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    Utility.hideLoadingPopup();
                                    JSONObject Aff  = jsonArray.getJSONObject(i);
                                    String AffiliateI=Aff.getString("AffiliateId");
                                    Log.d("No",AffiliateI);
                                    Bundle bundle = new Bundle();
                                    Fragment fragment=null;
                                    bundle.putString("AffiliateId", AffiliateI);
                                    bundle.putString("ProductVariantID", ProductVariant);
                                    bundle.putString("StaffName", StaffName);
                                    fragment = new Signup_Member_Details();
                                    fragment.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag, fragment).addToBackStack(null).commit();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                             }
                    else
                    {
                        ErrorController.showError(getActivity(),data,success);
                    }


                }
            }).request(ServiceMod.DefaultAffiliate,params);
            /*Intent intent=new Intent(getContext(), GoDineRestaurantSearchActivity.class);
            intent.putExtra("From","Signup");
            startActivityForResult(intent,3);*/
        }
        if(id==R.id.bt_findsponsorbyrest)
        {
            Intent intent=new Intent(getContext(), GoDineRestaurantSearchActivity.class);
            intent.putExtra("From","Signup");
            startActivityForResult(intent,3);
        }
        if(id==R.id.bt_findsponsorbymember)
        {
            Intent intent=new Intent(getContext(), Members.class);
            startActivityForResult(intent,2);
        }
        if(id==R.id.bt_staffname)
        {
            final AlertDialog.Builder build=new AlertDialog.Builder(getContext());
            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view1=inflater.inflate(R.layout.dialogbox_activity,null);
            build.setView(view1);
            TextView tchangepass= (TextView) view1.findViewById(R.id.pasword);
            tchangepass.setVisibility(View.GONE);
            final EditText textname= (EditText) view1.findViewById(R.id.entername);
            final EditText textnumber= (EditText) view1.findViewById(R.id.enternumber);
            build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    StringBuilder sbuilder=new StringBuilder();
                    sbuilder.append(textname.getText().toString());
                    sbuilder.append(textnumber.getText().toString());
                    String txt= String.valueOf(sbuilder);
                    if(!txt.isEmpty())
                    {
                        bt_staff.setText(txt);
                    }
                    else
                    {
                        Utility.Alertbox(getContext(),"Info","Please enter Staff Member Name or Staff Member Number","OK");
                    }
                    dialogInterface.dismiss();
                }
            });
            build.create();
            build.show();
        }
        if(id==R.id.bt_refNext)
        {
            Nextbuttonclick();
        }
        if(id==R.id.bt_restaurantmembe)
        {
           // Utility.message(getContext(),"Hi");

            String chechk=et_RestaurantmemberNumber.getText().toString().trim();
            if(!chechk.isEmpty()) {

                String numb = et_RestaurantmemberNumber.getText().toString();
                String refid = "1";
                HashMap<String, String> params = new HashMap<>();
                params.put("ReferrerId", numb);
                params.put("ReferrerType", refid);
                Utility.showLoadingPopup(getActivity());
                new ServiceController(getActivity(), new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data) {
                        if (success) {
                            Utility.hideLoadingPopup();
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(data);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                        String Rname = jsonObjects.getString("RestaurantName");
                                        String Rcity = jsonObjects.getString("City");
                                        // Utility.message(getContext(), "Success");
                                        txtrestaurentname.setText(Rname);
                                        txtcityname.setText(Rcity);
                                        btrefNext.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Utility.Alertbox(getContext(), "Info", "No Result Found.Please Try again.", "OK");
                                    txtname.setText("");
                                    txtcellname.setText("");
                                    if (btrefNext.getVisibility() == View.VISIBLE) {
                                        btrefNext.setVisibility(View.INVISIBLE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ErrorController.showError(getActivity(), data, success);

                        }
                    }
                }).request(ServiceMod.ReferSignUp, params);

            }
            else
            {
                Utility.Alertbox(getContext(),"Error","Please enter Refer Number","OK");
            }

        }
        if(id==R.id.bt_godine_memberId)
        {
          //Utility.message(getContext(),"Hello");
            String chech = godinememberid.getText().toString().trim();
            if (!chech.isEmpty()) {

                String numb = godinememberid.getText().toString();
                String refid = "2";
                HashMap<String, String> params = new HashMap<>();
                params.put("ReferrerId", numb);
                params.put("ReferrerType", refid);
                Utility.showLoadingPopup(getActivity());
                new ServiceController(getActivity(), new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data) {
                        if (success) {
                            JSONArray jsonArray = null;
                            Utility.hideLoadingPopup();
                            try {
                                jsonArray = new JSONArray(data);
                                if(jsonArray.length()>0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                        String name = jsonObjects.getString("Name");
                                        String cell = jsonObjects.getString("CellNo");
                                        String tele=jsonObjects.getString("Telephone");
                                        //  Utility.message(getContext(), "Success");
                                        if(cell.isEmpty())
                                        {
                                            txtcellname.setText(tele);
                                        }
                                        else
                                        {
                                            txtcellname.setText(cell);
                                        }
                                        txtname.setText(name);
                                        btrefNext.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    Utility.Alertbox(getContext(), "Info", "No Result Found.Please Try again.", "OK");
                                    txtname.setText("");
                                    txtcellname.setText("");
                                    if (btrefNext.getVisibility() == View.VISIBLE) {
                                        btrefNext.setVisibility(View.INVISIBLE);
                                    }
                                }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                ErrorController.showError(getActivity(), data, success);
                            }
                        }
                    }).request(ServiceMod.ReferSignUp, params);


            } else {
                Utility.Alertbox(getContext(), "Error", "Please enter Refer Number", "OK");
            }
        }
    }

    public void Nextbuttonclick() {

        Fragment fragment=null;
        FragmentManager manager=  getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Bundle bundle = new Bundle();
        if(Byrestaurant.isChecked())
        {

            String AffiliateId= et_RestaurantmemberNumber.getText().toString();
          StaffName=bt_staff.getText().toString();
            if (!AffiliateId.isEmpty())
            {
                if(ProductVariant !="N/A") {
                    bundle.putString("AffiliateId", AffiliateId);
                    bundle.putString("ProductVariantID", ProductVariant);
                    bundle.putString("StaffName", StaffName);
                    fragment = new Signup_Member_Details();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.frag, fragment);
                    transaction.commit();
                }
                else
                {
                    Utility.Alertbox(getContext(),"Error","Plz Select your Plan","OK");
                }
            }
            else
            {
                Utility.Alertbox(getContext(),"Error","Plz Enter Member Number","OK");
            }
        }
        else if(Bymember.isChecked())
        {

            String AffiliateId= godinememberid.getText().toString();
            StaffName="";
            if(!AffiliateId.isEmpty()) {
                bundle.putString("AffiliateId", AffiliateId);
                bundle.putString("ProductVariantID", ProductVariant);
                bundle.putString("StaffName", StaffName);
                fragment = new Signup_Member_Details();
                fragment.setArguments(bundle);
                transaction.replace(R.id.frag, fragment);
                transaction.commit();
            }
            else
            {
                Utility.Alertbox(getContext(),"Error","Plz Enter Godine Member ID","OK");
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
    {
        int id=textView.getId();
        switch (id) {
            case R.id.Et_restaurantmembernumber:
                String chechk=et_RestaurantmemberNumber.getText().toString().trim();
                if(!chechk.isEmpty())
                {
                    if(i== EditorInfo.IME_ACTION_SEARCH) {
                        InputMethodManager inputMethodManager= (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(),0);
                        String numb = et_RestaurantmemberNumber.getText().toString();
                        String refid = "1";
                        HashMap<String, String> params = new HashMap<>();
                        params.put("ReferrerId", numb);
                        params.put("ReferrerType", refid);
                        Utility.showLoadingPopup(getActivity());
                        new ServiceController(getActivity(), new HttpResponseCallback() {
                            @Override
                            public void response(boolean success, boolean fail, String data) {
                                if (success) {
                                    Utility.hideLoadingPopup();
                                    JSONArray jsonArray = null;
                                    try {
                                        jsonArray = new JSONArray(data);
                                        if(jsonArray.length()>0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                                String Rname = jsonObjects.getString("RestaurantName");
                                                String Rcity = jsonObjects.getString("City");
                                                // Utility.message(getContext(), "Success");
                                                txtrestaurentname.setText(Rname);
                                                txtcityname.setText(Rcity);
                                                btrefNext.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        else
                                        {
                                            Utility.Alertbox(getContext(), "Info", "No Result Found.Please Try again.", "OK");
                                            txtname.setText("");
                                            txtcellname.setText("");
                                            if(btrefNext.getVisibility()==View.VISIBLE) {
                                                btrefNext.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    ErrorController.showError(getActivity(), data, success);

                                }
                            }
                        }).request(ServiceMod.ReferSignUp, params);
                        return true;
                    }
                }
                else
                {
                    Utility.Alertbox(getContext(),"Error","Please enter Refer Number","OK");
                }
                break;

            case R.id.et_godine_memberId:

                String chech = godinememberid.getText().toString().trim();
                if (!chech.isEmpty()) {
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        InputMethodManager inputManager= (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(textView.getWindowToken(),0);
                        String numb = godinememberid.getText().toString();
                        String refid = "2";
                        HashMap<String, String> params = new HashMap<>();
                        params.put("ReferrerId", numb);
                        params.put("ReferrerType", refid);
                        Utility.showLoadingPopup(getActivity());
                        new ServiceController(getActivity(), new HttpResponseCallback() {
                            @Override
                            public void response(boolean success, boolean fail, String data) {
                                if (success) {
                                    JSONArray jsonArray = null;
                                    Utility.hideLoadingPopup();
                                    try {
                                        jsonArray = new JSONArray(data);
                                        if(jsonArray.length()>0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                                String name = jsonObjects.getString("Name");
                                                String cell = jsonObjects.getString("CellNo");
                                                String tele=jsonObjects.getString("Telephone");
                                                //  Utility.message(getContext(), "Success");
                                                if(cell.isEmpty())
                                                {
                                                    txtcellname.setText(tele);
                                                }
                                                else
                                                {
                                                    txtcellname.setText(cell);
                                                }
                                                txtname.setText(name);
                                                btrefNext.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        else {
                                            Utility.Alertbox(getContext(),"Info","No Result Found.Please Try again.","OK");
                                            txtname.setText("");
                                            txtcellname.setText("");
                                            if(btrefNext.getVisibility()==View.VISIBLE) {
                                                btrefNext.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    ErrorController.showError(getActivity(), data, success);
                                }
                            }
                        }).request(ServiceMod.ReferSignUp, params);
                        return true;
                    }
                } else {
                    Utility.Alertbox(getContext(), "Error", "Please enter Refer Number", "OK");
                }
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode==2)
            {
                String name=data.getStringExtra("Name");
                String cell=data.getStringExtra("Cell");
                AffiliateId=data.getStringExtra("UserId");
                if (!name.isEmpty() && !cell.isEmpty())
                {
                    txtname.setText(name);
                    txtcellname.setText(cell);
                    godinememberid.setText( AffiliateId);
                    btrefNext.setVisibility(View.VISIBLE);
                }
            }
            if(requestCode==3)
            {
                layout_referedbymember.setVisibility(View.GONE);
                String Restaurantname=data.getStringExtra("Restaurantname");
                String Cityname=data.getStringExtra("Cityname");
                AffiliateId=data.getStringExtra("Id");
                if (!Restaurantname.isEmpty() && !Cityname.isEmpty() )
                {
                    Byrestaurant.setChecked(true);
                    layout_referedbyrestaurant.setVisibility(View.VISIBLE);
                    txtrestaurentname.setText(Restaurantname);
                    txtcityname.setText(Cityname);
                    et_RestaurantmemberNumber.setText( AffiliateId);
                    btrefNext.setVisibility(View.VISIBLE);
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        planame= (TextView) getView().findViewById(R.id.planname);
        Step= (TextView) getView().findViewById(R.id.step);
        Step.setText("STEP #2");
        planame.setText("Choose Referrer");
    }
}
