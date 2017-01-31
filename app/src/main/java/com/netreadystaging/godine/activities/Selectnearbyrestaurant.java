package com.netreadystaging.godine.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.main.PaymentView;
import com.netreadystaging.godine.adapters.SearchRestaurantAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.fragments.MemberVerification;
import com.netreadystaging.godine.fragments.ProfilePageFragment;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;
import in.technobuff.helper.utils.PermissionRequestHandler;

public class Selectnearbyrestaurant extends AppBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {
    ListView listView;
    TextView mTitle;
    ArrayList<Restaurant> nearbylist = new ArrayList<>();
    SearchRestaurantAdapter adapter;
    private boolean isCurrentLocationSearch;
    GoogleApiClient mgoogleApiclient;
    private double currentLat;
    private double currentLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectnearbyrestaurant);
        listView = (ListView) findViewById(R.id.listvies);
        adapter = new SearchRestaurantAdapter(Selectnearbyrestaurant.this, R.layout.restaurant_search_list, nearbylist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        isCurrentLocationSearch = true;
        if (Utility.checkGooglePlayService(Selectnearbyrestaurant.this)) {
            setupLocation();
        }
        setupToolBar();
    }

    protected synchronized void setupLocation() {
        mgoogleApiclient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if (mgoogleApiclient != null) {
            mgoogleApiclient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiclient);
        if (mLocation != null) {
            currentLat = mLocation.getLatitude();
            currentLng = mLocation.getLongitude();
            Log.d("Current Location", currentLat + "," + currentLng);
            String name = "";
            String zipcode = "";
            String latitude = "33.7669444";
            String longitude = "-118.1883333";
            String miles = "0";
          /*  if(isCurrentLocationSearch){
                latitude = currentLat+"" ;
                longitude= currentLng+"";
                miles= AppGlobal.getInatance().getMiles();
            }*/
            loadRestaurants(name, zipcode, latitude, longitude, miles);
        }
    }

    private void loadRestaurants(final String name, final String zipcode, final String latitude, final String longitude, String Miles) {
        Utility.showLoadingPopup(Selectnearbyrestaurant.this);
        if (nearbylist != null) {
            nearbylist.clear();
        }

        // For Testing
        Miles = "100";
        final HashMap<String, String> params = new HashMap<>();
        params.put("RestaurantNameOrCity", name);
        params.put("ZipCode", zipcode);
        params.put("lat", latitude);
        params.put("lng", longitude);
        params.put("miles", Miles);

        new ServiceController(getApplicationContext(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                Utility.hideLoadingPopup();
                if (success) {
                    JSONArray jsonArray = null;
                    try {

                        jsonArray = new JSONArray(data);
                        if (jsonArray.length() > 0) {
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
                                Area.append("," + jsonObjects.getString("City"));
                                Area.append("," + jsonObjects.getString("PostalCode"));
                                restaurantObj.setArea("" + Area);
                                restaurantObj.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
                                restaurantObj.setLunch(jsonObjects.getString("RestaurantAverageLunch"));
                                restaurantObj.setDinner(jsonObjects.getString("RestaurantAverageDinner"));
                                restaurantObj.setRating((float) jsonObjects.getDouble("Rating"));
                                double lat = jsonObjects.getDouble("Latitude");
                                double lng = jsonObjects.getDouble("Longitude");
                                long miles = calculateMiles(lat, lng);
                                restaurantObj.setMiles(miles);
                                int availableOffers = jsonObjects.getInt("IsOfferAvailable");
                                restaurantObj.setOffers(availableOffers);
                                nearbylist.add(restaurantObj);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Selectnearbyrestaurant.this);
                            builder.setTitle("Info");
                            builder.setCancelable(false);
                            builder.setMessage("No Restaurant Found at your location,Please check again");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                   finish();

                                }
                            });
                            builder.create();
                            builder.show();
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ErrorController.showError(getApplicationContext(), data, success);
                }
            }
        }).request(ServiceMod.SEARCH_RESTAURANT, params);
    }

    private long calculateMiles(double lat, double lng) {
        Location locationA = new Location("point A");

        locationA.setLatitude(currentLat);
        locationA.setLongitude(currentLng);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat);
        locationB.setLongitude(lng);

        float distance = locationA.distanceTo(locationB);
        long miles = (long) (distance * 0.000621371);
        return miles;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionRequestHandler.requestPermissionToLocation(Selectnearbyrestaurant.this, null)) {
            checkGPSStatus();
        }
        mTitle.setText("Select Near by Restaurant");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String restid = ((TextView) view.findViewById(R.id.idd)).getText().toString();
        Bundle extra = getIntent().getExtras();
        String amount = extra.getString("amount");
        String id = extra.getString("userId");
        String membersaving = extra.getString("membersaving");
        HashMap<String, String> params = new HashMap<>();
        params.put("UserId", id);
        params.put("RestaurantId", restid);
        params.put("CheckAmount", amount);
        params.put("Savings", membersaving);
        Utility.showLoadingPopup(Selectnearbyrestaurant.this);
        new ServiceController(getApplicationContext(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                if (success) {
                    Utility.hideLoadingPopup();
                    JSONArray jsonArray=null;
                    try {
                        jsonArray=new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=null;
                            jsonObject=jsonArray.getJSONObject(i);
                            String Result=jsonObject.getString("Result");
                            if(Result.equalsIgnoreCase("Success"))
                            {

                                Log.d("Muhib", data);
                                AlertDialog.Builder builder = new AlertDialog.Builder(Selectnearbyrestaurant.this);
                                builder.setTitle("Info");
                                builder.setCancelable(false);
                                builder.setMessage("Data saved successfully");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                            else if(Result.equalsIgnoreCase("Error"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Selectnearbyrestaurant.this);
                                builder.setTitle("Info");
                                builder.setCancelable(false);
                                builder.setMessage("Savings amount cannot be greater than check amount");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ErrorController.showError(getApplicationContext(), data, success);
                }
            }
        }).request(ServiceMod.InsertRestaurantSavings, params);

    }
    private void setupToolBar() {

            Toolbar toolbar_gd_rest_search = (Toolbar) findViewById(R.id.toolbar_gd_rest_search);
            mTitle = (TextView) toolbar_gd_rest_search.findViewById(R.id.tvToolBarMiddleLabel) ;
            mTitle.setText(getResources().getText(R.string.signup));
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
            ivToolBarBack.setVisibility(View.GONE);
            ivToolBarEndIcn.setVisibility(View.GONE);
        }
    }



