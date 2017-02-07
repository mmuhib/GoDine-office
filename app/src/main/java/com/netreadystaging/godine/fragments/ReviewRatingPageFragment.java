package com.netreadystaging.godine.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.adapters.RestaurantMyReviewAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.RatingCard;
import com.netreadystaging.godine.models.RestaurantMyReview;
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
public class ReviewRatingPageFragment extends Fragment {

    View view ;
    TextView title ;
    AppGlobal appGlobal=AppGlobal.getInatance();
    private ListView lvMemberReview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.review_rating_page_fragment,container,false);
        setupToolBar();
        lvMemberReview = (ListView) view.findViewById(R.id.lvMemberReview) ;
        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
        bottomToolBar.setVisibility(View.GONE);

        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
    }

    private void loadUserReviews() {
        final ArrayList<RestaurantMyReview> listMemberReview =  new ArrayList<>();
        Utility.showLoadingPopup(getActivity());
        HashMap<String,String> params =  new  HashMap<>();
        params.put("userId", String.valueOf(appGlobal.getUserId()));
        new ServiceController(getActivity(), new HttpResponseCallback()
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
                                RestaurantMyReview myReview = new RestaurantMyReview();
                                JSONObject reviewSuccessObj  =  jsonArray.getJSONObject(i);
                                String ratingId =  reviewSuccessObj.getString("RatingId");
                                String restId =  reviewSuccessObj.getString("RestaurantId");
                                String restName =  reviewSuccessObj.getString("RestaurantName");
                                String address =  reviewSuccessObj.getString("Address");
                                String city =  reviewSuccessObj.getString("City");
                                String state =  reviewSuccessObj.getString("Region");
                                String country =  reviewSuccessObj.getString("Country");
                                String postalCode =  reviewSuccessObj.getString("PostalCode");
                                String restImageUrl =  reviewSuccessObj.getString("ImageUrl");
                                String reviewDate  =  reviewSuccessObj.getString("DateOfReview");

                                float foodQualityRating = (float) reviewSuccessObj.getDouble("FoodQuality");
                                float ambianceRating =  (float) reviewSuccessObj.getDouble("Ambience");
                                float serviceRating = (float)  reviewSuccessObj.getDouble("Service");
                                float overallExperienceRating = (float)  reviewSuccessObj.getDouble("OverallExeperience");
                                float willYouReturnRating = (float)  reviewSuccessObj.getDouble("WillYouReturn");


                                RatingCard ratingCard = new RatingCard();
                                ratingCard.setFood_quality_rating(foodQualityRating);
                                ratingCard.setAmbiance_rating(ambianceRating);
                                ratingCard.setOverall_exp_rating(overallExperienceRating);
                                ratingCard.setService_rating(serviceRating);
                                ratingCard.setWill_you_return_rating(willYouReturnRating);

                                myReview.setRatingId(ratingId);
                                myReview.setAddress(address);
                                myReview.setCity(city);
                                myReview.setState(state);
                                myReview.setCountry(country);
                                myReview.setPostalCode(postalCode);

                                myReview.setRestName(restName);
                                myReview.setRestId(restId);
                                myReview.setReviewDate(reviewDate);
                                myReview.setRatingCard(ratingCard);
                                myReview.setRestImageUrl(restImageUrl);

                                listMemberReview.add(myReview);
                            }
                        }
                        else
                        {
                            Utility.hideLoadingPopup();
                            Utility.Alertbox(getActivity(),"Info","No Review.","OK");
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

                // Review Adapter List
                RestaurantMyReviewAdapter myReviewAdapter = new RestaurantMyReviewAdapter(getActivity(),listMemberReview);
                lvMemberReview.setAdapter(myReviewAdapter);

            }
        }).request(ServiceMod.REVIEWS_BY_USER,params);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadUserReviews();
        title.setText("My Reviews");
    }
}
