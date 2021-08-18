/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

public interface APIAccessConstants {
    String MethodGET = "GET";
    String MethodPOST = "POST";
    String MethodPUT = "PUT";
    String Bearer = "Bearer ";
    String ContentType = "application/json";
    String BaseURL = "https://sheltered-badlands-62293.herokuapp.com";
    String LoginAPIEndPoint = BaseURL + "/login";
    String RegisterAPIEndPoint = BaseURL + "/register";
    String UserAPIEndPoint = BaseURL + "/user";
    String PostAddAPIEndPoint = BaseURL + "/post/add";
    String PostRemoveAPIEndPoint = BaseURL + "/post/remove";
    String PostEditAPIEndPoint = BaseURL + "/post/edit";
    String PostGetAPIEndPoint = BaseURL + "/post/get";
    String TPTEndPoint = BaseURL + "/tptverify";
}
