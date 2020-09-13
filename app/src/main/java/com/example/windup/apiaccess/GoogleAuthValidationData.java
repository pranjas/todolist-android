/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;


import android.util.JsonReader;
import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;
import com.example.windup.apiaccess.TPTAuthValidationData;
import com.example.windup.apiaccess.response.ResponseParser;

import java.io.IOException;
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

    private GoogleAuthValidationResponseParser parser;

    public GoogleAuthValidationData(byte[] responseData) {
       try {
           parser = new GoogleAuthValidationResponseParser(responseData);
       } catch (UnsupportedEncodingException | ApplicationException e) {
           Log.d(ApplicationConstants.LOG_TAG_ERROR, "error creating validation parser" +
                   e.getMessage());
       }
    }

    @Override
    public String getUserFirstname() {
        if (parser !=null)
            return parser.response.extras.get(JSON_FIELD_FIRSTNAME);
        return null;
    }

    @Override
    public String getUserLastname() {
        if (parser != null)
            return parser.response.extras.get(JSON_FIELD_LASTNAME);
        return  null;
    }

    @Override
    public String getUserID() {
        if (parser != null)
            return parser.response.extras.get(JSON_FIELD_UUSERID);
        return null;
    }

    @Override
    public String getAuthenticator() {
        return TPTAuthValidationData.AuthProviderGoogle;
    }

    @Override
    public int parseResponse() {
        if (parser != null) {
            parser.parseResponse();
            return 0;
        }
        return -1;
    }

    public String getLocale() {
        if (parser != null) {
            return parser.response.extras.get(JSON_FIELD_LOCALE);
        }
        return null;
    }

    public boolean isEmailVerified() {
        if (parser != null) {
            new Boolean(parser.response.extras.get(JSON_FIELD_EMAIL_VERIFIED)).booleanValue();
        }
        return false;
    }

    public String getPicture() {
        if (parser != null) {
            return parser.response.extras.get(JSON_FIELD_PICTURE);
        }
        return null;
    }

    public String getName() {
        if (parser != null) {
            return parser.response.extras.get(JSON_FIELD_NAME);
        }
        return null;
    }

    public String getEmail() {
        if (parser != null) {
            return parser.response.extras.get(JSON_FIELD_EMAIL);
        }
        return null;
    }

    public long getApiErrorCode() {
        if (parser != null) {
            return parser.response.errorCode;
        }
        return  -1;
    }


    private class GoogleAuthValidationResponseParser extends ResponseParser {

        public GoogleAuthValidationResponseParser(byte[] inputData) throws UnsupportedEncodingException, ApplicationException {
            super(inputData);
        }
        /*
         * We don't expect any objects and arrays in meta information.
         */
        @Override
        public void parseArray(String currentKey) {

        }
        @Override
        public void parseObject(String currentKey) {

        }

    }
}


