package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.FilterActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.models.FilterResponse;
import com.netreadystaging.godine.models.Restaurant;

import java.util.ArrayList;

/**
 * Created by Atul on 12/20/2016.
 */

public class RestaurantSearchFilterFragment extends Fragment implements View.OnClickListener{
    private static final int FILTER_REQUEST = 222 ;
    private ArrayList<Restaurant> restlist;
    private Button btnRestFilter ;
    private Button btnRestMap;
    private  Button btnRestList;
    private String restType = "Restaurant Filters";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.restaurant_filter_frag,container,false);

        btnRestFilter =  (Button) view.findViewById(R.id.btnRestFilter);
        btnRestMap =  (Button) view.findViewById(R.id.btnRestMap);
        btnRestList =  (Button) view.findViewById(R.id.btnRestList);

        btnRestFilter.setOnClickListener(this);
        btnRestMap.setOnClickListener(this);
        btnRestList.setOnClickListener(this);

        Bundle bundle  = getArguments() ;
        if(bundle==null){
            restlist = new ArrayList<>();
        }else{
            restlist = (ArrayList<Restaurant>) bundle.getSerializable("restaurants");
            if(restlist==null)
            {
                restlist = new ArrayList<>();
            }

            restType = bundle.getString("restType");
        }
        setupToolBar();

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnRestList.setSelected(true);
        selectRestList();
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText(restType);
    }

    TextView title ;
    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.VISIBLE);
        ivToolBarBack.setVisibility(View.GONE);
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

    private void selectRestList() {
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass =  SearchedRestaurantListFragment.class ;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle  = new Bundle();
        bundle.putSerializable("restaurants",restlist);

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rest_filter_container, fragment).commit();
    }
    private void selectRestMap() {
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass =  ContactMapFragment.class ;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle  = new Bundle();
        bundle.putSerializable("restaurants",restlist);

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rest_filter_container, fragment).commit();
    }



    private void selectFragment(int viewId) {
        switch (viewId)
        {
            case R.id.btnRestFilter :
                selectRestFilter();
                break ;
            case R.id.btnRestList :
                btnRestMap.setSelected(false);
                btnRestList.setSelected(true);
                selectRestList();
                break ;

            case R.id.btnRestMap :
                btnRestList.setSelected(false);
                btnRestMap.setSelected(true);
                selectRestMap();
                break ;
        }

    }

    private void selectRestFilter() {
        startActivityForResult(new Intent(getActivity(), FilterActivity.class),FILTER_REQUEST);
        getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
    }

    @Override
    public void onClick(View v) {
        selectFragment(v.getId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== FILTER_REQUEST)
        {
            if(resultCode==FilterActivity.FILTER_RESPONSE_CODE)
            {
                FilterResponse filterResponse  = (FilterResponse)  data.getSerializableExtra(FilterActivity.FILTER_RESPONSE);
                String cuisinesStr  = filterResponse.getCuisines() ;
                String featuresStr  = filterResponse.getFeatures() ;
                String priceStr  = filterResponse.getPrice() ;
                String ratingStr  = filterResponse.getRating() ;

              //  filterRestaurantList(cuisinesStr,featuresStr,priceStr,ratingStr) ;

            }
        }
    }

    private void filterRestaurantList(String cuisinesStr, String featuresStr, String priceStr, String ratingStr) {
        String[] cuisines = cuisinesStr.split("[,]");
        String[] features = featuresStr.split("[,]");
        int minPrice ,maxPrice;

        int rating=0 ;
        if(!ratingStr.isEmpty()){
            rating  = Integer.parseInt(ratingStr) ;
        }
        switch(priceStr)
        {
            case "$11-$30($$)" :
                minPrice = 11 ;
                maxPrice = 30 ;
                break;

            case "$31-$60($$$)" :
                minPrice = 31 ;
                maxPrice = 60 ;
                break;

            case "$61+" :
                minPrice = 62 ;
                maxPrice = -1 ;
                break;
        }
        final ArrayList<Restaurant> filteredRestList =  new ArrayList<>();
        int restCount =  restlist.size() ;
        for (int index= 0 ;index<restCount;index++){
            Restaurant restaurant = restlist.get(index) ;
            int restRating =  (int) restaurant.getRating();
            if(restRating==rating) {

            }
        }
    }
}
