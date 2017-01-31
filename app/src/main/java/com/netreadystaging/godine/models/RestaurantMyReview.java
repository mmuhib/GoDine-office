package com.netreadystaging.godine.models;

import java.io.Serializable;

/**
 * Created by lenovo on 1/21/2017.
 */

public class RestaurantMyReview implements Serializable {

    private String ratingId ;
    private String restId ;
    private String restName ;
    private String restImageUrl=""  ;
    private String address  ;
    private String city  ;
    private String state ;
    private String country  ;
    private String postalCode ;
    private RatingCard ratingCard ;
    private String reviewDate ;

    public String getRestImageUrl() {
        return restImageUrl;
    }

    public void setRestImageUrl(String restImageUrl) {
        this.restImageUrl = restImageUrl;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public RatingCard getRatingCard() {
        return ratingCard;
    }

    public void setRatingCard(RatingCard ratingCard) {
        this.ratingCard = ratingCard;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
}
