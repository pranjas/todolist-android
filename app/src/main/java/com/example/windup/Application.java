/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup;

import androidx.annotation.Nullable;

import com.example.windup.data.model.LoggedInUser;
import com.example.windup.ui.login.LoggedInUserView;

public class Application {
    private static LoggedInUserView loggedInUser;

    public static void setLoggedInUserView(LoggedInUserView userView) {
        loggedInUser = userView;
    }

    @Nullable
    public static LoggedInUserView getLoggedInUser() {
        return loggedInUser;
    }
}
