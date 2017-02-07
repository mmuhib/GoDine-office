package com.netreadystaging.godine.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.AboutRestaurantActivity;
import com.netreadystaging.godine.activities.main.BrowserActivity;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.main.RestaurantOffersActivity;
import com.netreadystaging.godine.activities.main.RestaurantReviewActivity;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;
import com.netreadystaging.godine.views.OpeningDayView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;
import in.technobuff.helper.utils.PermissionRequestHandler;

/**
 * Created by lenovo on 12/29/2016.
 */

public class RestaurantProfile extends Fragment  implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Restaurant restaurant = null ;
    private String restaurantId;
    private GoogleApiClient mgoogleApiclient;
    View view ;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = null ;
        try{
            view  = inflater.inflate(R.layout.frag_restaurant_profile,container,false);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Bundle bundle  =  getArguments();

        if(bundle!=null) {
            // restaurant = (Restaurant)bundle.getSerializable("restaurant");
            restaurantId = bundle.getString("rest_id");
            if (Utility.checkGooglePlayService(getActivity()))
            {
                setupLocation();
            }
        }
        setupToolBar();
        return view;
    }

    private void setupRestInfo() {
        final TextView restaurantOpenStatus = (TextView) view.findViewById(R.id.restaurantOpenStatus);
        if(restaurant.isOpenedNow()) {
            restaurantOpenStatus.setText("open");
            restaurantOpenStatus.setTextColor(Color.parseColor("#FFFF00"));
        }
        else {
            restaurantOpenStatus.setText("closed");
            restaurantOpenStatus.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    private void setupRestProfileImage() {
        final ImageView imageRestProfile =(ImageView) view.findViewById(R.id.imageRestProfile);
        String imageUrl  = restaurant.getImage() ;
        Picasso.with(getActivity()).load("https://"+imageUrl).into(imageRestProfile);
    }

    private void setupRestOptions() {

        final TextView tvRestHours = (TextView) view.findViewById(R.id.tvRestHours);
        tvRestHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
                View view  = LayoutInflater.from(getActivity()).inflate(R.layout.popup_rest_hours,null);

                loadOpeningHours(view);
                builder.setView(view);
                builder.setPositiveButton("OK", null);
                builder.create();
                builder.show() ;
            }
        });
        final TextView tvRestRestrictions = (TextView) view.findViewById(R.id.tvRestRestrictions);
        tvRestRestrictions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Restrictions");
//                builder.setMessage(restaurant.getSpecialRestrictions());
//                builder.setPositiveButton("OK", null);
//                builder.create();
//                builder.show() ;
                Utility.Alertbox(getActivity(),"Restrictions",restaurant.getSpecialRestrictions(),"OK");
            }
        });
        final TextView tvRestFeatures = (TextView) view.findViewById(R.id.tvRestFeatures);
        tvRestFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Features");
//                builder.setMessage(restaurant.getRestaurantFeatures());
//                builder.setPositiveButton("OK", null);
//                builder.create();
//                builder.show() ;
                Utility.Alertbox(getActivity(),"Features",restaurant.getRestaurantFeatures(),"OK");
            }
        });
    }

    private void loadOpeningHours(final View view) {
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantId",restaurantId);
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
                                JSONObject hourDayJsonObj  =  jsonArray.getJSONObject(i);
                                String day = hourDayJsonObj.getString("Day");
                                switch (day.toLowerCase())
                                {
                                    case "mon" :
                                        day = "Monday";
                                        break ;
                                    case "tue":
                                        day = "Tuesday";
                                        break ;
                                    case "wed" :
                                        day = "Wednesday";
                                        break ;
                                    case "thu":
                                        day = "Thursday";
                                        break ;
                                    case "fri" :
                                        day = "Friday";
                                        break ;
                                    case "sat":
                                        day = "Saturday";
                                        break ;
                                    case "sun":
                                        day = "Sunday";
                                        break ;
                                }

                                String   LunchTimeOpen=  hourDayJsonObj.getString("LunchTimeOpen");
                                String    LunchTimeOpenFormat= hourDayJsonObj.getString("LunchTimeOpenFormat");
                                String   LunchTimeClose=  hourDayJsonObj.getString("LunchTimeClose");
                                String   LunchTimeCloseFormat=  hourDayJsonObj.getString("LunchTimeCloseFormat");
                                String  DinnerTimeOpen=   hourDayJsonObj.getString("DinnerTimeOpen");
                                String  DinnerTimeOpenFormat=   hourDayJsonObj.getString("DinnerTimeOpenFormat");
                                String   DinnerTimeClose=  hourDayJsonObj.getString("DinnerTimeClose");
                                String   DinnerTimeCloseFormat=  hourDayJsonObj.getString("DinnerTimeCloseFormat");
                                String lunchTiming = "Lunch: "+LunchTimeOpen+LunchTimeOpenFormat+" - "+LunchTimeClose+LunchTimeCloseFormat;
                                String dinnerTiming = "Dinner: "+DinnerTimeOpen+DinnerTimeOpenFormat+" - "+DinnerTimeClose+DinnerTimeCloseFormat;

                                OpeningDayView openingDayView = new OpeningDayView(getActivity());
                                openingDayView.setOpeningHourOfDay(day,lunchTiming,dinnerTiming);
                                ((LinearLayout)view.findViewById(R.id.hourContainer)).addView(openingDayView);
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.HOURS_OF_OPERATION,params);
    }

    private void setupInfoOptions() {

        final TextView tvAboutRest =  (TextView)view.findViewById(R.id.tvAboutRest);
        tvAboutRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), AboutRestaurantActivity.class);
                intent.putExtra("about",restaurant.getRestaurantOverview());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });

        final TextView tvMenuRest =  (TextView)view.findViewById(R.id.tvMenuRest);
        tvMenuRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRestMenus(restaurant.getId());
            }
        });

        final TextView tvSpecialOffersRest =  (TextView)view.findViewById(R.id.tvSpecialOffersRest);
        tvSpecialOffersRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), RestaurantOffersActivity.class);
                intent.putExtra("rest_id",restaurant.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });

        final TextView tvReviewRest =  (TextView)view.findViewById(R.id.tvReviewRest);
        tvReviewRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(), RestaurantReviewActivity.class);
                intent.putExtra("rest_id",restaurant.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });


        final TextView restDistance = (TextView) view.findViewById(R.id.restDistance) ;
        restDistance.setText(restaurant.getMiles()+" miles");

        final TextView tvAvgLunchDinnerPrice = (TextView) view.findViewById(R.id.tvAvgLunchDinnerPrice) ;
        tvAvgLunchDinnerPrice.setText("Avg Lunch-$"+restaurant.getLunch()+"/Avg Dinner-$"+restaurant.getDinner());
    }

    private void setupActionOptions() {

        final ImageView imgRestCall = (ImageView) view.findViewById(R.id.imgRestCall);
        imgRestCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber  = restaurant.getRestaurantPhoneNumber() ;
                if(!phoneNumber.trim().isEmpty())
                    call("+1"+phoneNumber);
            }
        });


        final ImageView imgRestMail = (ImageView) view.findViewById(R.id.imgRestMail);
        imgRestMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email  = restaurant.getEmail();
                if(!email.trim().isEmpty()) {
                    String[] to = {email};
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_EMAIL, to);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(Intent.createChooser(intent, "Send mail"));
                    }
                }
            }
        });

        final ImageView imgRestDirection = (ImageView) view.findViewById(R.id.imgRestDirection);
        imgRestDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url  =  "http://maps.google.com/maps?saddr="+restaurant.currentLat+","+restaurant.currentLng+"&daddr="+restaurant.getLat()+","+restaurant.getLng();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });

        final ImageView imgRestFavorite = (ImageView) view.findViewById(R.id.imgRestFavorite);
        imgRestFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()) {
                    requestUnFavorite(v,restaurant.getId()); // false
                }else {
                    requestFavorite(v,restaurant.getId()) ;// true
                }
            }
        });
        imgRestFavorite.setSelected(restaurant.isFavorite());

        final ImageView imgRestShare = (ImageView) view.findViewById(R.id.imgRestShare);
        imgRestShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
//                builder.setItems(R.array.share, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch(which)
//                        {
//                            case 0 :
//                                break ;
//
//                            case 1:
//                                break ;
//
//                            case 2 :
//                                dialog.dismiss();
//                                break ;
//                        }
//                    }
//                });
//                builder.create() ;
//                builder.show();
                StringBuilder shareContent = new StringBuilder();
                shareContent.append("Get 50% off your Entree every time at ").append(restaurant.getName()).append(" ")
                        .append(restaurant.getCity()).append(" ").append("with the GoDine\u2122 App.Enter Sponsor ")
                        .append(AppGlobal.getInatance().getUserId()).append(" or www.godineclub.com/").append(AppGlobal.getInatance().getUserId());


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share"));

            }
        });

    }

    private void setupRestWebsite() {
        final ImageView imgWebsiteView = (ImageView) view.findViewById(R.id.imgWebsiteView);
        imgWebsiteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://"+restaurant.getWebsite();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });
    }

    private void setupMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupRatingBar() {
        final  AppCompatRatingBar  ratingBar= (AppCompatRatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setRating(restaurant.getRating());
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FF3399FF"), PorterDuff.Mode.SRC_ATOP);

        final TextView review = (TextView) view.findViewById(R.id.review);
        review.setText(restaurant.getReview()+" Review");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PermissionRequestHandler.requestPermissionToLocation(getActivity(),RestaurantProfile.this)) {
            ((MainPageActivity) getActivity()).checkGPSStatus();
        }
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
        title.setText("Restaurant Profile");
        title.setTextSize(15f);
        ivToolBarEndIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        FrameLayout bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.VISIBLE);
        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(restaurant.getLat(),restaurant.getLng());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(restaurant.getName()))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
    }

    public void onDestroyView()
    {
        super.onDestroyView();
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.map));
        if(fragment !=null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }

    private void requestFavorite(final View v,String restId) {
        Utility.showLoadingPopup(getActivity());
        final HashMap<String,String> params=new HashMap<>();
        params.put("IsMobileRequest","1");
        params.put("RestaurantId",restId);
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
                                JSONObject favSuccessObj  =  jsonArray.getJSONObject(i);
                                String status =  favSuccessObj.getString("Result");
                                if(status.trim().toLowerCase().equals("success"))
                                {
                                    v.setSelected(true);

//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                    builder.setMessage("Restaurant has been added to your fav list.") ;
//                                    builder.setPositiveButton("Ok",null);
//                                    builder.setTitle("Info");
//                                    builder.create();
//                                    builder.show() ;

                                    Utility.Alertbox(getActivity(),"Info","Restaurant has been added to your fav list.","OK");

                                }
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.MARK_FAVOURITE,params);
    }

    private void requestUnFavorite(final View v,String restId) {
        Utility.showLoadingPopup(getActivity());
        final HashMap<String,String> params=new HashMap<>();
        params.put("IsMobileRequest","1");
        params.put("RestaurantId",restId);
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
                                JSONObject favSuccessObj  =  jsonArray.getJSONObject(i);
                                String status =  favSuccessObj.getString("Result");
                                if(status.trim().toLowerCase().equals("success"))
                                {
                                    v.setSelected(false);
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                    builder.setMessage("Restaurant has been removed from your fav list.") ;
//                                    builder.setPositiveButton("Ok",null);
//                                    builder.setTitle("Info");
//                                    builder.create();
//                                    builder.show() ;

                                    Utility.Alertbox(getActivity(),"Info","Restaurant has been removed from your fav list.","OK");

                                }
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.UNMARK_FAVOURITE,params);
    }

    private void requestRestMenus(final String restId) {
        Utility.showLoadingPopup(getActivity());
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantId",restId);
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
                                JSONObject favSuccessObj  =  jsonArray.getJSONObject(i);
                                String menuImage =  favSuccessObj.getString("MenuImage").trim();
                                final String lunchURL =  favSuccessObj.getString("LunchURL").trim();
                                final String dinnerURL =  favSuccessObj.getString("DinnerURL").trim();
                                final String fullURL =  favSuccessObj.getString("FullURL").trim();

                                final AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
                                final View view  = LayoutInflater.from(getActivity()).inflate(R.layout.popup_rest_menu,null);

                                builder.setView(view);
                                final Dialog dialog = builder.create();
                                final TextView tvRestLunchMenu = (TextView) view.findViewById(R.id.tvRestLunchMenu);
                                tvRestLunchMenu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!lunchURL.isEmpty()){
                                            openMenuInBrowser(lunchURL,"Lunch");
                                        }
                                    }
                                });
                                final TextView tvRestFullMenu = (TextView) view.findViewById(R.id.tvRestFullMenu);
                                tvRestFullMenu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!fullURL.isEmpty()){
                                            openMenuInBrowser(fullURL,"Full");
                                        }
                                    }
                                });
                                final TextView tvRestDinnerMenu = (TextView) view.findViewById(R.id.tvRestDinnerMenu);
                                tvRestDinnerMenu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!dinnerURL.isEmpty()){
                                            openMenuInBrowser(dinnerURL,"Dinner");
                                        }
                                    }
                                });
                                final TextView tvCancelPopup = (TextView) view.findViewById(R.id.tvCancelPopup);
                                tvCancelPopup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show() ;

                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(getActivity(),data,success);
                }
            }
        }).request(ServiceMod.RESTAURANT_MENU,params);
    }

    private void openMenuInBrowser(String url, String title) {
        Intent intent  = new Intent(getActivity(), BrowserActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
    }

    /************************************************/

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
            double currentLat = mLocation.getLatitude();
            double currentLng = mLocation.getLongitude();
            if(Utility.isNetworkConnected(getActivity())) {
                loadRestaurantDetails(restaurantId, currentLat, currentLng);
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

    private void loadRestaurantDetails(final String rest_id, final double currentLat, final double currentLng) {
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantId",rest_id);
        params.put("UserId", AppGlobal.getInatance().getUserId()+"");
        new ServiceController(getActivity(), new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                Utility.hideLoadingPopup();
                if(success)
                {
                    StringBuffer area = new StringBuffer();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                restaurant = new Restaurant();
                                restaurant.setImage(jsonObjects.getString("ImageUrl"));
                                restaurant.setName(jsonObjects.getString("RestaurantName"));
                                restaurant.setId(rest_id);
                                restaurant.setReview(jsonObjects.getString("NumberOfReviews"));
                                restaurant.setAddress(jsonObjects.getString("Address"));
                                String state  = jsonObjects.getString("State") ;
                                String city  =jsonObjects.getString("City");
                                String zipCode  =jsonObjects.getString("ZipCode");
                                restaurant.setState(state);
                                restaurant.setCity(city);
                                restaurant.setZipcode(zipCode);

                                area.append(city );
                                area.append(", " +state);
                                area.append(", " + zipCode);
                                restaurant.setArea("" + area);
                                restaurant.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
                                restaurant.setRestaurantFeatures(jsonObjects.getString("RestaurantFeatures"));
                                restaurant.setRestaurantDaysOpen(jsonObjects.getString("RestaurantDaysOpen"));
                                restaurant.setRestaurantCusine(jsonObjects.getString("RestaurantCuisine"));
                                restaurant.setRestaurantOverview(jsonObjects.getString("RestaurantOverview"));
                                restaurant.setRestaurantMealServiceOffered(jsonObjects.getString("RestaurantMealServiceOffered"));
                                restaurant.setRestaurantPhoneNumber(jsonObjects.getString("RestaurantPhoneNumber"));
                                restaurant.setLunch(jsonObjects.getString("AverageLunchPrice"));
                                restaurant.setDinner(jsonObjects.getString("AverageDinnerPrice"));
                                restaurant.setSpecialRestrictions(jsonObjects.getString("SpecialRestrictions"));
                                restaurant.setEmail(jsonObjects.getString("Email"));
                                restaurant.setSpecialRestrictions(jsonObjects.getString("SpecialRestrictions"));
                                restaurant.setHoursOfOperation(jsonObjects.getString("HoursofOperation"));
                                restaurant.setRating((float)jsonObjects.getDouble("Rating"));
                                double lat = jsonObjects.getDouble("Latitude");
                                double lng = jsonObjects.getDouble("Longitude");
                                restaurant.lat = lat ;
                                restaurant.lng = lng ;
                                restaurant.currentLat = currentLat ;
                                restaurant.currentLng  = currentLng ;

                                long miles = calculateMiles( lat, lng,currentLat,currentLng);
                                restaurant.setMiles(miles);
                                restaurant.setWebsite(jsonObjects.getString("Website"));

                                int isFavorite = jsonObjects.getInt("IsFavorite") ;
                                if(isFavorite>0) restaurant.setFavorite(true);
                                else restaurant.setFavorite(false);
                                String openNow  = jsonObjects.getString("IsOpenedNow");
                                if(openNow.trim().isEmpty()) restaurant.setOpenedNow(false);
                                else{
                                    int openNowNumber  = Integer.parseInt(openNow);
                                    if(openNowNumber>0) restaurant.setOpenedNow(true);
                                    else restaurant.setOpenedNow(false);
                                }
                                break ;
                            }

                            bindDataToGUI();
                            StringBuilder sb  = new StringBuilder();
                            sb.append(restaurant.getName()).append("\n").append(restaurant.getAddress()).append("\n")
                                    .append(area.toString());
                            title.setText(sb.toString());

                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(getActivity(),data,success);
                }
            }

            private long calculateMiles(double lat, double lng, double currentLat, double currentLng) {
                Location locationA = new Location("point A");

                locationA.setLatitude(currentLat);
                locationA.setLongitude(currentLng);

                Location locationB = new Location("point B");

                locationB.setLatitude(lat);
                locationB.setLongitude(lng);

                float distance = locationA.distanceTo(locationB);

                return (long)(distance*0.000621371) ;
            }
        }).request(ServiceMod.RESTAURANT_DETAILS,params);
    }

    private void bindDataToGUI() {
        setupRestProfileImage();
        setupRestInfo();
        setupRestOptions();
        setupInfoOptions();
        setupActionOptions();
        setupRestWebsite();
        setupRatingBar();
        setupMapView();
    }

    /***************************************************
     * Calling Feature
     * **************************************************/

    private void call(String  number) {

        try {
            // set the data
            String uri = "tel:" + number;
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CALL_PHONE) || ActivityCompat.shouldShowRequestPermissionRationale( getActivity(),
                            Manifest.permission.READ_PHONE_STATE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.


                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions( getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
            }
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Your call has failed...",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Toast.makeText(getActivity(), incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(getActivity(), "on call...",
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
                        Toast.makeText(getActivity(), "restart app after call",
                                Toast.LENGTH_LONG).show();

                        // restart our application
                        Intent restart = getActivity().getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);

                        onCall = false;
                    }
                    break;
                default:
                    break;
            }

        }
    }

}
