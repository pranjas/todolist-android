/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Tag;

public final class WindupAPIServiceImpl {
    private Retrofit retrofit;
    private WindupAPIServiceImpl instance;
    private WindupAPIService service = null;
    private WindupAPIServiceImpl() {
    }
    public static WindupAPIServiceImpl getInstance() {
        return getInstance(null, null, null, null);
    }
    public static WindupAPIServiceImpl getInstance( Type type, JsonDeserializer deserializer) {
        return getInstance(null, null, type, deserializer);
    }
    //use a custom okHttp client along with a custom data convertor factory
    // if required.
    public static WindupAPIServiceImpl getInstance(OkHttpClient customClient,
                                                   Converter.Factory factory,
                                                   Type type,
                                                   JsonDeserializer deserializer) {
        WindupAPIServiceImpl instance = new WindupAPIServiceImpl();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(APIAccessConstants.BaseURL);
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setPrettyPrinting();
        if (deserializer != null && type != null)
            gsonBuilder.registerTypeAdapter(type, deserializer);
        Gson gson = gsonBuilder.create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new WindupAPIHeaderInterceptor())
                .addInterceptor(new HttpLoggingInterceptor(s ->
                        Log.d(ApplicationConstants.LOG_TAG, s))
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        if (customClient == null)
            builder.client(okHttpClient);
        else
            builder.client(customClient);

        if (factory == null)
            builder.addConverterFactory(GsonConverterFactory.create(gson));
        else
            builder.addConverterFactory(factory);
        instance.retrofit = builder.build();
        instance.service = instance.retrofit.create(WindupAPIService.class);
        return  instance;
    }
    public WindupAPIService getService() {
        return service;
    }
}
