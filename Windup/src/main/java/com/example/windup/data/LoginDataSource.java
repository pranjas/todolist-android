/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.apiaccess.APIAccess;
import com.example.windup.apiaccess.model.response.TPTAuthValidationData;
import com.example.windup.apiaccess.WindupAPIResponseUtils;
import com.example.windup.apiaccess.WindupAPIService;
import com.example.windup.apiaccess.WindupAPIServiceImpl;
import com.example.windup.apiaccess.model.Login;
import com.example.windup.apiaccess.model.TPTValidation;
import com.example.windup.apiaccess.model.response.Response;
import com.example.windup.data.model.LoggedInUser;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public LoggedInUser login(final String username, final String password) {
        LoggedInUser user = null;
        try {
            WindupAPIService apiService = WindupAPIServiceImpl.getInstance().getService();
            Call<Response> apiCall = apiService.login(new Login(username, password));
            String bearerToken = null;

            retrofit2.Response apiResponse = apiCall.execute();
            if (apiResponse.isSuccessful()) {
                String authorizationHeader = apiResponse.headers().get(APIAccess.HeaderFieldAuthorization);
                bearerToken = WindupAPIResponseUtils.getBearerToken(authorizationHeader);
            }
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
            WindupAPIService apiService = WindupAPIServiceImpl.getInstance().getService();
            HashMap<String, String> apiHeaders = new HashMap<>();
            Call<Response> apiCall = null;
            String bearerToken = null;
            TPTAuthValidationData tptAuthValidationData = null;

            apiHeaders.put(APIAccess.HeaderFieldAuthorizationType, TPTAuthValidationData.AuthProviderGoogle);
            apiCall = apiService.tptVerify(new TPTValidation(),
                                            WindupAPIResponseUtils.setBearerTokenHeader(googleTokenId),
                                            apiHeaders);
            retrofit2.Response<Response> apiResponse= apiCall.execute();

            if (apiResponse.isSuccessful()) {
                Response fromServer = apiResponse.body();
                String authorizationHeader = apiResponse.headers().get(APIAccess.HeaderFieldAuthorization);

                if (fromServer.errorCode == Response.ErrorCode.TOKEN_EXPIRED.getCode()) {
                    user = new LoggedInUser(true, TPTAuthValidationData.AuthProviderGoogle);
                } else {
                    bearerToken = WindupAPIResponseUtils.getBearerToken(authorizationHeader);
                    tptAuthValidationData = TPTAuthValidationData.build(
                            TPTAuthValidationData.AuthProvider.GOOGLE, fromServer);
                }
            }
            if (bearerToken != null && bearerToken.length() != 0) {
                user = new LoggedInUser(tptAuthValidationData.getUserID(),
                                 tptAuthValidationData.getUserFirstname() +
                                         tptAuthValidationData.getUserLastname(), TPTAuthValidationData.AuthProviderGoogle);
                user.setBearerToken(bearerToken);
            }
        } finally {
            return user;
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}