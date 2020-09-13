/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;
import com.example.windup.apiaccess.GoogleAuthValidationData;
import com.example.windup.apiaccess.LoginAPIAccess;
import com.example.windup.apiaccess.TPTAuthValidationAPIAccess;
import com.example.windup.apiaccess.TPTAuthValidationData;
import com.example.windup.apiaccess.response.Response;
import com.example.windup.data.model.LoggedInUser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public LoggedInUser login(final String username, final String password) {
        LoggedInUser user = null;
        try {
            LoginAPIAccess loginAPIAccess = new LoginAPIAccess(username, password);
            String bearerToken = null;

            loginAPIAccess.execute();
            if (loginAPIAccess.isSuccess())
                bearerToken = loginAPIAccess.getBearerToken();
            if (bearerToken != null && bearerToken.length() != 0) {
                user =new LoggedInUser(username, username);
                user.setBearerToken(bearerToken);
            }
        } catch (Exception e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Error in login - " +
                    this.getClass().getName()+ ": " +
                    e.getMessage());
        } finally {
            return user;
        }
    }

    public LoggedInUser validateToken(String token, String authProvider) {
        switch (authProvider) {
            case TPTAuthValidationData.AuthProviderGoogle:
                return validateGoogleSignin(token);
            default:
                return null;
        }
    }

    private LoggedInUser validateGoogleSignin(String googleTokenId) {
        LoggedInUser user = null;
        try {
            String bearerToken = null;
            TPTAuthValidationAPIAccess validationAPIAccess = new TPTAuthValidationAPIAccess(googleTokenId,
                    TPTAuthValidationData.AuthProviderGoogle);

            validationAPIAccess.execute();

            TPTAuthValidationData tptAuthValidationData = validationAPIAccess.getTptAuthValidationData();
            if (validationAPIAccess.isSuccess())
                bearerToken = validationAPIAccess.getBearerToken();
            else {
                if (tptAuthValidationData instanceof GoogleAuthValidationData) {
                    GoogleAuthValidationData data = (GoogleAuthValidationData) tptAuthValidationData;
                    if (data.getApiErrorCode() == Response.ErrorCode.TOKEN_EXPIRED.getCode()) {
                        user = new LoggedInUser(true,
                                    TPTAuthValidationData.AuthProviderGoogle);
                    }
                }
            }
            if (bearerToken != null && bearerToken.length() != 0) {
                user = new LoggedInUser(tptAuthValidationData.getUserID(),
                                 tptAuthValidationData.getUserFirstname() +
                                         tptAuthValidationData.getUserLastname(), TPTAuthValidationData.AuthProviderGoogle);
                user.setBearerToken(bearerToken);
            }
        } catch (ApplicationException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "error validating token - " +
                    e.getMessage());
        } finally {
            return user;
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}