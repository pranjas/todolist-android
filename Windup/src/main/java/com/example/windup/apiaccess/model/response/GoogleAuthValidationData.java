/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model.response;


import androidx.annotation.NonNull;

import com.example.windup.ApplicationException;

import java.io.UnsupportedEncodingException;

public class GoogleAuthValidationData extends TPTAuthValidationData {
    /*
     * These fields would be present in the response from server.
     * These replicate exactly the names as are available in the token
     * generated from google.
     */
    public static final String  JSON_FIELD_NAME = "name",
            JSON_FIELD_EMAIL = "email",
            JSON_FIELD_EMAIL_VERIFIED = "email_verified",
            JSON_FIELD_PICTURE = "picture",
            JSON_FIELD_FIRSTNAME = "given_name",
            JSON_FIELD_LASTNAME = "family_name",
            JSON_FIELD_LOCALE = "locale",
            JSON_FIELD_UUSERID = "userid"; //Unique User ID of this google account.

    private Response response;

    public  GoogleAuthValidationData(@NonNull Response response) {
        this.response = response;
    }

    @Override
    public String getUserFirstname() {
            return String.valueOf(response.extras.get(JSON_FIELD_FIRSTNAME));
    }

    @Override
    public String getUserLastname() {
            return String.valueOf(response.extras.get(JSON_FIELD_LASTNAME));
    }

    @Override
    public String getUserID() {
            return String.valueOf(response.extras.get(JSON_FIELD_UUSERID));
    }

    @Override
    public String getAuthenticator() {
        return TPTAuthValidationData.AuthProviderGoogle;
    }


    public String getLocale() {
            return String.valueOf(response.extras.get(JSON_FIELD_LOCALE));
    }

    public boolean isEmailVerified() {
            return new Boolean(String.valueOf(response.extras.get(JSON_FIELD_EMAIL_VERIFIED))).booleanValue();
    }

    public String getPicture() {
            return String.valueOf(response.extras.get(JSON_FIELD_PICTURE));
    }

    public String getName() {
            return String.valueOf(response.extras.get(JSON_FIELD_NAME));
    }

    public String getEmail() {
            return String.valueOf(response.extras.get(JSON_FIELD_EMAIL));
    }

    public long getApiErrorCode() {
           return response.errorCode;
    }
}


