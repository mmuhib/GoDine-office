package com.netreadystaging.godine.models;

import java.io.Serializable;

/**
 * Created by sony on 09-12-2016.
 */

public class Restaurant implements Serializable
{

    private String Id;
    private String name;
    private String Review;
    private String Address;
    private String Area;
    private String RestaurantCusine;

    private String city ;
    private String state ;
    private String zipcode ;
    private String restaurantFeatures;
    private String restaurantDaysOpen;
    private String restaurantOverview;
    private String restaurantMealServiceOffered;
    private String restaurantPhoneNumber;
    private String email;
    public double currentLat;
    public double currentLng ;

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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getRestaurantFeatures() {
        return restaurantFeatures;
    }

    public void setRestaurantFeatures(String restaurantFeatures) {
        this.restaurantFeatures = restaurantFeatures;
    }

    public String getRestaurantDaysOpen() {
        return restaurantDaysOpen;
    }

    public void setRestaurantDaysOpen(String restaurantDaysOpen) {
        this.restaurantDaysOpen = restaurantDaysOpen;
    }

    public String getRestaurantOverview() {
        return restaurantOverview;
    }

    public void setRestaurantOverview(String restaurantOverview) {
        this.restaurantOverview = restaurantOverview;
    }

    public String getRestaurantMealServiceOffered() {
        return restaurantMealServiceOffered;
    }

    public void setRestaurantMealServiceOffered(String restaurantMealServiceOffered) {
        this.restaurantMealServiceOffered = restaurantMealServiceOffered;
    }

    public String getRestaurantPhoneNumber() {
        return restaurantPhoneNumber;
    }

    public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
        this.restaurantPhoneNumber = restaurantPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialRestrictions() {
        return specialRestrictions;
    }

    public void setSpecialRestrictions(String specialRestrictions) {
        this.specialRestrictions = specialRestrictions;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public void setHoursOfOperation(String hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;
    }

    public String getEntreePriceRange() {
        return entreePriceRange;
    }

    public void setEntreePriceRange(String entreePriceRange) {
        this.entreePriceRange = entreePriceRange;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isOpenedNow() {
        return isOpenedNow;
    }

    public void setOpenedNow(boolean openedNow) {
        isOpenedNow = openedNow;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    private String specialRestrictions;
    private boolean isFavorite ;
    private boolean isReviewed ;
    private String hoursOfOperation;
    private String entreePriceRange ;
    private String website ;
    private boolean isOpenedNow = false ;

    private String Lunch;
    private String Dinner;

    public double lat;
    public double lng ;
    private float rating ;
    private int offers ;
    private long miles ;
    private String image;

    public int getOffers() {
        return offers;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setOffers(int offers) {
        this.offers = offers;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getMiles() {
        return miles;
    }

    public void setMiles(long miles) {
        this.miles = miles;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getLunch() {
        return Lunch;
    }

    public void setLunch(String lunch) {
        Lunch = lunch;
    }

    public String getDinner() {
        return Dinner;
    }

    public void setDinner(String dinner) {
        Dinner = dinner;
    }


    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRestaurantCusine() {
        return RestaurantCusine;
    }

    public void setRestaurantCusine(String restaurantCusine) {
        RestaurantCusine = restaurantCusine;
    }




}
