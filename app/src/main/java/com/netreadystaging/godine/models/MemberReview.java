package com.netreadystaging.godine.models;

import java.io.Serializable;

/**
 * Created by lenovo on 1/21/2017.
 */

public class MemberReview implements Serializable {

    private String memberId ;
    private String lastName ;
    private String firstName ;
    private RatingCard ratingCard ;
    private String reviewDate ;
    private String City ;
    private String State;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }



    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
