package com.netreadystaging.godine.fragments;



import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.GoDineRestaurantSearchActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.adapters.SearchRestaurantAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
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

/**
 * Created by sony on 19-07-2016.
 */

public class FavoritePageFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    View view ;
    private ListView lvRestaurant;
    private GoogleApiClient mgoogleApiclient;
    private double currentLat;
    private double currentLng;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.favorite_page_fragment,container,false);
        setupToolBar();
        setupListView(view);
        return view ;
    }


    protected synchronized void setupLocation() {
        Utility.showLoadingPopup(getActivity());
        mgoogleApiclient=new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if(mgoogleApiclient!=null)
        {
            mgoogleApiclient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLocation= LocationServices.FusedLocationApi.getLastLocation(mgoogleApiclient);
        if(mLocation!=null)
        {
             currentLat = mLocation.getLatitude();
             currentLng = mLocation.getLongitude();
            if(Utility.isNetworkConnected(getActivity())) {
                loadFavoriteRestaurants();
            }else
            {
                Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                Utility.hideLoadingPopup();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Utility.hideLoadingPopup();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Utility.hideLoadingPopup();
    }

    private void setupListView(View view) {
        lvRestaurant = (ListView) view.findViewById(R.id.listrestuant) ;
        lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = (Restaurant) ((ListView) parent).getAdapter().getItem(position);
                RestaurantProfile fragment = new RestaurantProfile();
                Bundle restBundle = new Bundle();
                restBundle.putSerializable("rest_id",restaurant.getId());
                fragment.setArguments(restBundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).addToBackStack(null).commit() ;
            }
        });
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void loadFavoriteRestaurants() {
        final ArrayList<Restaurant> restlist =  new ArrayList<>();
        Utility.showLoadingPopup(getActivity());
        final HashMap<String,String> params=new HashMap<>();
        params.put("UserId", AppGlobal.getInatance().getUserId()+"");
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
                                restaurantObj.setImage(jsonObjects.getString("ImageUrl"));//
                                restaurantObj.setName(jsonObjects.getString("RestaurantName"));//
                                restaurantObj.setId(jsonObjects.getString("RestaurantId"));//
                                restaurantObj.setReview(jsonObjects.getString("NumberOfReviews"));//
                                restaurantObj.setAddress(jsonObjects.getString("Address"));//
                                StringBuffer area = new StringBuffer();
                                area.append(jsonObjects.getString("State"));//
                                area.append("," + jsonObjects.getString("City"));//
                                area.append("," + jsonObjects.getString("ZipCode"));//
                                restaurantObj.setArea("" + area);
                                restaurantObj.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));//
                                restaurantObj.setLunch(jsonObjects.getString("RestaurantAverageLunch"));//
                                restaurantObj.setDinner(jsonObjects.getString("RestaurantAverageDinner"));//
                                restaurantObj.setRating((float)jsonObjects.getDouble("Rating"));//
                                double lat = jsonObjects.getDouble("Latitude");//
                                double lng = jsonObjects.getDouble("Longitude");//
                                long miles =  calculateMiles(lat,lng);
                                restaurantObj.setMiles(miles) ;
                                int availableOffers = jsonObjects.getInt("IsOfferAvailable"); ///
                                restaurantObj.setOffers(availableOffers);
                                restaurantObj.lat = lat ;
                                restaurantObj.lng = lng ;
                                restaurantObj.currentLat = currentLat ;
                                restaurantObj.currentLng =currentLng ;
                                restlist.add(restaurantObj);
                            }
                        }
                        else
                        {

                            Utility.Alertbox(getActivity(),"Info","No Favorite Restaurant Found ","OK");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }

               final SearchRestaurantAdapter adapter=new SearchRestaurantAdapter(getActivity(),0,restlist);
                lvRestaurant.setAdapter(adapter);
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
        }).request(ServiceMod.FAVOURITE_RESTAURANT_LIST,params);

    }

    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.VISIBLE);
        ivToolBarBack.setVisibility(View.GONE);
        ivToolBarEndIcn.setVisibility(View.GONE);
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);

    }

TextView title ;
    @Override
    public void onResume() {
        super.onResume();
        title.setText("Favorite");
        if (Utility.checkGooglePlayService(getActivity()))
        {
            setupLocation();
        }
    }

}
