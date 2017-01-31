package com.netreadystaging.godine.models;

import android.util.Log;

/**
 * Created by sony on 15-12-2016.
 */

public class MemberList
{
    String UserId;
    String Username;
    String FirstName;
    String LastName;
    String Email;
    String Cell;
    String DisplayName;
    String ProfileImage;

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }



    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDisplayName() {
            return DisplayName;
    }

    public void setDisplayName(String displayName) {
             DisplayName = displayName;
    }

    public String getCell() {
            return Cell;
    }

    public void setCell(String cell) {
             Cell = cell;
    }


}
