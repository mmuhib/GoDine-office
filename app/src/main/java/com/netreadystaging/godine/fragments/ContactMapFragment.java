package com.netreadystaging.godine.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

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
GoogleMap mymap;
    SupportMapFragment mapFragment;

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
      mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mymap=mapFragment.getMap();
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
    public void onMapReady(final GoogleMap googleMap) {
        for (final Restaurant restaurant:listRestaurants) {

            final LatLng latLng = new LatLng(restaurant.lat, restaurant.lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng).snippet(restaurant.getAddress() )
                    .title(restaurant.getName()))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        /*    Log.d("Restauar", "" + restaurant.getId());


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    Restaurant restaurant=mymap.get(marker);

                    Log.d("Restaua",""+restaurant.getId());
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    LayoutInflater inflater=getActivity().getLayoutInflater();
                    View view1=inflater.inflate(R.layout.mapdetail,null);
                    TextView Rname= (TextView) view1.findViewById(R.id.Rname);
                    TextView tchangepass= (TextView) view1.findViewById(R.id.Rregion);
                    tchangepass.setText(restaurant.getAddress());
                    Rname.setText(marker.getTitle());
                   String title= String.valueOf(new MarkerOptions().title(restaurant.getName()));
                    Log.d("name",""+restaurant.getName());
               //     Log.d("Region2",title);
                   // view1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    builder.setView(view1);
                    builder.create();
                    builder.show();
                    return true;
                }

            });*/
         /*  googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
               @Override
               public boolean onMarkerClick(Marker marker) {
                   if(marker.getSnippet() ==null) {
                       googleMap.moveCamera(CameraUpdateFactory.zoomIn());
                       return true;
                   }
                   AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                   LayoutInflater inflater=getActivity().getLayoutInflater();
                   View view1=inflater.inflate(R.layout.mapdetail,null);
                  *//* TextView Rname= (TextView) view1.findViewById(R.id.Rname);
                   Rname.setText(restaurant.getName());*//*
                        return true;
               }
           });*/

        /*googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        View view1=getActivity().getLayoutInflater().inflate(R.layout.mapdetail,null);
        TextView Rname= (TextView) view1.findViewById(R.id.Rname);
        TextView tchangepass= (TextView) view1.findViewById(R.id.Rregion);
        Rname.setText(restaurant.getName());
        tchangepass.setText(restaurant.getAddress());
        return view1;
    }
        }   );*/
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
    class Yourcustominfowindowadpater implements GoogleMap.InfoWindowAdapter
    {
        View mymarkerview;
        Yourcustominfowindowadpater()
        {
            mymarkerview = getActivity().getLayoutInflater()
                    .inflate(R.layout.mapdetail, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mymarkerview);

            return mymarkerview;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
        private void render(Marker marker, View view) {
            // Add the code to set the required values
            // for each element in your custominfowindow layout file
            TextView Rname= (TextView) view.findViewById(R.id.Rname);
            TextView tchangepass= (TextView) view.findViewById(R.id.Rregion);

        }

    }
}

