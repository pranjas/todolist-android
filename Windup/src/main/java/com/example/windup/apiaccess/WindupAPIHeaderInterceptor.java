/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class WindupAPIHeaderInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();
        if (!oldRequest.header(APIAccess.HeaderFieldAuthorization).startsWith("Bearer")) {
            String bearerToken = oldRequest.header(APIAccess.HeaderFieldAuthorization);
            builder.removeHeader(APIAccess.HeaderFieldAuthorization);
            builder.addHeader(APIAccess.HeaderFieldAuthorization, "Bearer " + bearerToken);
        }
        builder.addHeader(APIAccess.HeaderFieldContentType, APIAccessConstants.ContentType);
        //Proceed with the new request but with added ContentType header.
        return chain.proceed(builder.build());
    }
}
