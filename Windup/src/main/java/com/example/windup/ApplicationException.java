/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup;

public class ApplicationException extends Exception{
    private Exception exception;
    private static final int ERROR_CODE_NOT_SET = -1;
    int errorCode;
    public ApplicationException (Exception exception) {
        this.exception = exception;
        errorCode = ERROR_CODE_NOT_SET;
    }
    public ApplicationException(int errorCode) {
        this.errorCode = errorCode;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (exception != null) {
            stringBuilder.append(exception);
        }
        if (errorCode != ERROR_CODE_NOT_SET) {
            stringBuilder.append("Error Code: " + errorCode);
        }
        return stringBuilder.toString();
    }
}
