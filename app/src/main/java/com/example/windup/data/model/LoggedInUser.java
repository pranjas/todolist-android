/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data.model;

import androidx.annotation.NonNull;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String bearerToken;
    private String authProvider;
    private boolean isExpired;

    public LoggedInUser(@NonNull String userId, @NonNull String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }
    public LoggedInUser(@NonNull String userId, @NonNull String displayName,
                        @NonNull String authProvider) {
        this(userId, displayName);
        this.authProvider = authProvider;
    }
    public LoggedInUser(boolean isExpired, String authProvider) {
        this.isExpired = isExpired;
        this.authProvider = authProvider;
    }
    public void setBearerToken(@NonNull String bearerToken) {
        this.bearerToken = bearerToken;
    }
    public String getUserId() {
        return userId;
    }
    public String getDisplayName() {
        return displayName;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public boolean isExpired() {
        return isExpired;
    }
}