/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

public class WindupAPIResponseUtils{
    public static String getBearerToken(String authorizationHeaderValue) {
        String[] split = authorizationHeaderValue.split(" ");
        if (split.length != 2 || !split[0].equals("Bearer")) {
            return "";
        }
        return split[1];
    }

    public static String setBearerTokenHeader(String rawBearerToken) {
        return "Bearer " + rawBearerToken;
    }
}
