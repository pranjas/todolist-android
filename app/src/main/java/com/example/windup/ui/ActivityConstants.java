/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui;

import android.app.Activity;

public interface ActivityConstants {
    int ACTIVITY_BASE_RESULT_CODE = Activity.RESULT_FIRST_USER;
    int ACTIVITY_BASE_REQUEST_CODE = 0;
    int ACTIVITY_GOOGLE_SIGNIN_REQUEST = ACTIVITY_BASE_REQUEST_CODE + 1;
    int ACTIVITY_GOOGLE_SIGNIN_RESULT = ACTIVITY_BASE_RESULT_CODE + 1;
    int ACTIVITY_TODOLIST_RESULT = ACTIVITY_GOOGLE_SIGNIN_RESULT + 1;
    int ACTIVITY_TODOLIST_REQEUST = ACTIVITY_TODOLIST_RESULT + 1;
}
