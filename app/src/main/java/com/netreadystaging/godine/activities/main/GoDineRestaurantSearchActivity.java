package com.netreadystaging.godine.activities.main;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.onboard.LoginActivity;
import com.netreadystaging.godine.activities.onboard.Splash2;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.adapters.SearchRestaurantAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;

import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;
import in.technobuff.helper.utils.PermissionRequestHandler;

public class GoDineRestaurantSearchActivity extends AppBaseActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mgoogleApiclient;

    ScrollView gd_rest_search_filter_container  ;
    TextView mTitle;

    ArrayList<Restaurant> restlist;
    SearchRestaurantAdapter adapter;
    AppGlobal appGlobal = AppGlobal.getInatance() ;
    private boolean isCurrentLocationSearch;
    private double currentLat;
     Button  search;
    private double currentLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_dine_restauant_search);
        setupToolBar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        restlist=new ArrayList<>();
        final ListView lvRestaurant = (ListView) findViewById(R.id.listrestuant);
        Bundle extra=getIntent().getExtras();
        final String value=extra.getString("From");

        lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (value.contains("Login"))
                {

                    if (appGlobal.getUsername().trim().isEmpty() || !appGlobal.getPassword().isEmpty()) {
                        final CharSequence[] items = {"Login", "Sign up", "Cancel"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(GoDineRestaurantSearchActivity.this);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (items[item].equals("Login")) {
                                    startActivity(new Intent(GoDineRestaurantSearchActivity.this, LoginActivity.class));
                                    finish();
                                } else if (items[item].equals("Sign up")) {

                                    Intent intent=new Intent(GoDineRestaurantSearchActivity.this, Splash2.class);
                                    intent.putExtra("From","Outside");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
                                } else if (items[item].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            }
                        });

                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        return;
                    }
                }
                else if(value.contains("Signup")){

                    String Restaurantname=((TextView)view.findViewById(R.id.restautname)).getText().toString();
                    String Cityname=((TextView)view.findViewById(R.id.restaurantaddress)).getText().toString();
                    String Id= ((TextView)view.findViewById(R.id.idd)).getText().toString();
                    Intent intent=new Intent();
                    intent.putExtra("Restaurantname",Restaurantname);
                    intent.putExtra("Cityname",Cityname);
                    intent.putExtra("Id",Id);
                    setResult(3,intent);
                    finish();
                }
            }

        });
        adapter=new SearchRestaurantAdapter(getApplicationContext(),R.layout.restaurant_search_list,restlist);
        lvRestaurant.setAdapter(adapter);

        final Button bt_tapsearchrestaurant= (Button) findViewById(R.id.bt_tapsearchrestaurant);
        search= (Button) findViewById(R.id.bt_searchrestaurant);
        bt_tapsearchrestaurant.setOnClickListener(this);
        search.setOnClickListener(this);
        gd_rest_search_filter_container = (ScrollView) findViewById(R.id.gd_rest_search_filter_container);

        // Search near By restaurants
        isCurrentLocationSearch = true ;
        if (Utility.checkGooglePlayService(GoDineRestaurantSearchActivity.this))
        {
            setupLocation();
        }
    }

    protected synchronized void setupLocation()
    {
        mgoogleApiclient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if(mgoogleApiclient!=null)
        {
            mgoogleApiclient.connect();
        }
    }

    Toolbar toolbar_gd_rest_search ;
    private void setupToolBar() {

        toolbar_gd_rest_search = (Toolbar) findViewById(R.id.toolbar_gd_rest_search);
        mTitle = (TextView) toolbar_gd_rest_search.findViewById(R.id.tvToolBarMiddleLabel) ;
        mTitle.setText(getResources().getText(R.string.go_dine_restaurant));
        mTitle.setTextSize(18);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        ImageView ivToolBarNavigationIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView ivToolBarEndIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setImageResource(R.drawable.search_icn_toolbar);
        ivToolBarEndIcn.setVisibility(View.VISIBLE);
        ivToolBarEndIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gd_rest_search_filter_container.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
            }
        });
    }
    public void closeGDRestFilterSearchForm(View view)
    {
        final ImageView ivToolBarEndIcn = (ImageView)toolbar_gd_rest_search.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarEndIcn.setVisibility(View.VISIBLE);
        gd_rest_search_filter_container.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch(view.getId())
        {
            case R.id.bt_searchrestaurant :
                isCurrentLocationSearch  = false ;

                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                break ;

            case R.id.bt_tapsearchrestaurant :
                isCurrentLocationSearch = true ;

                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                break ;
        }

        if (Utility.checkGooglePlayService(GoDineRestaurantSearchActivity.this))
        {
            setupLocation();
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

    private void    loadRestaurants(final String name,final String zipcode,final String latitude,final String longitude ,String Miles)    {
        Utility.showLoadingPopup(GoDineRestaurantSearchActivity.this);
        if(restlist!=null) {
            restlist.clear();
        }
      /*  String  latitud="47.6426815000";
        String longitud="-117.5193558000";*/

        // For Testing
        Miles="100";
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantNameOrCity",name);
        params.put("ZipCode",zipcode);
        params.put("lat",latitude);
        params.put("lng",longitude);
        params.put("miles",Miles);
        params.put("RestaurantType","253,284" );
        new ServiceController(getApplicationContext(), new HttpResponseCallback()
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
                                // String Id=jsonObjects.getString("Id");
                                restaurantObj.setId(jsonObjects.getString("Id"));
                                restaurantObj.setImage(jsonObjects.getString("RestaurantImage"));
                                restaurantObj.setName(jsonObjects.getString("Name"));
                                restaurantObj.setReview(jsonObjects.getString("NumberOfReviews"));
                                restaurantObj.setAddress(jsonObjects.getString("Address"));
                                StringBuffer Area = new StringBuffer();
                                Area.append(jsonObjects.getString("Region"));
                                Area.append(", " + jsonObjects.getString("City"));
                                Area.append(", " + jsonObjects.getString("PostalCode"));
                                restaurantObj.setArea("" + Area);
                                restaurantObj.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
                                restaurantObj.setLunch(jsonObjects.getString("RestaurantAverageLunch"));
                                restaurantObj.setResttype(jsonObjects.getString("RestaurantType"));
                                restaurantObj.setDinner(jsonObjects.getString("RestaurantAverageDinner"));
                                restaurantObj.setRating((float)jsonObjects.getDouble("Rating"));
                                double lat = jsonObjects.getDouble("Latitude");
                                double lng = jsonObjects.getDouble("Longitude");
                                long miles =  calculateMiles(lat,lng);
                                restaurantObj.setMiles(miles) ;
                                int availableOffers = jsonObjects.getInt("IsOfferAvailable");
                                restaurantObj.setOffers(availableOffers);
                                restlist.add(restaurantObj);
                            }
                        }
                        else
                        {
                            AlertDialog.Builder builder =  new AlertDialog.Builder(GoDineRestaurantSearchActivity.this);
                            builder.setTitle("Info");
                            builder.setMessage("No Restaurant Found Nearby, Please use other search option.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create();
                            builder.show();
                        }
                        Collections.sort(restlist, new Comparator<Restaurant>() {
                            @Override
                            public int compare(Restaurant restaurant, Restaurant t1) {
                             if(restaurant.getMiles()>t1.getMiles())
                             {
                                 return 1;
                             }
                                else
                             {
                                 return -1;
                             }
                               // return 0;
                            }
                        });
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                else
                {
                    ErrorController.showError(getApplicationContext(),data,success);
                }
            }
        }).request(ServiceMod.SEARCHRESTAURANTFORREGISTRATION,params);
    }

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
    public void onConnected(Bundle bundle) {
        Location mLocation= LocationServices.FusedLocationApi.getLastLocation(mgoogleApiclient);
        if(mLocation!=null)
        {
            currentLat = mLocation.getLatitude();
            currentLng = mLocation.getLongitude();
            Log.d("Current Location",currentLat+","+currentLng);
            String name= "";
            String zipcode="";
            String latitude = "0" ;
            String longitude="0";
            String miles= "0";
            if(isCurrentLocationSearch){
                latitude = currentLat+"" ;
                longitude= currentLng+"";
                miles= AppGlobal.getInatance().getMiles();
            }
            else {
                final EditText et_restaurantname = (EditText) findViewById(R.id.et_restname);
                final EditText et_zipcode = (EditText) findViewById(R.id.et_zipcode);
                name = et_restaurantname.getText().toString();
                zipcode = et_zipcode.getText().toString();
                if (name.isEmpty() && zipcode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please search by one parameter", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }

            loadRestaurants(name,zipcode,latitude,longitude,miles);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }




    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionRequestHandler.requestPermissionToLocation(GoDineRestaurantSearchActivity.this,null))
        {
            checkGPSStatus();
        }
    }
}

