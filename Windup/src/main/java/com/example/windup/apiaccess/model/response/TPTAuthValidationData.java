/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model.response;

import androidx.annotation.NonNull;

public abstract class TPTAuthValidationData {
    public abstract String getUserFirstname();
    public abstract String getUserLastname();
    public abstract String getUserID();
    public abstract String getAuthenticator();
    //Add more auth providers here as we integrate more
    //viz facebook / twitter etc..
    public static final String AuthProviderGoogle = "google";
    public enum AuthProvider {
        GOOGLE
        ;
        public String toName() {
            switch (this) {
                case GOOGLE:
                    return GOOGLE.name().toLowerCase();
                default:
                    return "";
            }
        }
    }

    public static TPTAuthValidationData build(AuthProvider authProvider, Response response) {
        switch (authProvider) {
            case GOOGLE:
                return new GoogleAuthValidationData(response);
            default:
                return null;
        }
    }
}
