/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup;

import com.example.windup.apiaccess.TPTAuthValidationData;

public interface ApplicationConstants {
    String LOG_TAG = "Windup";
    String LOG_TAG_ERROR = LOG_TAG + "-error";
    String LOG_TAG_INFO = LOG_TAG + "-info";
    String LOG_TAG_FATAL = LOG_TAG + "-fatal";

    String AuthProviderGoogle = TPTAuthValidationData.AuthProviderGoogle;
}
