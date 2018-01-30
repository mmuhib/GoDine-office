package com.netreadystaging.godine.models;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class Staffmodel {
    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    String Firstname;
    String Lastname;
    String UserId;
}
