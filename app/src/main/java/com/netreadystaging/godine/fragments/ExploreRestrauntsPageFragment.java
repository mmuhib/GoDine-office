package com.netreadystaging.godine.fragments;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.adapters.RestTypeAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;


import com.netreadystaging.godine.models.RestaurantType;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.netreadystaging.godine.models.Restaurant;
import in.technobuff.helper.http.HttpResponseCallback;
import in.technobuff.helper.utils.PermissionRequestHandler;

/**
 * Created by sony on 19-07-2016.
 */
public class ExploreRestrauntsPageFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    View view ;
    EditText etNameOfRestaurant ,etRestaurantCity,etRestaurantZipcode,etMiles ;
    Spinner etTypeOfCuisine,etRestaurantFeature;
    private boolean isCurrentLocationSearch;
    GoogleApiClient mgoogleApiclient;
    private double currentLat;
    private double currentLng;
    private ArrayList<Restaurant> restlist;
    private ArrayList<String> listOfCuisinesTypes;
    private ArrayAdapter<String> cuisinesAdapter;
    private ArrayAdapter<String> featuresAdapter;
    private ArrayList<RestaurantType> listOfRestTypes;
    private ArrayList<String> listOfFeatures;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.explore_restaurant_page_fragment,container,false);
        setupToolBar();
        setupGUI() ;
        return view ;
    }

    private void setupGUI() {
        etNameOfRestaurant = (EditText)view.findViewById(R.id.etNameOfRestaurant) ;
        etRestaurantCity = (EditText) view.findViewById(R.id.etRestaurantCity) ;
        etRestaurantZipcode = (EditText)view.findViewById(R.id.etRestaurantZipcode) ;
        etMiles = (EditText)view.findViewById(R.id.etMiles) ;

        final TextView btnClearFields = (TextView) view.findViewById(R.id.btnClearFields) ;
        btnClearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNameOfRestaurant.setText("");
                etRestaurantCity.setText("");
                etRestaurantZipcode.setText("");
                etMiles.setText("");
            }
        });
        final TextView btnCurrentLocationSearch = (TextView)view.findViewById(R.id.btnCurrentLocationSearch) ;
        final TextView btnOpenSearchOption = (TextView)view.findViewById(R.id.btnOpenSearchOption) ;
        final TextView btnSearchRestaurants = (TextView)view.findViewById(R.id.btnSearchRestaurants) ;
        btnCurrentLocationSearch.setOnClickListener(this);
        btnSearchRestaurants.setOnClickListener(this);

//        isCurrentLocationSearch = true ;
//        if (Utility.checkGooglePlayService(getActivity())) {
//            setupLocation();
//        }

        etTypeOfCuisine = (Spinner)view.findViewById(R.id.etTypeOfCuisine) ;

        listOfCuisinesTypes = new ArrayList<>();
        listOfCuisinesTypes.add("Types of Cuisines");
        cuisinesAdapter  = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,listOfCuisinesTypes);
        cuisinesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etTypeOfCuisine.setAdapter(cuisinesAdapter);
        cuisinesAdapter.notifyDataSetChanged();

        etRestaurantFeature = (Spinner)view.findViewById(R.id.etRestaurantFeature) ;
        listOfFeatures = new ArrayList<>();
        listOfFeatures.add("Features");
        featuresAdapter  = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,listOfFeatures);
        featuresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etRestaurantFeature.setAdapter(featuresAdapter);
        featuresAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showRestaurantTypesAlert();

        loadTypesOfCuisines();
        loadRestFeatures();
    }

    private void loadRestaurantTypes(final ListView lv,final ProgressBar progressBar) {

        listOfRestTypes = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String,String> params=new HashMap<>();
        new ServiceController(getActivity(), new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                progressBar.setVisibility(View.GONE);
                if(success)
                {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject featureJObj  = jsonArray.getJSONObject(i);
                                RestaurantType restaurantType = new RestaurantType();
                                String restType  = featureJObj.getString("Restaurant Type") ;
                                switch (restType)
                                {
                                    case "Premier Restuarant":
                                        restType = "Premier Partner Restaurants\nGet 50% Off your Entrée every time you dine out." ;
                                        break ;

                                    case "Affiliate Restaurant" :
                                        restType = "Other Dining Options\nGet 30% Off your Entree, or more, every time you dine out." ;
                                        break ;
                                }

                                String roleId  = featureJObj.getString("RoleID") ;
                                restaurantType.setRestType(restType);
                                restaurantType.setRoleId(roleId);
                                listOfRestTypes.add(restaurantType);
                            }
                        }
                        RestTypeAdapter restTypesAdapter  = new RestTypeAdapter(getActivity(),listOfRestTypes);
                        lv.setAdapter(restTypesAdapter);
                        restTypesAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }


            }
        }).request(ServiceMod.REST_TYPE_LISTING,params);
    }
    private String  restRoleId ;
    private void showRestaurantTypesAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewRestTypes  =  LayoutInflater.from(getActivity()).inflate(R.layout.popup_rest_types,null) ;
        final TextView lblHeading = (TextView) viewRestTypes.findViewById(R.id.lblHeading);
        lblHeading.setText(getResources().getString(R.string.select_rest_type));
        final ListView lvRestTypes = (ListView) viewRestTypes.findViewById(R.id.lvRestTypes);
        final ProgressBar progressBar = (ProgressBar) viewRestTypes.findViewById(R.id.progressBar);
        loadRestaurantTypes(lvRestTypes,progressBar);
        builder.setView(viewRestTypes);
        builder.setCancelable(false) ;
        final Dialog dialog = builder.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_BACK)
                {

                        ProfilePageFragment frag=new ProfilePageFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,frag).addToBackStack(null).commit();
                        dialog.dismiss();
                          return true;
                }
                return false;
            }
        });

        lvRestTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RestaurantType restaurantType = (RestaurantType) parent.getAdapter().getItem(position);
                restRoleId = restaurantType.getRoleId() ;
                dialog.dismiss();
            }
        });
        dialog.show() ;

       // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    private void loadRestFeatures() {
        listOfFeatures = new ArrayList<>();
        listOfFeatures.add("Features");
        final HashMap<String,String> params=new HashMap<>();

        new ServiceController(getActivity(), new HttpResponseCallback()
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
                        featuresAdapter  = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,listOfFeatures);
                        featuresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        etRestaurantFeature.setAdapter(featuresAdapter);
                        featuresAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.FEATURES_LISTING,params);
    }

    private void loadTypesOfCuisines() {
        listOfCuisinesTypes = new ArrayList<>();
        listOfCuisinesTypes.add("Types of Cuisines");
        final HashMap<String,String> params=new HashMap<>();
        new ServiceController(getActivity(), new HttpResponseCallback()
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
                        cuisinesAdapter  = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,listOfCuisinesTypes);
                        cuisinesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        etTypeOfCuisine.setAdapter(cuisinesAdapter);
                        cuisinesAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }


            }
        }).request(ServiceMod.CUISINES_LISTING,params);
    }

    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setImageResource(R.drawable.search_icn_toolbar);
        ivToolBarEndIcn.setVisibility(View.GONE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);

        ivToolBarEndIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);

    }
    TextView title ;

    @Override
    public void onResume() {
        super.onResume();
        title.setText("Restaurant Search");
        if(PermissionRequestHandler.requestPermissionToLocation(getActivity(),ExploreRestrauntsPageFragment.this)) {
            ((MainPageActivity) getActivity()).checkGPSStatus();
        }
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
            String address="";
            String latitude = "" ;
            String longitude="";
            String miles;
            String cuisine= "";
            String feature= "";

            if(isCurrentLocationSearch){
                latitude = currentLat+"" ;
                longitude= currentLng+"";
                miles= AppGlobal.getInatance().getMiles();
            }
            else {
                if(etTypeOfCuisine.getSelectedItemPosition()>0)
                {
                    cuisine = (String) etTypeOfCuisine.getSelectedItem() ;
                }

                if(etRestaurantFeature.getSelectedItemPosition()>0)
                {
                    feature = (String) etRestaurantFeature.getSelectedItem() ;
                }
                name = etNameOfRestaurant.getText().toString();
                StringBuilder builder = new StringBuilder();
                String  city = etRestaurantCity.getText().toString();
                String zipcode = etRestaurantZipcode.getText().toString();
                if(!city.isEmpty()){
                    builder.append(city);
                    if(!zipcode.isEmpty()){builder.append(",").append(zipcode);}
                }
                else{ if(!zipcode.isEmpty()){builder.append(zipcode);}}

                address =  builder.toString() ;

                miles = etMiles.getText().toString();
                if(miles.trim().isEmpty() && !city.isEmpty())
                {
                    miles =  AppGlobal.getInatance().getMiles() ;
                    etMiles.setText(miles);
                }else if(miles.trim().isEmpty()){
                    miles = "0" ;
                }

                if (name.isEmpty() && cuisine.isEmpty() && feature.isEmpty() && city.isEmpty() && zipcode.isEmpty()) {
                    Toast.makeText(getActivity(), "Please search by one parameter", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
            loadRestaurants(name,cuisine,feature,address,latitude,longitude,miles,restRoleId);
        }
    }
    private void loadRestaurants(String name, String cuisine, String feature,
                                 String address, String latitude, String longitude, String miles,final String restRoleId) {
        Utility.showLoadingPopup(getActivity());
        restlist =  new ArrayList<>();
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantName",name);
        params.put("lat",latitude);
        params.put("lng",longitude);
        params.put("Miles",miles);
        params.put("Features",feature);
        params.put("Address",address);
        params.put("CuisineType",cuisine);
        params.put("RestaurantType",restRoleId);
        params.put("UserId",AppGlobal.getInatance().getUserId()+"");
        new ServiceController(getActivity(), new HttpResponseCallback()
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
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                Restaurant restaurantObj = new Restaurant();
                                restaurantObj.setImage(jsonObjects.getString("RestaurantImage"));
                                restaurantObj.setName(jsonObjects.getString("Name"));
                                restaurantObj.setId(jsonObjects.getString("Id"));
                                restaurantObj.setReview(jsonObjects.getString("NumberOfReviews"));
                                restaurantObj.setAddress(jsonObjects.getString("Address"));
                                StringBuffer area = new StringBuffer();
                                area.append(jsonObjects.getString("Region"));
                                area.append(", " + jsonObjects.getString("City"));
                                area.append(", " + jsonObjects.getString("PostalCode"));
                                restaurantObj.setArea("" + area);
                                restaurantObj.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
                                restaurantObj.setRestaurantFeatures(jsonObjects.getString("RestaurantFeatures"));
                                restaurantObj.setLunch(jsonObjects.getString("RestaurantAverageLunch"));
                                restaurantObj.setDinner(jsonObjects.getString("RestaurantAverageDinner"));
                                restaurantObj.setRating((float)jsonObjects.getDouble("Rating"));
                                double lat = jsonObjects.getDouble("Latitude");
                                double lng = jsonObjects.getDouble("Longitude");
                                long miles =  calculateMiles(lat,lng);
                                restaurantObj.setMiles(miles) ;
                                int availableOffers = jsonObjects.getInt("IsOfferAvailable");
                                restaurantObj.setOffers(availableOffers);
                                restaurantObj.lat = lat ;
                                restaurantObj.lng = lng ;
                                restaurantObj.currentLat = currentLat ;
                                restaurantObj.currentLng =currentLng ;
                                restlist.add(restaurantObj);
                            }
                            String restType = "";
                            switch (Integer.parseInt(restRoleId))
                            {
                                case 253 :
                                    restType = "Premier Restaurants";
                                    break ;

                                case 284 :
                                    restType = "Affiliate Restaurants";
                                    break ;
                            }
                            RestaurantSearchFilterFragment fragment = new RestaurantSearchFilterFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("restaurants",restlist);
                            bundle.putString("restType",restType);
                            fragment.setArguments(bundle);
                            FragmentManager fm =  getActivity().getSupportFragmentManager() ;
                            fm.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                        }
                        else
                        {
                          /*  AlertDialog.Builder builder =  new AlertDialog.Builder();
                          builder.setTitle("Info");
                            builder.setMessage();
                           builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           });
                          builder.create();
                            builder.show();*/
                            Utility.Alertbox(getActivity(),"Info","No Restaurant Found Nearby, Please use other search option.","OK");
                        }
                    } catch (JSONException e) {
                        ErrorController.showError(getActivity(),data,true);
                    }
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.SEARCH_RESTAURANT,params);
    }
    private long calculateMiles(double lat, double lng) {
        Location locationA = new Location("point A");

        locationA.setLatitude(currentLat);
        locationA.setLongitude(currentLng);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat);
        locationB.setLongitude(lng);

        float distance = locationA.distanceTo(locationB);
        return (long)(distance*0.000621371) ;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnCurrentLocationSearch :
                isCurrentLocationSearch  = true ;
                break ;

            case R.id.btnSearchRestaurants :
                isCurrentLocationSearch = false ;
                break ;
        }

        if (Utility.checkGooglePlayService(getActivity()))
        {
            setupLocation();
        }
    }

    protected synchronized void setupLocation() {
        mgoogleApiclient=new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if(mgoogleApiclient!=null)
        {
            mgoogleApiclient.connect();
        }
    }
    private static final int PERMISSIONS_REQUEST_LOCATION = 2 ;
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                }
                return;
            }
        }
    }
}
