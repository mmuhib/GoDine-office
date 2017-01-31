package com.netreadystaging.godine.models;

import java.io.Serializable;

/**
 * Created by sony on 19-01-2017.
 */

public class OfferList implements Serializable {
    private String offerId ;
    private String RestaurantId;
    private String RestaurantName;
    private String Offer;
    private String OfferName;
    private String Description;
    private String OfferEndDate;
    private String image;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getOffer() {
        return Offer;
    }

    public void setOffer(String offer) {
        Offer = offer;
    }

    public String getOfferEndDate() {
        return OfferEndDate;
    }

    public void setOfferEndDate(String offerEndDate) {
        OfferEndDate = offerEndDate;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }


}
