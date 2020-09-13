/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import android.util.JsonReader;

import androidx.annotation.NonNull;

public abstract class TPTAuthValidationData {
    public abstract String getUserFirstname();
    public abstract String getUserLastname();
    public abstract String getUserID();
    public abstract String getAuthenticator();
    public abstract int parseResponse();

    //Add more auth providers here as we integrate more
    //viz facebook / twitter etc..
    public static final String AuthProviderGoogle = "google";

    public static TPTAuthValidationData build(@NonNull String authProvider, byte[] responseData) {
        switch (authProvider) {
            case AuthProviderGoogle:
                return new GoogleAuthValidationData(responseData);
            default:
                return null;
        }
    }
}
