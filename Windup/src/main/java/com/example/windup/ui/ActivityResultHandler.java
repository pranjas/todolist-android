/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui;

import android.content.Intent;

/*
 * Use a helper class to use with multiple
 * activity results.
 */
public class ActivityResultHandler {
    private int requestCode;
    private int resultCode;
    protected Intent data;
    public static final int REQUEST_CODE_ANY = -1;
    public static final int RESULT_CODE_ANY = -1;

    public ActivityResultHandler(int requestCode, int resultCode) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
    }
    /*
     * Implementations should override this.
     */
    public void handleActivityResult() { }

    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultIntent(Intent data) {
        this.data = data;
    }
}
