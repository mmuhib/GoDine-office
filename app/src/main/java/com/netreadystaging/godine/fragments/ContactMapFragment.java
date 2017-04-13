package com.netreadystaging.godine.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Atul Kumar Gupta  on 28-07-2016.
 */
public class ContactMapFragment extends Fragment implements OnMapReadyCallback {

    View view  ;
    private List<Restaurant> listRestaurants;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
            view =  inflater.inflate(R.layout.contact_map_fragment,container,false);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            this.listRestaurants  = (ArrayList<Restaurant>) bundle.getSerializable("restaurants") ;
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (final Restaurant restaurant:listRestaurants) {
            LatLng latLng = new LatLng(restaurant.lat, restaurant.lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(restaurant.getName()))

                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    return false;
                }

            });
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                }
            });
        }
    }


    public void onDestroyView()
    {
        super.onDestroyView();
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if(fragment !=null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }
}
