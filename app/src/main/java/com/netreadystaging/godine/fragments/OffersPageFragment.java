package com.netreadystaging.godine.fragments;
import android.app.Activity;
import android.content.DialogInterface;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.netreadystaging.godine.R;

import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.adapters.OfferAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.OfferList;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 19-07-2016.
 */
public class OffersPageFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    RelativeLayout containe;
    public  String selectedmile="";
    EditText etname,etzipcode;
    View view ;
    ListView listfavres;
    TextView title ;
    Spinner Smiles;
    CheckBox choffer;
    ArrayList<String> miles;
    ArrayAdapter<String> spinneradapter;
    ArrayList<OfferList> nearbylist = new ArrayList<>();
    OfferAdapter offerAdapter;
    private boolean isCurrentLocationSearch;
    GoogleApiClient mgoogleApiclient;
    private double currentLat;
    private double currentLng;
    Button searchrestaurant;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.offers_page_fragment,container,false);
        choffer= (CheckBox) view.findViewById(R.id.Ch_offer);
        listfavres= (ListView) view.findViewById(R.id.listofrest);
        Smiles = (Spinner) view.findViewById(R.id.spinmiles);
        searchrestaurant= (Button) view.findViewById(R.id.searchrestaurant);
        etname= (EditText) view.findViewById(R.id.et_offname);
        etzipcode= (EditText) view.findViewById(R.id.et_offzipcode);
        searchrestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!choffer.isChecked()) {
                    Searchoffer();
                }
                else
                {
             //   Utility.message(getContext(),selectedmile);
                    Location mLocation= LocationServices.FusedLocationApi.getLastLocation(mgoogleApiclient);
                    if(mLocation!=null)
                    {
                        currentLat = mLocation.getLatitude();
                        currentLng = mLocation.getLongitude();
                        Log.d("Current Location",currentLat+","+currentLng);
                        String name= "";
                        String zipcode="";
                        String latitude = "" ;
                        String longitude="";
                        String miless= selectedmile;
                       if(isCurrentLocationSearch){
                            latitude = currentLat+"" ;
                            longitude= currentLng+"";
                          miless= selectedmile;
                        }
                        loadRestaurants(name,zipcode,latitude,longitude,miless);
                    }
                }
            }
        });

        offerAdapter = new OfferAdapter(getContext(), R.layout.offer_row, nearbylist);
        listfavres.setAdapter(offerAdapter);
        listfavres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ids=((TextView)view.findViewById(R.id.listids)).getText().toString();
              //  Utility.message(getContext(),ids);
                RestaurantProfile fragment = new RestaurantProfile();
                Bundle restBundle = new Bundle();
                restBundle.putSerializable("rest_id",ids);
                fragment.setArguments(restBundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).addToBackStack(null).commit() ;
            }
        });
        setupGui();
        containe= (RelativeLayout) view.findViewById(R.id.gdcontainer);
        containe.setVisibility(View.GONE);
        isCurrentLocationSearch = true;
        if (Utility.checkGooglePlayService(getActivity())) {
            setupLocation();
        }
        setupToolBar();
        return view ;
    }

    private void Searchoffer() {
        Utility.showLoadingPopup(getActivity());
        String latitude = "0";
        String longitude = "0";
        String miles = "0";
        final String nam=etname.getText().toString();
        String zipcode=etzipcode.getText().toString();
        if(!nam.isEmpty() || !zipcode.isEmpty()) {
            final HashMap<String, String> params = new HashMap<>();
            params.put("RestaurantNameOrCity", nam);
            params.put("ZipCode", zipcode);
            params.put("Lat", latitude);
            params.put("Lng", longitude);
            params.put("distance", miles);

            new ServiceController(getContext(), new HttpResponseCallback() {
                @Override
                public void response(boolean success, boolean fail, String data) {
                    Utility.hideLoadingPopup();
                    if (success) {
                        Log.d("Muhib", data);
                        //Utility.message(getContext(), data);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(data);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                    OfferList offerListObj = new OfferList();
                                    offerListObj.setRestaurantId(jsonObjects.getString("RestaurantId"));
                                    offerListObj.setRestaurantName(jsonObjects.getString("RestaurantName"));
                                    offerListObj.setImage(jsonObjects.getString("Image"));
                                    // Log.d("In offer",jsonObjects.getString("Image"));
                                    offerListObj.setOffer(jsonObjects.getString("Offer"));
                                    offerListObj.setOfferName(jsonObjects.getString("OfferName"));
                                    offerListObj.setDescription(jsonObjects.getString("Description"));
                                    offerListObj.setOfferEndDate(jsonObjects.getString("OfferEndDate"));
                                    nearbylist.add(offerListObj);
                                    containe.setVisibility(View.GONE);
                                    Activity activity = getActivity();
                                    Toolbar toolBar = (Toolbar) activity.findViewById(R.id.toolbar);
                                    ImageView ivToolBarEndIcn = (ImageView) toolBar.findViewById(R.id.ivToolBarEndIcn);
                                    ivToolBarEndIcn.setImageResource(R.drawable.search_icn_toolbar);
                                    ivToolBarEndIcn.setVisibility(View.VISIBLE);
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Info");
                                builder.setCancelable(false);
                                builder.setMessage("No Restaurant Found with any offer for searched option.Please try something else");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                            offerAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ErrorController.showError(getContext(), data, success);
                    }
                }
            }).request(ServiceMod.OFFER_LIST, params);
        }
        else
        {
            Utility.Alertbox(getContext(),"Error","Please Search by any parameter.","OK");
            Utility.hideLoadingPopup();
        }
    }

    private void setupGui() {
        Smiles.setEnabled(false);
        miles = new ArrayList<>();
        miles.add("Miles");
        miles.add("5");
        miles.add("10");
        miles.add("20");
        miles.add("30");
        miles.add("40");
        miles.add("50");
        miles.add("100");
        spinneradapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, miles);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Smiles.setAdapter(spinneradapter);
        Smiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedmile = (String) adapterView.getItemAtPosition(i);
                if (i <=0) {
                    selectedmile="0";
                }
                else
                {
                    selectedmile=(String) adapterView.getItemAtPosition(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        choffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!choffer.isChecked()) {
                    Smiles.setEnabled(false);
                }
                else {
                    Smiles.setEnabled(true);
                }
            }
        });
    }
    protected synchronized void setupLocation() {
        mgoogleApiclient=new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if(mgoogleApiclient!=null)
        {
            mgoogleApiclient.connect();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        final ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setImageResource(R.drawable.search_icn_toolbar);
        ivToolBarBack.setVisibility(View.GONE);
        ivToolBarEndIcn.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containe.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                if (nearbylist != null) {
                    nearbylist.clear();
                }
            }
        });
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText("Exclusive GoDine Offers");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLocation= LocationServices.FusedLocationApi.getLastLocation(mgoogleApiclient);
        if(mLocation!=null)
        {
            currentLat = mLocation.getLatitude();
            currentLng = mLocation.getLongitude();
            Log.d("Current Location",currentLat+","+currentLng);
            String name= "";
            String zipcode="";
            String latitude = "" ;
            String longitude="";
            String miless= "100";
           if(isCurrentLocationSearch){
                latitude = currentLat+"" ;
                longitude= currentLng+"";
                miless= AppGlobal.getInatance().getMiles();
            }
            loadRestaurants(name,zipcode,latitude,longitude,miless);
        }
    }

    private void loadRestaurants(final String name, final String zipcode, final String latitude, final String longitude, String Miles) {
        Utility.showLoadingPopup(getActivity());
        if (nearbylist != null) {
            nearbylist.clear();
        }
        // For Testing
       // Miles = "0";
        final HashMap<String, String> params = new HashMap<>();
        params.put("RestaurantNameOrCity", name);
        params.put("ZipCode", zipcode);
        params.put("lat", latitude);
        params.put("lng", longitude);
        params.put("distance", Miles);
        new ServiceController(getContext(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                Utility.hideLoadingPopup();
                if (success) {
                    Log.d("Muhib", data);
                    // Utility.message(getContext(), data);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                OfferList offerListObj = new OfferList();
                                offerListObj.setRestaurantId(jsonObjects.getString("RestaurantId"));
                                offerListObj.setRestaurantName(jsonObjects.getString("RestaurantName"));
                                offerListObj.setImage(jsonObjects.getString("Image"));
                                // Log.d("In offer",jsonObjects.getString("Image"));
                                offerListObj.setOffer(jsonObjects.getString("Offer"));
                                offerListObj.setOfferName(jsonObjects.getString("OfferName"));
                                offerListObj.setDescription(jsonObjects.getString("Description"));
                                offerListObj.setOfferEndDate(jsonObjects.getString("OfferEndDate"));
                                nearbylist.add(offerListObj);
                                containe.setVisibility(View.GONE);
                                Activity activity = getActivity();
                                Toolbar toolBar = (Toolbar) activity.findViewById(R.id.toolbar);
                                ImageView ivToolBarEndIcn = (ImageView) toolBar.findViewById(R.id.ivToolBarEndIcn);
                                ivToolBarEndIcn.setImageResource(R.drawable.search_icn_toolbar);
                                ivToolBarEndIcn.setVisibility(View.VISIBLE);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Info");
                            builder.setCancelable(false);
                            builder.setMessage("No Restaurant Found with any offer for searched option.Please try something else");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create();
                            builder.show();
                        }
                        offerAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ErrorController.showError(getContext(), data, success);
                }
            }
        }).request(ServiceMod.OFFER_LIST, params);    }

    private long calculateMiles(double lat, double lng) {
        Location locationA = new Location("point A");

        locationA.setLatitude(currentLat);
        locationA.setLongitude(currentLng);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat);
        locationB.setLongitude(lng);

        float distance = locationA.distanceTo(locationB);
        long miles =(long)(distance*0.000621371) ;
        return miles;
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
