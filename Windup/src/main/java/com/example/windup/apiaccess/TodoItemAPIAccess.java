/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;
import com.example.windup.apiaccess.model.response.ResponseParser;
import com.example.windup.apiaccess.model.response.TodoItemGenericResponseParser;
import com.example.windup.apiaccess.model.response.TodoItemGetResponseParser;

import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class TodoItemAPIAccess extends APIAccess{

    private TodoItemAPIEndpoint apiEndpoint;
    private ResponseParser responseParser;

    public enum TodoItemAPIEndpoint {
        ADD,
        REMOVE,
        MODIFY,
        GET;
        public String getAPIEndpoint() {
            switch (this) {
                case ADD:
                    return APIAccessConstants.PostAddAPIEndPoint;
                case MODIFY:
                    return APIAccessConstants.PostEditAPIEndPoint;
                case REMOVE:
                    return APIAccessConstants.PostRemoveAPIEndPoint;
                case GET:
                    return APIAccessConstants.PostGetAPIEndPoint;
                default:
                    return "";
            }
        }
    }

    public TodoItemAPIAccess (Map<String, String > headers, TodoItemAPIEndpoint apiEndpoint) {
        super(apiEndpoint.getAPIEndpoint());
        this.apiEndpoint = apiEndpoint;
        super.apiHeaders = headers;
        //Query Parameters for TodoItems are only supported in the GET request.
        if (apiEndpoint == TodoItemAPIEndpoint.GET) {
            queryParams = new HashMap<>();
        }
    }
    /*
     * Only if this instance is being used for the GET request, the parameters
     * would be used otherwise silently ignored.
     */
    public void setQueryParams(Map<String, String > params) {
        if (queryParams != null) {
            //Don't use the passed in params but it's copy.
            queryParams.putAll(params);
        }
    }

    @Override
    protected void doPreAPIRequest(HttpsURLConnection httpsURLConnection) {
        super.doPreAPIRequest(httpsURLConnection);
        try {
            switch (apiEndpoint) {
                case ADD:
                case REMOVE:
                case MODIFY:
                    httpsURLConnection.setRequestMethod(APIAccessConstants.MethodPOST);
                    break;
                case GET:
                    httpsURLConnection.setRequestMethod(APIAccessConstants.MethodGET);
            }
        } catch (ProtocolException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR , "Error in PreAPI request" +
                    this.getClass().getName() + "-: " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void doPostAPIRequest(HttpsURLConnection httpsURLConnection) {
        //The super method gets us the responseData.
        super.doPostAPIRequest(httpsURLConnection);
        try {
            if (responseData != null) {
                switch (apiEndpoint) {
                    case GET:
                        responseParser = new TodoItemGetResponseParser(responseData);
                        break;
                    case ADD:
                    case MODIFY:
                    case REMOVE:
                        responseParser = new TodoItemGenericResponseParser(responseData);
                        break;
                }
                responseParser.parseResponse();
            }
        } catch (UnsupportedEncodingException | ApplicationException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Execution of post api with method " +
                    this.apiEndpoint.getAPIEndpoint()+ " failed: " + e.getLocalizedMessage());
            responseParser = null;
        }
    }

    public ResponseParser getResponseParser() {
        return responseParser;
    }
    public void setRequestData(byte[] requestData) {
        postData = requestData;
    }
}
