/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import com.example.windup.apiaccess.model.Login;
import com.example.windup.apiaccess.model.TPTValidation;
import com.example.windup.apiaccess.model.response.Response;
import com.example.windup.apiaccess.model.TodoItem;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface WindupAPIService {
    //Login
    @Headers({
            APIAccess.HeaderFieldAuthorization +": " + APIAccessConstants.Bearer +" XXX"
    })
    @POST("login")
    Call<Response> login(@Body Login login);


    //Third party token verification post login.
    //Needs the Authorization header.
    @POST("tptverify")
    Call<Response> tptVerify(@Body TPTValidation tptValidation,
                             //The bearer token must be of the format "Bearer XXXXXX"
                             @Header(APIAccess.HeaderFieldAuthorization) String bearerToken,
                             @HeaderMap Map<String, String> headers);

    //User API
    @POST("user")
    Call<Response> getUser();

    //Post addition
    @POST("post/add")
    Call<Response> postAdd(@Body TodoItem todoItem,
                           @Header(APIAccess.HeaderFieldAuthorization) String bearerToken,
                           @HeaderMap Map<String, String> headers);

    //Post removal
    @POST("post/remove")
    Call<Response> postRemove(@Body TodoItem todoItem,
                              @Header(APIAccess.HeaderFieldAuthorization) String bearerToken,
                              @HeaderMap Map<String, String> headers);

    @POST("post/edit")
    Call<Response> postEdit(@Body TodoItem todoItem,
                            @Header(APIAccess.HeaderFieldAuthorization) String bearerToken,
                            @HeaderMap Map<String, String> headers);

    @GET("post/get")
    Call<Response> postGet(@QueryMap Map<String, String> queryParams,
                                                  @Header(APIAccess.HeaderFieldAuthorization) String bearerToken,
                                                  @HeaderMap Map<String, String> headers);
}
