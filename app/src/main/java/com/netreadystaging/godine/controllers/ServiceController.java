package com.netreadystaging.godine.controllers;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.netreadystaging.godine.utils.ServiceMod;

import java.util.HashMap;

import in.technobuff.helper.http.HttpRequestService;
import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 18-07-2016.
 */
public class ServiceController extends Application {

    HttpRequestService service  ;
    Context context ;


    final private String API_BASE_URL = "https://godineclub.com/api/" ;
    final private String AUTH_TOKEN ="CDwbClOOk61h3lQrTMbAkFNK5xB2r25c" ;

    public ServiceController(Context context, final HttpResponseCallback callback) {
        this.context = context;

        this.service = new HttpRequestService(this.context, new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                callback.response(success,fail,data);
                Log.d("response",data);
            }
        });
    }

    public void request(String method, HashMap<String,String> params)
    {
        String url= API_BASE_URL+method ;
        switch(method)
        {
            case ServiceMod.LOGIN :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.GET_RESTAURANT_SAVINGS :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.CALL_BACK_REQUEST :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.ReferSignUp :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.FEEDBACK:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.SEARCH_RESTAURANT:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.USER_PROFILE:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.RESTAURANT_DETAILS:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.ProfilePicDownload:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.REST_TYPE_LISTING:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.InsertRestaurantSavings:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.REVIEWS_BY_USER:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.OFFER_LIST:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.MARK_FAVOURITE:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.UNMARK_FAVOURITE:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.RESTAURANT_MENU:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.RESTAURANT_OFFER:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.FAVOURITE_RESTAURANT_LIST:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.LEAVE_REVIEW:
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.CancelMembership :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            case ServiceMod.SIGN_UP :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
            default :
                this.service.setAuthToken(AUTH_TOKEN);
                break ;
        }
        this.service.setParams(params) ;
        this.service.execute(url) ;
    }
}
