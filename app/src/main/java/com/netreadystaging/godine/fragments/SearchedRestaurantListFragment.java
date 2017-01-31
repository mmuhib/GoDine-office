package com.netreadystaging.godine.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.netreadystaging.godine.R;

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
 * Created by lenovo on 12/19/2016.
 */

public class SearchedRestaurantListFragment extends Fragment {

    ArrayList<Restaurant> restlist;
    SearchRestaurantAdapter adapter;
    ListView lvRestaurant ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.searched_rest_list_frag,container,false);
        lvRestaurant = (ListView) view.findViewById(R.id.listrestuant) ;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle  = getArguments() ;
        if(bundle==null){
            restlist = new ArrayList<>();
        }else{
            restlist = (ArrayList<Restaurant>) bundle.getSerializable("restaurants");
            if(restlist==null)
            {
                restlist = new ArrayList<>();
            }
        }
        lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = (Restaurant) ((ListView) parent).getAdapter().getItem(position);
               // loadRestaurantDetails(restaurant.getId(),restaurant.getMiles(),restaurant.currentLat,restaurant.currentLng);
                RestaurantProfile fragment = new RestaurantProfile();
                Bundle restBundle = new Bundle();
                restBundle.putSerializable("rest_id",restaurant.getId());
                fragment.setArguments(restBundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).addToBackStack(null).commit() ;


            }
        });
        adapter=new SearchRestaurantAdapter(getActivity(),0,restlist);
        lvRestaurant.setAdapter(adapter);
    }

//    private void loadRestaurantDetails(final String rest_id, final long miles, final double currentLat, final double currentLng) {
//        Utility.showLoadingPopup(getActivity());
//        final HashMap<String,String> params=new HashMap<>();
//        params.put("RestaurantId",rest_id);
//        params.put("UserId", AppGlobal.getInatance().getUserId()+"");
//        new ServiceController(getActivity(), new HttpResponseCallback()
//        {
//            @Override
//            public void response(boolean success, boolean fail, String data)
//            {
//                Utility.hideLoadingPopup();
//                if(success)
//                {
//                    JSONArray jsonArray = null;
//                    try {
//                        jsonArray = new JSONArray(data);
//                        if(jsonArray.length()>0) {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
//                                Restaurant restaurantObj = new Restaurant();
//                                restaurantObj.setImage(jsonObjects.getString("ImageUrl"));
//                                restaurantObj.setName(jsonObjects.getString("RestaurantName"));
//                                restaurantObj.setId(rest_id);
//                                restaurantObj.setReview(jsonObjects.getString("NumberOfReviews"));
//                                restaurantObj.setAddress(jsonObjects.getString("Address"));
//                                String state  = jsonObjects.getString("State") ;
//                                String city  =jsonObjects.getString("City");
//                                String zipCode  =jsonObjects.getString("ZipCode");
//                                restaurantObj.setState(state);
//                                restaurantObj.setCity(city);
//                                restaurantObj.setZipcode(zipCode);
//                                StringBuffer area = new StringBuffer();
//                                area.append(state);
//                                area.append("," +city );
//                                area.append("," + zipCode);
//                                restaurantObj.setArea("" + area);
//                                restaurantObj.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
//                                restaurantObj.setRestaurantFeatures(jsonObjects.getString("RestaurantFeatures"));
//                                restaurantObj.setRestaurantDaysOpen(jsonObjects.getString("RestaurantDaysOpen"));
//                                restaurantObj.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
//                                restaurantObj.setRestaurantOverview(jsonObjects.getString("RestaurantOverview"));
//                                restaurantObj.setRestaurantMealServiceOffered(jsonObjects.getString("RestaurantMealServiceOffered"));
//                                restaurantObj.setRestaurantPhoneNumber(jsonObjects.getString("RestaurantPhoneNumber"));
//                                restaurantObj.setLunch(jsonObjects.getString("AverageLunchPrice"));
//                                restaurantObj.setDinner(jsonObjects.getString("AverageDinnerPrice"));
//                                restaurantObj.setSpecialRestrictions(jsonObjects.getString("SpecialRestrictions"));
//                                restaurantObj.setEmail(jsonObjects.getString("Email"));
//                                restaurantObj.setSpecialRestrictions(jsonObjects.getString("SpecialRestrictions"));
//                                restaurantObj.setHoursOfOperation(jsonObjects.getString("HoursofOperation"));
//                                restaurantObj.setRating((float)jsonObjects.getDouble("Rating"));
//                                double lat = jsonObjects.getDouble("Latitude");
//                                double lng = jsonObjects.getDouble("Longitude");
//                                restaurantObj.lat = lat ;
//                                restaurantObj.lng = lng ;
//                                restaurantObj.currentLat = currentLat ;
//                                restaurantObj.currentLng  = currentLng ;
//                                restaurantObj.setMiles(miles);
//                                restaurantObj.setWebsite(jsonObjects.getString("Website"));
//
//                                int isFavorite = jsonObjects.getInt("IsFavorite") ;
//                                if(isFavorite>0) restaurantObj.setFavorite(true);
//                                else restaurantObj.setFavorite(false);
//                                String openNow  = jsonObjects.getString("IsOpenedNow");
//                                if(openNow.trim().isEmpty()) restaurantObj.setOpenedNow(false);
//                                else{
//                                    int openNowNumber  = Integer.parseInt(openNow);
//                                    if(openNowNumber>0) restaurantObj.setOpenedNow(true);
//                                    else restaurantObj.setOpenedNow(false);
//                                }
//
//
//                            }
//                        }
//                        else
//                        {
//                            Utility.hideLoadingPopup();
//                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        Utility.hideLoadingPopup();
//                        e.printStackTrace();
//                    }
//                }
//                else
//                {
//                    Utility.hideLoadingPopup();
//                    ErrorController.showError(getActivity(),data,success);
//                }
//            }
//        }).request(ServiceMod.RESTAURANT_DETAILS,params);
//    }
}
