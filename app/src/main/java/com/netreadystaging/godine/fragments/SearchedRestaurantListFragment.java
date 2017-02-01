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
}
