package com.netreadystaging.godine.utils;

import android.app.Application;
import android.content.Context;

import com.helpshift.All;
import com.helpshift.Core;
import com.helpshift.InstallConfig;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.Support;

import in.technobuff.helper.utils.SharedPreferenceStore;


/**
 * Created by sony on 19-07-2016.
 */
public class AppGlobal extends Application{

    private static AppGlobal appGlobal ;
    public Context context ;
    public String welcomeText =  "Welcome" ;
    private int userId = 0;
    private String username = "";
    private String pass = "";
    private String firstName = "";
    private String lastName = "";
    private String mobile = "";
    private String telephone = "";
    private String emailId = "";
    private String address = "";
    private  String city = "";
    private String state = "";
    private String zipcode = "";
    private String mobileNotification = "";
    private String miles ="0";
    private int membershipId = -1;
    private String memberType = "";
    private String memberExpiryDate = "";
    private String memberSince = "";
    private String parentRestaurantIdOrUserId = "";
    private String isVerificationImageUploaded = "";
    public String p_remember= "";
    public String u_remember = "";
    public String getAddress()
    {

        return address;
    }



    public void setAddress(String address) {
        SharedPreferenceStore.storeValue(context,LSConstant.ADDRESS,address);
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        SharedPreferenceStore.storeValue(context,LSConstant.CITY,city);

        this.city = city;
    }

    public String getEmailId() {

        return emailId;
    }


    public void setEmailId(String emailId) {
        SharedPreferenceStore.storeValue(context ,LSConstant.EMAIL_ID,emailId);

        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        SharedPreferenceStore.storeValue(context ,LSConstant.FIRST_NAME,firstName);


        this.firstName = firstName;
    }

    public String getIsVerificationImageUploaded() {
        return isVerificationImageUploaded;
    }

    public void setIsVerificationImageUploaded(String isVerificationImageUploaded) {
        SharedPreferenceStore.storeValue(context ,LSConstant.IS_VERIFICATION_IMAGE_UPLOADED,isVerificationImageUploaded);
        this.isVerificationImageUploaded = isVerificationImageUploaded;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        SharedPreferenceStore.storeValue(context ,LSConstant.LAST_NAME,lastName);
        this.lastName = lastName;
    }

    public String getMemberExpiryDate() {
        return memberExpiryDate;
    }

    public void setMemberExpiryDate(String memberExpiryDate) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MEMBER_EXPIRY_DATE,memberExpiryDate);
        this.memberExpiryDate = memberExpiryDate;
    }

    public int getMembershipId() {
        return this.membershipId;
    }

    public void setMembershipId(int membershipId) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MEMBERSHIP_ID,membershipId);
        this.membershipId = membershipId;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MEMBER_SINCE,memberSince);
        this.memberSince = memberSince;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MEMBER_TYPE,memberType);

        this.memberType = memberType;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MILES,miles);

        this.miles = miles;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MOBILE,mobile);
        this.mobile = mobile;
    }

    public String getMobileNotification() {
        return mobileNotification;
    }

    public void setMobileNotification(String mobileNotification) {
        SharedPreferenceStore.storeValue(context ,LSConstant.MOBILE_NOTIFICATION,mobileNotification);
        this.mobileNotification = mobileNotification;
    }

    public String getParentRestaurantIdOrUserId() {
        return parentRestaurantIdOrUserId;
    }

    public void setParentRestaurantIdOrUserId(String parentRestaurantIdOrUserId) {
        SharedPreferenceStore.storeValue(context ,LSConstant.PARENT_RESTAURANT_ID_OR_USER_ID,parentRestaurantIdOrUserId);
        this.parentRestaurantIdOrUserId = parentRestaurantIdOrUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {

        SharedPreferenceStore.storeValue(context ,LSConstant.STATE,state);
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        SharedPreferenceStore.storeValue(context ,LSConstant.TELEPHONE,telephone);
        this.telephone = telephone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        SharedPreferenceStore.storeValue(context ,LSConstant.USER_ID,userId);
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        SharedPreferenceStore.storeValue(context ,LSConstant.USERNAME,username);
        this.username = username;
    }

    public String getPassword()
    {
        return pass;
    }
    public void setPassword(String pass) {
        SharedPreferenceStore.storeValue(context ,LSConstant.PASSWORD,pass);
        this.pass = pass;
    }
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        SharedPreferenceStore.storeValue(context ,LSConstant.ZIPCODE,zipcode);
        this.zipcode = zipcode;
    }
    public boolean isRemember() {
        boolean isRemember  = SharedPreferenceStore.getValue(context,LSConstant.IS_REMEMBER,false);
        if(isRemember)
        {
            this.p_remember = getRememberedPass();
            this.u_remember = getRememberedUser();
        }
        else
        {
            this.p_remember = "";
            this.u_remember = "";
        }
        return  isRemember;
    }

    public void setRemember(boolean remember) {
        SharedPreferenceStore.storeValue(context ,LSConstant.IS_REMEMBER,remember);
        if(remember)
        {
            setRememberPass(pass);
            setRememberUser(username);
        }
        else
        {
            setRememberPass("");
            setRememberUser("");
        }
    }
    private String getRememberedPass()
    {
        return SharedPreferenceStore.getValue(context,LSConstant.PASS_REMEMBER,"");
    }
    private String getRememberedUser()
    {
        return SharedPreferenceStore.getValue(context,LSConstant.USER_REMEMBER,"");
    }
    private void setRememberPass(String p_remember) {
        this.p_remember =  p_remember ;
        SharedPreferenceStore.storeValue(context ,LSConstant.PASS_REMEMBER,p_remember);
    }
    private void setRememberUser(String u_remember) {
        this.u_remember = u_remember ;
        SharedPreferenceStore.storeValue(context ,LSConstant.USER_REMEMBER,u_remember);
    }
    public void resetAppGlobalParams()
    {
        setUserId(0);
        setAddress("");
        setCity("");
        setEmailId("");
        setFirstName("");
        setLastName("");
        setIsVerificationImageUploaded("");
        setMembershipId(-1);
        setMemberSince("");
        setMemberExpiryDate("");
        setMemberType("");
        setMiles("");
        setMobile("");
        setTelephone("");
        setState("");
        setParentRestaurantIdOrUserId("");
        setMobileNotification("");
        setZipcode("");

        setUsername("");
        setPassword("");
        welcomeText = "Welcome" ;
    }

    public boolean copyLStoAppGlobal()
    {
        userId = SharedPreferenceStore.getValue(context,LSConstant.USER_ID,userId);
        if(userId>0) {
            address = SharedPreferenceStore.getValue(context, LSConstant.ADDRESS, address);
            city = SharedPreferenceStore.getValue(context, LSConstant.CITY, city);
            emailId = SharedPreferenceStore.getValue(context, LSConstant.EMAIL_ID, emailId);
            firstName = SharedPreferenceStore.getValue(context, LSConstant.FIRST_NAME, firstName);
            isVerificationImageUploaded = SharedPreferenceStore.getValue(context, LSConstant.IS_VERIFICATION_IMAGE_UPLOADED, isVerificationImageUploaded);
            lastName = SharedPreferenceStore.getValue(context, LSConstant.LAST_NAME, lastName);
            memberExpiryDate = SharedPreferenceStore.getValue(context, LSConstant.MEMBER_EXPIRY_DATE, memberExpiryDate);
            membershipId = SharedPreferenceStore.getValue(context, LSConstant.MEMBERSHIP_ID, membershipId);
            memberSince = SharedPreferenceStore.getValue(context, LSConstant.MEMBER_SINCE, memberSince);
            memberType = SharedPreferenceStore.getValue(context, LSConstant.MEMBER_TYPE, memberType);
            miles = SharedPreferenceStore.getValue(context, LSConstant.MILES, miles);
            mobile = SharedPreferenceStore.getValue(context, LSConstant.MOBILE, mobile);
            telephone = SharedPreferenceStore.getValue(context, LSConstant.TELEPHONE, telephone);
            state = SharedPreferenceStore.getValue(context, LSConstant.STATE, state);
            parentRestaurantIdOrUserId = SharedPreferenceStore.getValue(context, LSConstant.PARENT_RESTAURANT_ID_OR_USER_ID, parentRestaurantIdOrUserId);
            mobileNotification = SharedPreferenceStore.getValue(context, LSConstant.MOBILE_NOTIFICATION, mobileNotification);
            zipcode = SharedPreferenceStore.getValue(context, LSConstant.ZIPCODE, zipcode);
            username = SharedPreferenceStore.getValue(context, LSConstant.USERNAME, username);
            pass = SharedPreferenceStore.getValue(context, LSConstant.PASSWORD, pass);
            welcomeText =  "Welcome Back" ;
            return true ;
        }

        return false ;
    }

    public static AppGlobal getInatance()
    {
        if(appGlobal==null)
        {
            appGlobal = new AppGlobal();
        }
        return appGlobal ;
    }

}
