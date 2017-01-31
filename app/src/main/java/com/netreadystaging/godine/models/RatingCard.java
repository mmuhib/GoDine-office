package com.netreadystaging.godine.models;

import java.io.Serializable;

/**
 * Created by lenovo on 1/20/2017.
 */

public class RatingCard implements Serializable{
    private float food_quality_rating = 0.0f  ;
    private float ambiance_rating = 0.0f  ;
    private float service_rating = 0.0f  ;
    private float overall_exp_rating = 0.0f  ;
    private float will_you_return_rating = 0.0f  ;
    private int reviewCount = -1 ;

    public float getFood_quality_rating() {
        return food_quality_rating;
    }

    public void setFood_quality_rating(float food_quality_rating) {
        this.food_quality_rating = food_quality_rating;
    }

    public float getAmbiance_rating() {
        return ambiance_rating;
    }

    public void setAmbiance_rating(float ambiance_rating) {
        this.ambiance_rating = ambiance_rating;
    }

    public float getService_rating() {
        return service_rating;
    }

    public void setService_rating(float service_rating) {
        this.service_rating = service_rating;
    }

    public float getOverall_exp_rating() {
        return overall_exp_rating;
    }

    public void setOverall_exp_rating(float overall_exp_rating) {
        this.overall_exp_rating = overall_exp_rating;
    }

    public float getWill_you_return_rating() {
        return will_you_return_rating;
    }

    public void setWill_you_return_rating(float will_you_return_rating) {
        this.will_you_return_rating = will_you_return_rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
