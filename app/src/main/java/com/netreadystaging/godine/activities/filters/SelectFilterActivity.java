package com.netreadystaging.godine.activities.filters;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.itsolutionts.filterhelper.activity.FilterActivity;
import com.netreadystaging.godine.R;

import com.netreadystaging.godine.adapters.CuisinesAdapter;
import com.netreadystaging.godine.adapters.FeaturesAdapter;
import com.netreadystaging.godine.adapters.PriceSchemesAdapter;
import com.netreadystaging.godine.adapters.RatingSchemesAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.ServiceMod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

public class SelectFilterActivity extends FilterActivity {
    private String filter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void setupToolbar()
    {
        ActionBar actionBar =  getSupportActionBar() ;
        if(actionBar!=null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.filter_toolbar);
            Toolbar toolbar = (Toolbar) actionBar.getCustomView() ;
            final ImageView ivToolBarBack = (ImageView) toolbar.findViewById(R.id.ivToolBarBack) ;
            ivToolBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            final ImageView ivToolBarDone = (ImageView) toolbar.findViewById(R.id.ivToolBarDone) ;
            ivToolBarDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnResponse();
                }
            });
            final TextView tvTitle = (TextView) toolbar.findViewById(R.id.tvTitle) ;
            tvTitle.setText(filter);
        }
    }

    @Override
    public void loadFilterData() {
        filter = getIntent().getStringExtra("filter");
        setupToolbar();
        switch(filter.toLowerCase())
        {
            case  "cuisines" :
                tvFilterLabel.setText(getResources().getString(R.string.select_rest_cuisines));
                loadRestCuisines() ;
                break ;

            case  "features" :
                tvFilterLabel.setText(getResources().getString(R.string.select_rest_features));
                loadRestFeatures();
                break ;

            case  "price" :
                tvFilterLabel.setText(getResources().getString(R.string.select_rest_price));
                loadRestPriceSchemes();
                break ;

            case  "rating" :
                tvFilterLabel.setText(getResources().getString(R.string.select_rest_rating));
                loadRestRatingSchemes();
                break ;
        }
    }

    private void loadRestRatingSchemes() {

        final  ArrayList<String> listOfRatingSchemes = new ArrayList<>();
        listOfRatingSchemes.add("1");
        listOfRatingSchemes.add("2");
        listOfRatingSchemes.add("3");
        listOfRatingSchemes.add("4");
        listOfRatingSchemes.add("5");

        RatingSchemesAdapter ratingSchemesAdapter = new RatingSchemesAdapter(SelectFilterActivity.this,R.layout.advance_rating_chk_item,listOfRatingSchemes);
        ratingSchemesAdapter.notifyDataSetChanged();
        lvFilters.setAdapter(ratingSchemesAdapter);
    }

    private void loadRestPriceSchemes() {
        final  ArrayList<String> listOfPriceSchemes = new ArrayList<>();
        listOfPriceSchemes.add("$11-$30($$)");
        listOfPriceSchemes.add("$31-$60($$$)");
        listOfPriceSchemes.add("$61+");

        PriceSchemesAdapter priceSchemesAdapter = new PriceSchemesAdapter(SelectFilterActivity.this,R.layout.advance_str_chk_item,listOfPriceSchemes);
        priceSchemesAdapter.notifyDataSetChanged();
        lvFilters.setAdapter(priceSchemesAdapter);

    }

    private void loadRestFeatures() {
        final  ArrayList<String> listOfFeatures = new ArrayList<>();
        final HashMap<String,String> params=new HashMap<>();

        new ServiceController(SelectFilterActivity.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                if(success)
                {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject featureJObj  = jsonArray.getJSONObject(i);
                                String feature  = featureJObj.getString("Feature") ;
                                listOfFeatures.add(feature);
                            }
                        }
                        FeaturesAdapter featuresAdapter  = new FeaturesAdapter(SelectFilterActivity.this,R.layout.advance_str_chk_item,listOfFeatures);
                        lvFilters.setAdapter(featuresAdapter);
                        featuresAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                else
                {
                    ErrorController.showError(SelectFilterActivity.this,data,success);
                }


            }
        }).request(ServiceMod.FEATURES_LISTING,params);

    }

    private void loadRestCuisines() {
        final ArrayList<String> listOfCuisinesTypes = new ArrayList<>();
        final HashMap<String,String> params=new HashMap<>();
        new ServiceController(SelectFilterActivity.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data){
                if(success){
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cuisineJObj  = jsonArray.getJSONObject(i);
                                String cuisine  = cuisineJObj.getString("Cuisine") ;
                                listOfCuisinesTypes.add(cuisine);
                            }
                        }
                        CuisinesAdapter cuisinesAdapter  = new CuisinesAdapter(SelectFilterActivity.this,R.layout.advance_str_chk_item,listOfCuisinesTypes);
                        lvFilters.setAdapter(cuisinesAdapter);
                        cuisinesAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(SelectFilterActivity.this,data,success);
                }


            }
        }).request(ServiceMod.CUISINES_LISTING,params);
    }

    private void returnResponse()
    {
        Intent intent = null ;
        switch(filter.toLowerCase())
        {
            case  "cuisines" :
                ArrayList<String> selectedCuisines = ((CuisinesAdapter) lvFilters.getAdapter()).getSelectedList() ;
                intent  = new Intent();
                intent.putExtra("response",ALToString(selectedCuisines));

                break ;

            case  "features" :
                ArrayList<String> selectedFeatures = ((FeaturesAdapter) lvFilters.getAdapter()).getSelectedList() ;
                intent  = new Intent();
                intent.putExtra("response",ALToString(selectedFeatures));

                break ;

            case  "price" :
                String selectedPriceScheme =  ((PriceSchemesAdapter)lvFilters.getAdapter()).getSelectedItem() ;
                intent  = new Intent();
                intent.putExtra("response",selectedPriceScheme);

                break ;

            case  "rating" :
                String selectedRatingScheme =  ((RatingSchemesAdapter)lvFilters.getAdapter()).getSelectedItem() ;
                intent  = new Intent();
                intent.putExtra("response",selectedRatingScheme);

                break ;
        }
        setResult(FILTER_RESPONSE_CODE,intent);
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out_bottom);
    }

    @Override
    public void onClick(View v) {
       returnResponse();
    }


}
