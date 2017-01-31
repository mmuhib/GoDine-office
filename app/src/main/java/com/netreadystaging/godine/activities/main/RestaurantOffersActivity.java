package com.netreadystaging.godine.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.adapters.OfferAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.OfferList;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

public class RestaurantOffersActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_offers);
        setupTopBar();

        Intent intent =  getIntent() ;
        if(intent!=null)
        {
            final String restId = intent.getStringExtra("rest_id");
            if(Utility.isNetworkConnected(RestaurantOffersActivity.this)) {
                loadRestaurantOffers(restId);
            }
        }

    }


    private void loadRestaurantOffers(final String restId) {
        final ListView lv_rest_offer  = (ListView) findViewById(R.id.lv_rest_offer);
        final ArrayList<OfferList> listRestOffers = new ArrayList<>();

        Utility.showLoadingPopup(RestaurantOffersActivity.this);
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantId",restId);
        new ServiceController(RestaurantOffersActivity.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                Utility.hideLoadingPopup();
                if(success)
                {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OfferList restOffer = new OfferList();
                                JSONObject offerSuccessObj  =  jsonArray.getJSONObject(i);
                                restOffer.setOfferId(offerSuccessObj.getString("OfferId"));
                                restOffer.setOfferName(offerSuccessObj.getString("OfferName"));
                                restOffer.setOffer(offerSuccessObj.getString("Offer"));
                                restOffer.setDescription(offerSuccessObj.getString("Description"));
                                restOffer.setImage(offerSuccessObj.getString("OfferImage"));
                                restOffer.setOfferEndDate(offerSuccessObj.getString("OfferEndDate"));
                                listRestOffers.add(restOffer) ;
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            AlertDialog.Builder builder =  new AlertDialog.Builder(RestaurantOffersActivity.this);
                            builder.setMessage("No offers,please check back soon.") ;
                            builder.setTitle("Info");
                            builder.create() ;
                            builder.show() ;
                        }

                        final OfferAdapter offerAdapter = new OfferAdapter(getApplicationContext(), R.layout.offer_row, listRestOffers);
                        lv_rest_offer.setAdapter(offerAdapter);
                        offerAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(RestaurantOffersActivity.this,data,success);
                }
            }
        }).request(ServiceMod.RESTAURANT_OFFER,params);
    }

    private void setupTopBar()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView ivToolBarNavigationIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        final ImageView ivToolBarBack = (ImageView)toolbar.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        final TextView title= (TextView)toolbar.findViewById(R.id.tvToolBarMiddleLabel) ;
        title.setVisibility(View.VISIBLE);
        title.setText("Restaurant Offers");
        final ImageView ivToolBarEndIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarEndIcn.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
    }


}
