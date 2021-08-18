/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    public Login(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
    @SerializedName("id")
    public String loginId;

    @SerializedName("pass")
    public String password;
}
