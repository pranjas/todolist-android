/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import android.util.JsonWriter;
import android.util.Log;

import com.example.windup.ApplicationConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginAPIAccess extends APIAccess{
    private static String LoginAPIEndpoint = APIAccessConstants.LoginAPIEndPoint;
    private byte[] responseData;

    public LoginAPIAccess(String username, String password) {
        super(LoginAPIEndpoint);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter
                                                    (byteArrayOutputStream, "utf-8"));
            jsonWriter.beginObject();
            jsonWriter.name("id").value(username);
            jsonWriter.name("pass").value(password);
            jsonWriter.endObject();
            jsonWriter.close();
            super.postData = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "invalid json " + e.getMessage());
        }
    }

    @Override
    protected void doPreAPIRequest(HttpsURLConnection httpsURLConnection) {
        /*
         * Set the HTTP Method to post.
         * and mark the channel as doing input from our side.
         */
        try {
            httpsURLConnection.setRequestMethod(APIAccessConstants.MethodPOST);
            httpsURLConnection.setDoInput(true);
        } catch (ProtocolException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Error setting HTTP request method " +
                    e.getMessage());
        }
    }
    public byte[] getResponseData() {
        return responseData;
    }
}
