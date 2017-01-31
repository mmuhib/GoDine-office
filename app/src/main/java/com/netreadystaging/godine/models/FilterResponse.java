package com.netreadystaging.godine.models;

import java.io.Serializable;

/**
 * Created by lenovo on 1/19/2017.
 */

public class FilterResponse implements Serializable {

    private String cuisines =  "";
    private String features =  "";
    private String price =  "";
    private String rating =  "";

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
