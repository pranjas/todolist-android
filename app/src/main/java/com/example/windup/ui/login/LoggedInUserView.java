/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.login;

import com.example.windup.data.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private LoggedInUser loggedInUser;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String token() {
        String bearerToken = loggedInUser.getBearerToken();

        if (bearerToken != null)
            return bearerToken;
        return "";
    }

    public void setBearerToken(String bearerToken) {
        loggedInUser.setBearerToken(bearerToken);
    }

    public String getDisplayName() {
        return loggedInUser.getDisplayName();
    }

    public boolean isExpired() {
        return loggedInUser.isExpired();
    }
    public String getProvider() {
        return loggedInUser.getAuthProvider();
    }
}