package com.netreadystaging.godine.fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helpshift.providers.DataProvider;
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
                    .position(latLng).snippet(restaurant.getId())
                    .title(restaurant.getName()+"muhib"+restaurant.getRating()+"muhib"+restaurant.getReview()+"muhib"+restaurant.getRestaurantCusine()+"muhib"+restaurant.getAddress() +"muhib"+restaurant.getArea()+"muhib"+restaurant.getRestaurantPhoneNumber()))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker)
                    );

    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {
        String Id=marker.getSnippet();
        RestaurantProfile fragment = new RestaurantProfile();
        Bundle restBundle = new Bundle();
        restBundle.putSerializable("rest_id",Id);
        fragment.setArguments(restBundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).addToBackStack(null).commit() ;

    }
});
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
      //  googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        View view1=getActivity().getLayoutInflater().inflate(R.layout.mapdetail,null);
        String Title=marker.getTitle();
        String[] brek=Title.split("muhib");
        String Tname=brek[0];
         String TRating=brek[1];
        String Treview=brek[2];
        String TCuisine=brek[3];
        String Taddress=brek[4];
        String Tarea=brek[5];
        String Tphone=brek[6];


        Log.d("title",Title);
        Log.d("name",Tname);
         Log.d("rating",TRating);
        Log.d("review",Treview);
        Log.d("cuisine",TCuisine);
        Log.d("address",Taddress);
        Log.d("are",Tarea);
        Log.d("phone",Tphone);

        TextView Rname= (TextView) view1.findViewById(R.id.Rname);
        RatingBar Rrating= (RatingBar) view1.findViewById(R.id.Rrating);
        TextView Rreview= (TextView) view1.findViewById(R.id.Rreview);
        TextView RCuisine= (TextView) view1.findViewById(R.id.RrestaurantCuisine);
        TextView Raddess= (TextView) view1.findViewById(R.id.Raddress);
        TextView Rarea= (TextView) view1.findViewById(R.id.Rarea);
        TextView Rphone= (TextView) view1.findViewById(R.id.RCell);
        Button Tdetail= (Button) view1.findViewById(R.id.Rseedetail);
        Rname.setText(Tname);
        Rrating.setRating(Float.parseFloat(TRating));
        Rreview.setText(Treview+" Reviews");
        RCuisine.setText(TCuisine);
        Raddess.setText(Taddress);
        Rarea.setText(Tarea);
        Rphone.setText(Tphone);
        Tdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view1;
    }
        }   );
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
       Restaurant ret;
        View mymarkerview;
        Yourcustominfowindowadpater(Restaurant ret)
        {
            this.ret=ret;
            mymarkerview = getActivity().getLayoutInflater()
                    .inflate(R.layout.mapdetail, null);

        }
        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mymarkerview,ret);

            return mymarkerview;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
        private void render(Marker marker, View view, Restaurant rets) {
            // Add the code to set the required values
            // for each element in your custominfowindow layout file
            TextView Rname= (TextView) view.findViewById(R.id.Rname);
            Log.d("in cons",rets.getId());
            //TextView tchangepass= (TextView) view.findViewById(R.id.Rregion);
            Log.d("name", this.ret.getName());
            Log.d("title",""+marker.getTitle());
           Rname.setText(rets.getName());
           // tchangepass.setText(rets.getAddress());
        }

    }
}

