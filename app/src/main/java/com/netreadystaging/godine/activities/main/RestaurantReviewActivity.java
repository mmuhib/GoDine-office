package com.netreadystaging.godine.activities.main;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.adapters.MemberReviewAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.fragments.ProfilePageFragment;
import com.netreadystaging.godine.fragments.RestaurantProfile;
import com.netreadystaging.godine.models.MemberReview;
import com.netreadystaging.godine.models.RatingCard;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;
import com.netreadystaging.godine.views.RatingCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

public class RestaurantReviewActivity extends AppBaseActivity {


    private ListView lvMemberReview;
    String restId,saving;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_review);
        lvMemberReview = (ListView) findViewById(R.id.lvMemberReview);
        setupTopBar();

        Intent intent =  getIntent() ;
        if(intent!=null)
        {
            restId = intent.getStringExtra("rest_id");
            saving=intent.getStringExtra("From near");

            if(Utility.isNetworkConnected(getApplicationContext())) {
                loadRestaurantReview(restId);
                loadRestaurantCustomerReviews(restId);
            }
        }

        final AppCompatRatingBar rBarInputAmbiance = (AppCompatRatingBar) findViewById(R.id.rBarInputAmbiance) ;
        final AppCompatRatingBar rBarInputFoodQuality = (AppCompatRatingBar) findViewById(R.id.rBarInputFoodQuality) ;
        final AppCompatRatingBar rBarInputService = (AppCompatRatingBar) findViewById(R.id.rBarInputService) ;
        final AppCompatRatingBar rBarInputOverallExperience = (AppCompatRatingBar) findViewById(R.id.rBarInputOverallExperience) ;
        final AppCompatRatingBar rBarInputWillYouReturn = (AppCompatRatingBar) findViewById(R.id.rBarInputWillYouReturn) ;

        final Button btnSubmitRating = (Button) findViewById(R.id.btnSubmitRating);
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int ambianceRating = (int) rBarInputAmbiance.getRating();
                final int foodQualityRating = (int) rBarInputFoodQuality.getRating();
                final int serviceRating = (int) rBarInputService.getRating();
                final int overallExpRating = (int) rBarInputOverallExperience.getRating();
                final int willYouReturnRating = (int) rBarInputWillYouReturn.getRating();

                updateUserRating(ambianceRating,foodQualityRating,serviceRating,overallExpRating,willYouReturnRating);

            }
        });

    }

    private void updateUserRating(int ar, int fqr, int sr, int oaer, int wyrr) {
        Utility.showLoadingPopup(RestaurantReviewActivity.this);
        final HashMap<String,String> params=new HashMap<>();
        params.put("Ambience",String.valueOf(ar));
        params.put("FoodQuality",String.valueOf(fqr));
        params.put("OverallExeperience",String.valueOf(oaer));
        params.put("Restaurantid",restId);
        params.put("Service",String.valueOf(sr));
        params.put("UserId", AppGlobal.getInatance().getUserId()+"");
        params.put("WillYouReturn",String.valueOf(wyrr));

        new ServiceController(RestaurantReviewActivity.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                Utility.hideLoadingPopup();
                if(success)
                {
                    JSONArray jsonArray ;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ratingJsonObject =  jsonArray.getJSONObject(i);
                                String result = ratingJsonObject.getString("Result").toLowerCase().trim();
                                if(result.equals("success")){
                                  //  onBackPressed();
                                    //Toast.makeText(RestaurantReviewActivity.this, ratingJsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder build=new AlertDialog.Builder(RestaurantReviewActivity.this);
                                    build.setMessage("Thank You.Your review has been submitted.");
                                    build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        Intent mai=new Intent(RestaurantReviewActivity.this,MainPageActivity.class);
                                            startActivity(mai);
                                          /*  RestaurantProfile rest=new RestaurantProfile();
                                            Bundle restBundle = new Bundle();
                                            restBundle.putSerializable("rest_id",restId);
                                            rest.setArguments(restBundle);
                                            getSupportFragmentManager().beginTransaction().replace(R.id.flContent,rest).addToBackStack(null).commit() ;*/
                                        }
                                    });
                                    build.create();
                                    build.show();
                                }else {
                                    Utility.Alertbox(RestaurantReviewActivity.this,"Info","Something Wrong","OK");
                                }
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
//                            AlertDialog.Builder builder =  new AlertDialog.Builder(RestaurantReviewActivity.this);
//                            builder.setMessage("Something Wrong") ;
//                            builder.setTitle("Info");
//                            builder.create() ;
//                            builder.show() ;

                            Utility.Alertbox(RestaurantReviewActivity.this,"Info","Something Wrong","OK");

                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace() ;
//                        AlertDialog.Builder builder =  new AlertDialog.Builder(RestaurantReviewActivity.this);
//                        builder.setMessage("Something Wrong") ;
//                        builder.setTitle("Info");
//                        builder.create() ;
//                        builder.show() ;
                        Utility.Alertbox(RestaurantReviewActivity.this,"Info","Something Wrong","OK");
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(RestaurantReviewActivity.this,data,success);
                }
            }
        }).request(ServiceMod.LEAVE_REVIEW,params);

    }


    private void setupTopBar()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView ivToolBarNavigationIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        final ImageView ivToolBarBack = (ImageView)toolbar.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final TextView title= (TextView)toolbar.findViewById(R.id.tvToolBarMiddleLabel) ;
        title.setVisibility(View.VISIBLE);
        title.setTextSize(18);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setText("Review this Restaurant");
        final ImageView ivToolBarEndIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarEndIcn.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
    }


    private void loadRestaurantReview(final String restId) {
        Utility.showLoadingPopup(RestaurantReviewActivity.this);
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantId",restId);
        new ServiceController(RestaurantReviewActivity.this, new HttpResponseCallback()
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
                                JSONObject reviewSuccessObj  =  jsonArray.getJSONObject(i);
                                float foodQualityRating = (float) reviewSuccessObj.getDouble("FoodQuality");
                                float ambianceRating =  (float) reviewSuccessObj.getDouble("Ambiance");
                                float serviceRating = (float)  reviewSuccessObj.getDouble("Service");
                                float overallExperienceRating = (float)  reviewSuccessObj.getDouble("OverallExperience");
                                float willYouReturnRating = (float)  reviewSuccessObj.getDouble("WillYouReturn");
                                float overallRating = (float)  reviewSuccessObj.getDouble("OverallRating");

                                RatingCard ratingCard = new RatingCard();
                                ratingCard.setFood_quality_rating(foodQualityRating);
                                ratingCard.setAmbiance_rating(ambianceRating);
                                ratingCard.setOverall_exp_rating(overallExperienceRating);
                                ratingCard.setService_rating(serviceRating);
                                ratingCard.setWill_you_return_rating(willYouReturnRating);

                                int reviewCount = reviewSuccessObj.getInt("RateCount");
                                ratingCard.setReviewCount(reviewCount);

                                final RatingCardView membersOverAllRatingCard = (RatingCardView) findViewById(R.id.membersOverAllRatingCard);
                                membersOverAllRatingCard.setView(ratingCard);

                                final AppCompatRatingBar rBarOverall = (AppCompatRatingBar) findViewById(R.id.rBarOverall);
                                rBarOverall.setRating(overallRating);

                                final TextView tvRatingValue = (TextView) findViewById(R.id.tvRatingValue);
                                tvRatingValue.setText(String.valueOf(overallRating));

                                final TextView tvReviewCount = (TextView) findViewById(R.id.tvReviewCount);
                                tvReviewCount.setText(reviewCount+" Review");
                                break ;
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
//                            AlertDialog.Builder builder =  new AlertDialog.Builder(RestaurantReviewActivity.this);
//                            builder.setMessage("No Review.") ;
//                            builder.setTitle("Info");
//                            builder.create() ;
//                            builder.show() ;
                            Utility.Alertbox(RestaurantReviewActivity.this,"Info","No Review.","OK");
                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(RestaurantReviewActivity.this,data,success);
                }
            }
        }).request(ServiceMod.RESTAURANT_REVIEW,params);
    }

    private void loadRestaurantCustomerReviews(final String restId) {
        final ArrayList<MemberReview> listMemberReview =  new ArrayList<>();
        Utility.showLoadingPopup(RestaurantReviewActivity.this);
        final HashMap<String,String> params=new HashMap<>();
        params.put("RestaurantId",restId);
        new ServiceController(RestaurantReviewActivity.this, new HttpResponseCallback()
        {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                Utility.hideLoadingPopup();
                if(success)
                {
                    JSONArray jsonArray ;
                    try {
                        jsonArray = new JSONArray(data);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MemberReview memberReview = new MemberReview();
                                JSONObject reviewSuccessObj  =  jsonArray.getJSONObject(i);
                                String userId =  reviewSuccessObj.getString("UserId");

                                float foodQualityRating = (float) reviewSuccessObj.getDouble("FoodQuality");
                                float ambianceRating =  (float) reviewSuccessObj.getDouble("Ambiance");
                                float serviceRating = (float)  reviewSuccessObj.getDouble("Service");
                                float overallExperienceRating = (float)  reviewSuccessObj.getDouble("OverallExeperience");
                                float willYouReturnRating = (float)  reviewSuccessObj.getDouble("WillYouReturn");

                                String fName  =  reviewSuccessObj.getString("FirstName");
                                String lName  =  reviewSuccessObj.getString("LastName");
                                String reviewDate  =  reviewSuccessObj.getString("ReviewDate");
                                String City=reviewSuccessObj.getString("City");
                                String State= reviewSuccessObj.getString("State");

                                RatingCard ratingCard = new RatingCard();
                                ratingCard.setFood_quality_rating(foodQualityRating);
                                ratingCard.setAmbiance_rating(ambianceRating);
                                ratingCard.setOverall_exp_rating(overallExperienceRating);
                                ratingCard.setService_rating(serviceRating);
                                ratingCard.setWill_you_return_rating(willYouReturnRating);

                                memberReview.setMemberId(userId);
                                memberReview.setReviewDate(reviewDate);
                                memberReview.setRatingCard(ratingCard);
                                memberReview.setFirstName(fName);
                                memberReview.setLastName(lName);
                                memberReview.setCity(City+", "+State);
                                listMemberReview.add(memberReview);
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
//                            AlertDialog.Builder builder =  new AlertDialog.Builder(RestaurantReviewActivity.this);
//                            builder.setMessage("No Review.") ;
//                            builder.setTitle("Info");
//                            builder.create() ;
//                            builder.show() ;

                            Utility.Alertbox(RestaurantReviewActivity.this,"Info","No Review.","OK");
                        }
                    } catch (JSONException e) {
                        Utility.hideLoadingPopup();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Utility.hideLoadingPopup();
                    ErrorController.showError(RestaurantReviewActivity.this,data,success);
                }

                // Review Adapter List
                MemberReviewAdapter reviewAdapter = new MemberReviewAdapter(RestaurantReviewActivity.this,listMemberReview);
                lvMemberReview.setAdapter(reviewAdapter);

            }
        }).request(ServiceMod.RESTAURANT_REVIEWS_CUSTOMER_LISTS,params);
    }

}
