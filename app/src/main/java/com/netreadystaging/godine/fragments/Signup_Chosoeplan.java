package com.netreadystaging.godine.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.Join_GoDine;
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
import static com.netreadystaging.godine.activities.main.Join_GoDine.ProductVariantIDD;


/**
 * Created by sony on 05-12-2016.
 */

public class Signup_Chosoeplan extends Fragment implements View.OnClickListener {
    View view;
    public  static  Button btjustone,btplusone,btplusthree;
    Button bt_chooseplan,bt_refferdetails,bt_memberdetails;
    ImageView image_justone,image_plusone,image_plusthree;
    String justone,plusone,plusthree;
    TextView planame,Step;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Join_GoDine.ProductVariantIDD=0;
        view=inflater.inflate(R.layout.signup_choose_plan,container,false);
        btjustone= (Button) view.findViewById(R.id.bt_justone);
        btplusone= (Button) view.findViewById(R.id.bt_plusone);
        btplusthree= (Button) view.findViewById(R.id.bt_plusthee);
        image_justone= (ImageView) view.findViewById(R.id.image19);
        image_plusone=(ImageView) view.findViewById(R.id.image29);
        image_plusthree= (ImageView) view.findViewById(R.id.image49);

        btjustone.setOnClickListener(this);
        btplusone.setOnClickListener(this);
        btplusthree.setOnClickListener(this);
        image_justone.setOnClickListener(this);
        image_plusone.setOnClickListener(this);
        image_plusthree.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        planame= (TextView) getView().findViewById(R.id.planname);
        Step= (TextView) getView().findViewById(R.id.step);
        Step.setText("STEP #1");
        planame.setText("Choose Membership Level");
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.showLoadingPopup(getActivity());

        HashMap<String,String> params=new HashMap<>();
        new ServiceController(getContext(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                if(success)
                {
                    Utility.hideLoadingPopup();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObjects = jsonArray.getJSONObject(i);
                            ProductVariantIDD=jsonObjects.getInt("ProductVariantID");
                            if(ProductVariantIDD==20)
                            {
                                justone="20";
                            }
                            else if (ProductVariantIDD==21)
                            {
                                plusone="21";
                            }
                            else if(ProductVariantIDD==22)
                            {
                                plusthree="22";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(getContext(),data,success);
                }
            }
        }).request(ServiceMod.ProductVariants,params);

    }

    @Override
    public void onClick(View view)
    {
        Fragment fragment = null;
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();

        int id = view.getId();
        switch (id) {
            case R.id.bt_justone :
               case R.id.image19:
                bundle.putString("Product", justone);
                fragment = new Signup_Referrer_Details();
                Join_GoDine.ProductVariantIDD=20;
                fragment.setArguments(bundle);

                break;
            case R.id.bt_plusone:
            case R.id.image29:
                bundle.putString("Product", plusone);
                fragment = new Signup_Referrer_Details();
                Join_GoDine.ProductVariantIDD=21;
                fragment.setArguments(bundle);

                break;
            case R.id.bt_plusthee:
            case R.id.image49:
                bundle.putString("Product", plusthree);
                fragment = new Signup_Referrer_Details();
                Join_GoDine.ProductVariantIDD=22;
                fragment.setArguments(bundle);
                break;
        }
        transaction.replace(R.id.frag, fragment);
        transaction.commit();
    }




}


