/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/*
 * The idea of this class is to implement some common methods
 * while the actual specific code would be present in the implementations.
 *
 * Each API call has 3 parts to it,
 * 1 Pre-Request (Set the request method type and any additional headers here).
 * 2 The API Request itself, this is mostly
 */
public abstract class APIAccess {
    protected HashMap<String, String> apiHeaders;
    protected String endpoint;
    /*
     * This is a value / return argument.
     * Set it prior to request to send bearer token
     * and set it with refresh_token parameter to refresh
     * this token with a new value.
     */
    protected String bearerToken;
    protected Map<String, List<String>> responseHeaders;
    public static final String HeaderFieldAuthorization = "Authorization";
    public static final String HeaderFieldAuthorizationType = "X-Resource-Auth";
    public static final String HeaderFieldContentType = "Content-Type";
    protected boolean isSuccess;
    protected byte[] postData;
    protected byte[] responseData;
    protected String authorizationType;

    public APIAccess(String endpoint) {
        this.endpoint = endpoint;
    }

    protected void doAPIRequest(HttpsURLConnection httpsURLConnection) {
        if (authorizationType != null) {
            httpsURLConnection.setRequestProperty(HeaderFieldAuthorizationType, authorizationType);
        }
        if (bearerToken != null) {
            httpsURLConnection.setRequestProperty(HeaderFieldAuthorization, "Bearer " + bearerToken);
        }
        /*
         * REST APIs only support application/json for all request  Methods.
         */
        httpsURLConnection.setRequestProperty(HeaderFieldContentType, "application/json");
        switch (httpsURLConnection.getRequestMethod()) {
            case APIAccessConstants.MethodPOST:
            case APIAccessConstants.MethodPUT:
                if (postData != null) {
                    try {
                        httpsURLConnection.getOutputStream().write(postData);
                    } catch (IOException e) {
                        Log.d(ApplicationConstants.LOG_TAG_INFO, "error executing api :" +
                                this.getClass().getName()
                                +e.getMessage());
                    }
                }
                break;
            case APIAccessConstants.MethodGET:
                break;
        }
    }
    /*
     * If needed then provided by the implementor.
     * Set method type and any headers here.
     */
    protected void doPreAPIRequest(HttpsURLConnection httpsURLConnection) {
    }
    /*
     * If needed then provided by the implementor. This should work
     * though most of the time.
     */
    protected void doPostAPIRequest(HttpsURLConnection httpsURLConnection) {
        try {
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            StringBuilder responseLines = new StringBuilder();

            do {
                line = reader.readLine();
                if (line !=null)
                    responseLines.append(line);
            }while (line != null);
            responseData = responseLines.toString().getBytes();
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG_INFO, "error getting response from server : " +
                    e.getMessage());
            responseData = null;
        }
    }

    /*
     * This is what needs to be called for executing the API.
     */
    public void execute() throws ApplicationException {
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) new URL(endpoint).openConnection();
            if (apiHeaders != null) {
                for (Map.Entry<String, String> entry :apiHeaders.entrySet()) {
                    httpsURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            Log.d(ApplicationConstants.LOG_TAG_INFO, "Calling api - " + this.getClass().getName());
            doPreAPIRequest(httpsURLConnection);
            doAPIRequest(httpsURLConnection);
            responseHeaders = httpsURLConnection.getHeaderFields();
            /*
             * Try to store bearer token if possible.
             */
            if (responseHeaders.containsKey(HeaderFieldAuthorization)) {
                //The server always sends only one value for this header.
                bearerToken = responseHeaders.get(HeaderFieldAuthorization).get(0);
                /*
                 * The header is something like below,
                 * Authorization:Bearer XXXXXYYYYYYZZZZZZ
                 */
                bearerToken = bearerToken.split(" ")[1];
            } else {
                bearerToken = "";
            }

            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                isSuccess = true;
            else {
                Log.d(ApplicationConstants.LOG_TAG_ERROR, "api error in " + this.getClass().getName()+ "" +
                        "- response code is " + httpsURLConnection.getResponseCode());
            }
            doPostAPIRequest(httpsURLConnection);
            httpsURLConnection.disconnect();
            Log.d(ApplicationConstants.LOG_TAG_INFO, "Exiting api - " + this.getClass().getName());
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Error executing api " +
                    this.getClass().getName()+   " - " + e.getMessage());
            throw new ApplicationException(e);
        } catch (Exception e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Error executing api " +
                    this.getClass().getName()+   " - " + e.getMessage());
            throw new ApplicationException(e);
        }
    }

    protected String getHeaderValue(String headerName) {
        if (responseHeaders != null && responseHeaders.containsKey(headerName)) {
            return responseHeaders.get(headerName).get(0);
        }
        return null; /* Not present at all.*/
    }

    protected List<String> getHeaderValueRaw(String headerName) {
        if (responseHeaders != null && responseHeaders.containsKey(headerName)) {
            return responseHeaders.get(headerName);
        }
        return null;
    }
    public String getBearerToken() {
        return bearerToken;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
