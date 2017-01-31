package com.netreadystaging.godine.views;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.models.RatingCard;

/**
 * Created by sony on 22-11-2016.
 **/

public class RatingCardView extends LinearLayout {

    private AppCompatRatingBar rBarAmbiance,rBarFoodQuality,rBarOverallExperience,rBarService,rBarWillYouReturn;


    public RatingCardView(Context context) {
        super(context);
        init();
    }

    public RatingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_rating_card,this);
        rBarAmbiance = (AppCompatRatingBar) view.findViewById(R.id.rBarAmbiance);
        rBarFoodQuality = (AppCompatRatingBar) view.findViewById(R.id.rBarFoodQuality);
        rBarOverallExperience = (AppCompatRatingBar)  view.findViewById(R.id.rBarOverallExperience);
        rBarService = (AppCompatRatingBar) view.findViewById(R.id.rBarService);
        rBarWillYouReturn = (AppCompatRatingBar)  view.findViewById(R.id.rBarWillYouReturn);


    }

    public void setView(RatingCard ratingCard) {

        final float ambianceRating = ratingCard.getAmbiance_rating() ;
        final float foodQualityRating = ratingCard.getFood_quality_rating();
        final float overallExpRating = ratingCard.getOverall_exp_rating();
        final float serviceRating = ratingCard.getService_rating() ;
        final float willYouReturnRating =  ratingCard.getWill_you_return_rating() ;
        final int reviewCount = ratingCard.getReviewCount() ;

        rBarAmbiance.setRating(ambianceRating);
        rBarFoodQuality.setRating(foodQualityRating);
        rBarOverallExperience.setRating(overallExpRating);
        rBarService.setRating(serviceRating);
        rBarWillYouReturn.setRating(willYouReturnRating);

    }
}
