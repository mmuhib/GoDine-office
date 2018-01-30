package com.netreadystaging.godine.activities.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.adapters.SearchRestaurantAdapter;
import com.netreadystaging.godine.adapters.SimpleArrayAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.models.Staffmodel;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.MyTextView;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;
import in.technobuff.helper.utils.PermissionRequestHandler;

import static com.netreadystaging.godine.R.*;

public class NewSignUp extends AppBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener {
    AppGlobal appGlobal=AppGlobal.getInatance();
    SearchRestaurantAdapter adapter;
    ArrayList<Restaurant> nearbylist = new ArrayList<>();
    ArrayList<Staffmodel> satfslist = new ArrayList<>();
    private boolean isCurrentLocationSearch;
    private double currentLat;
    private double currentLng;
    GoogleApiClient mgoogleApiclient;
    ListView restlist;
    ProgressBar progressBar,progressBarlist;
    LinearLayout llstep0,llstep1,llstep2,llstep3;
    Dialog dial,dial1;
    Button buttonnext;
    TextView txt_notrefered,txt_clickhere,txt_name,submit,txt_joinstep,txt_Reac_rstname,txt_Reac_cityname,bt_Rect_change,bt_Rect_next;
    EditText et_checkrest,et_email,et_password,et_firstname,et_lastname,et_address,et_city,et_state,et_zipcode,et_cellnumber,et_Rechechrest;
    CheckBox ch_terms;
    String UserId,AffiliateI,ProductVariant,StaffName,Email,RecruiterID,Semail,Spassword,Sfirstname,Slname,Scity,Szipcode,
            ScellNumber,Sstreet,Sregion,SMembershipProductVariantId,SDeviceId,SDeviceType,SMiles,Status,Type,RestaurantName,City;
    MyTextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsignup);
      /*  Typeface typee=Typeface.createFromAsset(getAssets(),"fonts/Fenotype - PeachesandCreamBold.ttf");
        txt_joinstep.setTypeface(typee);*/
       // txt_joinstep.setTextSize(36f);
        myTextView= (MyTextView) findViewById(R.id.styledtext);
        et_email=(EditText) findViewById(R.id.et_email);
        Intent intent=getIntent();
        Email=intent.getStringExtra("Email");
        AffiliateI=intent.getStringExtra("AffiliateID");
        RecruiterID=intent.getStringExtra("RecruiterID");
        RestaurantName=intent.getStringExtra("RestaurantName");
        City=intent.getStringExtra("City");
        Type=intent.getStringExtra("From");
        if(Email!=null)
        {
            if(!Email.isEmpty())
            {
                et_email.setText(Email);
                et_email.setEnabled(false);
            }
        }
        et_checkrest= (EditText) findViewById(R.id.et_checkrest);
        buttonnext= (Button) findViewById(id.img_arrowdown);
        et_password=(EditText) findViewById(R.id.et_password);
        et_firstname=(EditText) findViewById(R.id.et_fname);
        et_lastname=(EditText) findViewById(R.id.et_lastname);
        et_address=(EditText) findViewById(R.id.et_addre);
        et_state=(EditText) findViewById(R.id.et_state);
        et_zipcode=(EditText) findViewById(R.id.et_zipcode);
        et_city=(EditText) findViewById(R.id.et_city);
        et_cellnumber=(EditText) findViewById(R.id.et_cellnumber);
        et_Rechechrest= (EditText) findViewById(id.et_checkrest1);
        ch_terms = (CheckBox) findViewById(R.id.ch_agree);

        //et_email.setOnEditorActionListener(this);
        et_checkrest.setOnEditorActionListener(this);
        et_Rechechrest.setOnEditorActionListener(this);
        buttonnext.setOnClickListener(this);
        txt_name= (TextView) findViewById(R.id.txt_name);

        bt_Rect_change= (TextView) findViewById(R.id.bt_change);
        bt_Rect_change.setOnClickListener(this);
        bt_Rect_next=(TextView) findViewById(R.id.bt_next);
        bt_Rect_next.setOnClickListener(this);

        txt_Reac_rstname=(TextView) findViewById(id.txt_Reacrstname);
        txt_Reac_cityname=(TextView) findViewById(id.txt_Reaccityname);
        txt_clickhere=(TextView) findViewById(R.id.txt_clickhere);
        String noAccount="Not the correct Referrer? CLICK HERE ";
        int i=noAccount.indexOf("CL");
        int j=noAccount.indexOf("E ");
        txt_clickhere.setMovementMethod(LinkMovementMethod.getInstance());
        txt_clickhere.setText(noAccount, TextView.BufferType.SPANNABLE);
        Spannable spannable= (Spannable) txt_clickhere.getText();
        final ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                senddefauldID();
            }
        };
        spannable.setSpan(clickableSpan,i,j+1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        llstep0= (LinearLayout) findViewById(R.id.ll_step0);
        llstep1= (LinearLayout) findViewById(R.id.ll_step1);
        llstep2= (LinearLayout) findViewById(R.id.ll_step2);
        llstep3= (LinearLayout) findViewById(R.id.ll_step3);
        txt_notrefered= (TextView) findViewById(R.id.bt_notReffered);
        txt_notrefered.setOnClickListener(this);
       // txt_clickhere.setOnClickListener(this);
        final TextView tvTermCondition = (TextView) findViewById(R.id.tvTermCondition);
        tvTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(NewSignUp.this, BrowserActivity.class);
                intent.putExtra("url","file:///android_asset/terms.html");
                intent.putExtra("title","Terms and Conditions");
                startActivity(intent);
                NewSignUp.this.overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
            }
        });
        submit= (TextView) findViewById(id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senddatatoUrl();
            }
        });
        if(RecruiterID!=null)
        {
            if(!RecruiterID.isEmpty())
            {
                llstep0.setVisibility(View.VISIBLE);
                et_Rechechrest.setText(RecruiterID);
                et_Rechechrest.setFocusable(false);
                et_Rechechrest.setEnabled(false);
                txt_Reac_rstname.setText("Member Name: "+RestaurantName);
                txt_Reac_cityname.setText("City Name: "+City);
            }
        }
        else {
            llstep1.setVisibility(View.VISIBLE);

        /*    isCurrentLocationSearch = true;
            showAlert();*/
        }
    }



    private void showAlert() {

        AlertDialog.Builder builder=new AlertDialog.Builder(NewSignUp.this);
        View view= getLayoutInflater().inflate(R.layout.newsignuppopup,null);
        restlist= (ListView) view.findViewById(id.listvies);
        progressBar = (ProgressBar) view.findViewById(id.progressBar);
        final TextView txtPop_bottom= (TextView) view.findViewById(id.lblSignupbottom);
        txtPop_bottom.setOnClickListener(this);
        selectnearbyRestaurant(restlist);
        adapter = new SearchRestaurantAdapter(NewSignUp.this, layout.restaurant_search_list, nearbylist);
        restlist.setAdapter(adapter);
      //  selectnearbyRestaurantlist(restlist,progressBar);
        builder.setView(view);
        builder.setCancelable(false) ;
        dial=builder.create();
        restlist.setOnItemClickListener(this);
        dial.show();

    }

    private void selectnearbyRestaurantlist(ListView restlist, ProgressBar progressBar) {
        nearbylist=new ArrayList<>();
    }


    private void selectnearbyRestaurant(ListView restlist) {
        if (Utility.checkGooglePlayService(NewSignUp.this)) {
            setupLocation();
        }
    }

    protected synchronized void setupLocation() {
        mgoogleApiclient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if (mgoogleApiclient != null) {
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
            loadRestaurants(name, zipcode, latitude, longitude, miles);
        }
    }
    private void loadRestaurants(final String name, final String zipcode, final String latitude, final String longitude, String Miles) {
        progressBar.setVisibility(View.VISIBLE);
        if (nearbylist != null) {
            nearbylist.clear();
        }
        //   For Testing
        Miles = "0.2";
 /*       String  latitud="47.6426815000";
        String longitud="-117.5193558000";*/

        final HashMap<String, String> params = new HashMap<>();
        params.put("RestaurantNameOrCity", name);
        params.put("ZipCode", zipcode);
        params.put("lat", latitude);
        params.put("lng", longitude);
        params.put("miles", Miles);
        params.put("RestaurantType","253,284" );

        new ServiceController(NewSignUp.this, new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
             progressBar.setVisibility(View.GONE);
                if (success) {
                    JSONArray jsonArray = null;
                    try {
                        String Result="";
                        String Message="";
                        jsonArray = new JSONArray(data);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                               /* Result=jsonObjects.getString("Result");
                                Message=jsonObjects.getString("Message");
                                if(Result.equalsIgnoreCase("Success")) {*/
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
                                restaurantObj.setResttype(jsonObjects.getString("RestaurantType"));
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

                              //  adapter.notifyDataSetChanged();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewSignUp.this);
                            builder.setTitle("Info");
                            builder.setCancelable(false);
                            builder.setMessage("No Restaurant Found at your location,Please check again");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dial.dismiss();
                                    llstep1.setVisibility(View.VISIBLE);

                                }
                            });
                            builder.create();
                            builder.show();
                        }
                        Collections.sort(nearbylist, new Comparator<Restaurant>() {
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
                            }
                        });
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ErrorController.showError(NewSignUp.this, data, success);
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
        if(PermissionRequestHandler.requestPermissionToLocation(NewSignUp.this,null))
        {
            checkGPSStatus();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     RecruiterID= ((TextView) view.findViewById(R.id.idd)).getText().toString();
      //  Utility.message(NewSignUp.this,RecruiterID);

        dial.dismiss();
        showstaffdialog("");

    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.lblSignupbottom:
                dial.dismiss();
                llstep1.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_notReffered:
                senddefauldID();
                break;
            case R.id.bt_change:
                et_Rechechrest.setEnabled(true);
                et_Rechechrest.setFocusableInTouchMode(true);
                et_Rechechrest.requestFocus();
                break;
            case R.id.bt_next:
                gotoReactiveUrl();
            break;
            case R.id.img_arrowdown:
                RecruiterID=et_checkrest.getText().toString();
                llstep1.setVisibility(View.GONE);
                llstep2.setVisibility(View.VISIBLE);
                llstep3.setVisibility(View.VISIBLE);
                myTextView.setText("Enter Profile Information");
                break;
        }
    }

    private void gotoReactiveUrl() {
        Utility.message(this,"Welcome to Reactivate");
        Intent intent=new Intent(NewSignUp.this,PaymentView.class);
        intent.putExtra("username",Email);
        intent.putExtra("password",appGlobal.getPassword());
        intent.putExtra("RecruiterID",RecruiterID);
        intent.putExtra("UserD",appGlobal.getUserId()+"");
        startActivity(intent);
    }

    private void senddefauldID() {
        Utility.showLoadingPopup(NewSignUp.this);
        final HashMap<String,String> params=new HashMap<>();
        new ServiceController(NewSignUp.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data){
                if(success){
                    JSONArray jsonArray=null;
                    try {
                        jsonArray=new JSONArray(data);
                        if (jsonArray.length()>0)
                        {
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                Utility.hideLoadingPopup();
                                JSONObject Aff  = jsonArray.getJSONObject(i);
                                RecruiterID=Aff.getString("AffiliateId");
                                 ProductVariant="94";
                                 StaffName="";
                                Log.d("No",RecruiterID);
                                llstep1.setVisibility(View.GONE);
                                llstep2.setVisibility(View.VISIBLE);
                                llstep3.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(NewSignUp.this,data,success);
                }


            }
        }).request(ServiceMod.DefaultAffiliate,params);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        int id=v.getId();
        if(buttonnext.getVisibility()==View.VISIBLE)
        {
            buttonnext.setVisibility(View.GONE);
        }
        switch (id)
        {
            case R.id.et_checkrest:
            RecruiterID=et_checkrest.getText().toString();
            if(actionId== EditorInfo.IME_ACTION_SEARCH)
            {
                checkrest();
            }
            break;
            case R.id.et_checkrest1:
                RecruiterID=et_Rechechrest.getText().toString();
                if(actionId== EditorInfo.IME_ACTION_SEARCH)
                {
                    checkrest();
                }
                break;
            case R.id.et_email:
                if(actionId==EditorInfo.IME_ACTION_NEXT) {
                    String check="email";
                //    checkemail(check);
                }
                break;
        }
        return false;
    }

    private void checkrest() {
        Utility.showLoadingPopup(NewSignUp.this);
        final HashMap<String,String> params=new HashMap<>();
        params.put("AffiliateId",RecruiterID);
        new ServiceController(NewSignUp.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data){
                if(success){
                    JSONArray jsonArray=null;
                    try {
                        jsonArray=new JSONArray(data);
                        if (jsonArray.length()>0)
                        {

                            Utility.hideLoadingPopup();
                            Log.d("Muhib",""+jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject rest = jsonArray.getJSONObject(i);
                                String name = rest.getString("Name");
                                String type = rest.getString("MemberType");
                                Log.d("Name",name);
                                Log.d("Type",type);
                                // Glide.with(getApplicationContext()).load("file:///android_asset/arrow.gif").into(imageView);

                                if(type.equalsIgnoreCase("Member"))
                                {
                                 //   showstaffdialog("Edited");
                                    buttonnext.setVisibility(View.VISIBLE);
                                    txt_name.setText(type+" Name: "+name);

                                }
                                else
                                {
                                    Utility.message(NewSignUp.this,"Wrong Referral!");
                                }
                            }

                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            Utility.message(getApplicationContext(),"Please Enter Valid Number");
                            txt_name.setText("Member Name: ");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ErrorController.showError(NewSignUp.this,data,success);
                    Utility.hideLoadingPopup();
                }


            }
        }).request(ServiceMod.FindAffiliate,params);
    }

    private void checkemail(final String User) {
        Email=et_email.getText().toString();
        //et_email.setBackgroundResource(drawable.newtext_field_bg);
        if(!Email.isEmpty()) {

            if (Utility.checkValidEmail(Email)) {
                HashMap<String, String> params = new HashMap<>();
                params.put("Email", Email);
                new ServiceController(NewSignUp.this, new HttpResponseCallback() {
                    @Override
                    public void response(boolean success, boolean fail, String data) {
                        if (success) {

                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(data);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                        String mesg = jsonObjects.getString("OutputResult");
                                        if (mesg.equalsIgnoreCase("EmailExists")) {
                                            Utility.message(NewSignUp.this, "Email not available");
                                            //et_email.setBackgroundResource(android.R.color.holo_red_light);
                                        }
                                        else
                                        {
                                            if(User.equalsIgnoreCase("Checkout"))
                                            {
                                                sendtopayment();
                                            }
                                        }

                                    }
                                } else {
                                    Utility.message(NewSignUp.this, "Error");
                                }
                                progressBarlist.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                progressBarlist.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        } else {
                            progressBarlist.setVisibility(View.VISIBLE);

                            ErrorController.showError(NewSignUp.this, data, success);
                        }
                    }
                }).request(ServiceMod.EmailCheck, params);
            }
            else {
                Utility.Alertbox(NewSignUp.this,"Error","Provide Valid Email","OK");

            }
        }
        else
        {
            Utility.message(getApplicationContext(),"Please enter email");
        }



    }

    private void showstaffdialog(final String fromselected) {

        final AlertDialog.Builder build=new AlertDialog.Builder(NewSignUp.this);
        LayoutInflater inflater=NewSignUp.this.getLayoutInflater();
        View view1=inflater.inflate(R.layout.newsignupstafflayout,null);
        build.setView(view1);
        final ListView stafflist= (ListView) view1.findViewById(R.id.stafflist);
        final EditText staffinf= (EditText) view1.findViewById(id.et_enterstaff);
        progressBarlist = (ProgressBar) view1.findViewById(R.id.listprogress);
        ImageView imageView= (ImageView) view1.findViewById(id.ivGDFilterFormClose);
        SimpleArrayAdapter stringArrayAdapter=new SimpleArrayAdapter(this,satfslist);
        stafflist.setAdapter(stringArrayAdapter);
        TextView txt_staffsearch= (TextView) view1.findViewById(id.txt_staffsearch);
        txt_staffsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staffid=staffinf.getText().toString();
                InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(staffinf.getWindowToken(),0);
                selectStaff(stafflist,staffid);
            }
        });
        build.setCancelable(false);
       dial1= build.create();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromselected.equalsIgnoreCase("Edited"))
                {
                    dial1.dismiss();
                }
                else {
                    if (llstep1.getVisibility() == View.VISIBLE)
                    {
                        llstep1.setVisibility(View.GONE);
                    }
                    llstep2.setVisibility(View.VISIBLE);
                    llstep3.setVisibility(View.VISIBLE);
                    dial1.dismiss();
                }
            }
        });
        stafflist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AffiliateI = ((TextView) view.findViewById(R.id.id)).getText().toString();
               // Utility.message(NewSignUp.this,AffiliateI);
                dial1.dismiss();
                if(llstep1.getVisibility()==View.VISIBLE)
                {
                    llstep1.setVisibility(View.GONE);
                }
                llstep2.setVisibility(View.VISIBLE);
                llstep3.setVisibility(View.VISIBLE);
            }
        });
        dial1.show();

    }

    private void selectStaff(final ListView stafflist, final String staffid) {

        progressBarlist.setVisibility(View.VISIBLE);
        stafflist.setVisibility(View.VISIBLE);

        if(satfslist!=null)
        {
            satfslist.clear();
        }
        final HashMap<String, String> params = new HashMap<>();
        params.put("InputVariable",staffid);
        new ServiceController(NewSignUp.this, new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                if (success) {

                    JSONArray jsonArray = null;
                    try {
                        String Result="";
                        String Message="";
                        jsonArray = new JSONArray(data);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                Staffmodel staffmodel=new Staffmodel();
                                staffmodel.setFirstname(jsonObjects.getString("FirstName"));
                                staffmodel.setLastname(jsonObjects.getString("LastName"));
                                staffmodel.setUserId(jsonObjects.getString("UserID"));
                               /* String firstname=jsonObjects.getString("FirstName");
                                String LastName=jsonObjects.getString("LastName");
                                String UserID=jsonObjects.getString("UserID");*/
                                satfslist.add(staffmodel);
                              //  satfslist.add("Name: "+firstname+" "+LastName+",Staff ID: "+UserID);
                            }
                        } else {
                            Utility.message(NewSignUp.this,"No match found.");
                            stafflist.setVisibility(View.GONE);
                        }
                        progressBarlist.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        progressBarlist.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    progressBarlist.setVisibility(View.VISIBLE);

                    ErrorController.showError(NewSignUp.this, data, success);
                }
            }
        }).request(ServiceMod.SearchServer, params);
    }
    private void senddatatoUrl() {

        Semail=et_email.getText().toString();
        Spassword=et_password.getText().toString();
        Sfirstname=et_firstname.getText().toString();
        Slname=et_lastname.getText().toString();
        ScellNumber=et_cellnumber.getText().toString();
        Sstreet=et_address.getText().toString();
        Scity=et_city.getText().toString();
        Szipcode=et_zipcode.getText().toString();
        Sregion=et_state.getText().toString().toUpperCase();
        SMembershipProductVariantId="94";
        SDeviceId="31cf203f8d4469ab59f397be5f6df49ac1b2f6e682f6cf15b8d1aeafc72e7c25";
        SDeviceType="ANDROID";


        if(!Semail.isEmpty() && !Spassword.isEmpty() && !Sfirstname.isEmpty() && !Slname.isEmpty() && !ScellNumber.isEmpty() && !Sstreet.isEmpty() && !Scity.isEmpty()
                && !Szipcode.isEmpty() && !Sregion.isEmpty()) {
            if (ch_terms.isChecked()) {

                    //checkemail("Checkout");
                sendtopayment();
            }
            else {
                Utility.Alertbox(NewSignUp.this,"Error","Please Agree to Terms and Conditions","OK");
            }
        }
        else
        {
            Utility.Alertbox(NewSignUp.this,"Error","Please enter all mandatory fields","OK");
        }
    }

    private void sendtopayment() {
                   final HashMap<String, String> params = new HashMap<>();
                    params.put("RecruiterID",RecruiterID);
                    if(AffiliateI==null)
                      {
                          AffiliateI="";
                      }
                    params.put("AffiliateID",AffiliateI);
                    params.put("Email",Semail);
                    params.put("Password",Spassword);
                    params.put("FirstName",Sfirstname);
                    params.put("LastName",Slname);
                    params.put("Cell",ScellNumber);
                    params.put("Street",Sstreet);
                    params.put("City",Scity);
                    params.put("Country","United States");
                    params.put("Region",Sregion);
                    params.put("PostalCode",Szipcode);
                    params.put("ProfileImageBase64","");
                    params.put("MembershipProductVariantId","94");
                    params.put("DeviceId",SDeviceId);
                    params.put("DeviceType",SDeviceType);
                   params.put("Telephone",ScellNumber);
                    SMiles="75";
                    Utility.showLoadingPopup(NewSignUp.this);
                    new ServiceController(NewSignUp.this, new HttpResponseCallback()
                    {
                        @Override
                        public void response(boolean success, boolean fail, String data)
                        {
                            if(success)
                            {
                                Utility.hideLoadingPopup();
                                JSONArray jsonArray=null;
                                try {
                                    String Result="";
                                    String Message="";
                                    Log.d("Muhib",data);
                                    jsonArray = new JSONArray(data);

                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                        Result=jsonObjects.getString("Result");
                                        Message=jsonObjects.getString("Message");
                                        if(Result.equalsIgnoreCase("Success"))
                                        {
                                            UserId=  jsonObjects.getString("UserId");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(NewSignUp.this);
                                            builder.setTitle("Info");
                                            builder.setCancelable(false);
                                            builder.setMessage("Congratulations! Your account has been successfully created with GoDine."+"\n"+"You will now be re-direct to our website to complete the payment portion of your membership. Once you have completed your payment, you will be redirected to the App."+"\n"+"If payment fails for some reason, don’t worry you can simply login to your GoDine App using the user name and password that you created, and it will give you the option to complete your payment.");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    Intent intent=new Intent(NewSignUp.this, PaymentView.class);
                                                    intent.putExtra("username",Semail);
                                                    appGlobal.setPassword(Spassword);
                                                    appGlobal.setMiles(SMiles);
                                                    intent.putExtra("password",Spassword);
                                                    intent.putExtra("productvariantid","94");
                                                    intent.putExtra("UserD",UserId);
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.create();
                                            builder.show();
                                        }
                                        else
                                        {
                                            Utility.Alertbox(NewSignUp.this,"Info",Message,"OK");
                                        }

                                    }

                                } catch (JSONException e)
                                {
                                    String error = null;
                                    try {
                                        JSONObject object=new JSONObject(data);
                                       error=object.getString("errors");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    Utility.Alertbox(NewSignUp.this,"Info",error,"OK");
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                ErrorController.showError(NewSignUp.this,data,success);

                            }
                        }
                    }).request(ServiceMod.SIGN_UP,params);
    }

}
