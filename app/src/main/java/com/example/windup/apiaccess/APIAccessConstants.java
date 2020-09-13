/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

public interface APIAccessConstants {
    String MethodGET = "GET";
    String MethodPOST = "POST";
    String MethodPUT = "PUT";
    String BaseURL = "https://sheltered-badlands-62293.herokuapp.com";
    String LoginAPIEndPoint = BaseURL + "/login";
    String TPTEndPoint = BaseURL + "/tptverify";
}
