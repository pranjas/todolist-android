/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import android.os.Build;
import android.util.JsonWriter;
import android.util.Log;

import com.example.windup.ApplicationConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.net.ssl.HttpsURLConnection;

public class TPTAuthValidationAPIAccess extends APIAccess {
    public TPTAuthValidationAPIAccess(String idToken, String authProvider) {
        super(APIAccessConstants.TPTEndPoint);
        super.bearerToken = idToken;
        super.authorizationType = authProvider;
    }
    private TPTAuthValidationData tptAuthValidationData;

    public TPTAuthValidationData getTptAuthValidationData() {
        return tptAuthValidationData;
    }

    @Override
    protected void doPreAPIRequest(HttpsURLConnection httpsURLConnection) {
        try {
            httpsURLConnection.setRequestMethod(APIAccessConstants.MethodPOST);
            httpsURLConnection.setDoInput(true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(byteArrayOutputStream, "utf-8"));
            jsonWriter.beginObject();
            /*
             * Send name of the client
             */
            jsonWriter.name("client").value("Windup-App");
            jsonWriter.name("os").value(Build.VERSION.CODENAME);
            jsonWriter.name("board").value(Build.BOARD);
            jsonWriter.name("manufacturer").value(Build.MANUFACTURER);
            jsonWriter.name("model").value(Build.MODEL);
            jsonWriter.endObject();
            jsonWriter.close();
            super.postData = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Error in pre-api" + this.getClass().getName() + "" +
                    e.getMessage());
        }
    }

    @Override
    protected void doPostAPIRequest(HttpsURLConnection httpsURLConnection) {
        super.doPostAPIRequest(httpsURLConnection);
        if (responseData !=null) {
            tptAuthValidationData = TPTAuthValidationData.
                    build(super.authorizationType, responseData);
            if (tptAuthValidationData.parseResponse() < 0) {
                Log.d(ApplicationConstants.LOG_TAG_ERROR, "Failed to parse response - " +
                        this.getClass().getName());
                /*
                 * Though the API itself might've been OK, the JSON response wasn't
                 * parsable. Thus mark this request as failed.
                 */
                super.isSuccess = false;
            }
        }
    }
}


