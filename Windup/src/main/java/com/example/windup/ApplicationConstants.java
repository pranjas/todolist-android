/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup;

import com.example.windup.apiaccess.model.response.TPTAuthValidationData;

public interface ApplicationConstants {
    String LOG_TAG = "Windup";
    String LOG_TAG_ERROR = LOG_TAG + "-error";
    String LOG_TAG_INFO = LOG_TAG + "-info";
    String LOG_TAG_FATAL = LOG_TAG + "-fatal";
    String EMPTY_STRING = "";

    String AuthProviderGoogle = TPTAuthValidationData.AuthProviderGoogle;
}
