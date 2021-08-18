/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model;

import android.os.Build;

public class TPTValidation {

    public String  client,
            os,
            board,
            manufacturer,
            model;
    public TPTValidation() {
        client = "Windup-App";
        os = Build.VERSION.CODENAME;
        board = Build.BOARD;
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
    }
}
