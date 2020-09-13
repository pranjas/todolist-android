/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.response;

import java.util.HashMap;

public class Response {
    /*
     * This is the response
     */
    public HashMap<String , String> extras;
    public int status;
    public String message;
    public long errorCode;
    public String errorCodeDescription;

    public int getStatus() {
        return status;
    }

    public Response() {
        status = -1;
        message = "";
        errorCode = 0;
        errorCodeDescription = "";
        extras = new HashMap<>();
    }

    public enum ErrorCode {
        OK(0),
        GENERIC_BASE(0xebadf),
        GENERIC_ERROR(GENERIC_BASE.getCode()),
        TOKEN_EXPIRED(GENERIC_BASE.getCode() + 1),
        INVALID_INPUT(GENERIC_BASE.getCode() + 2),
        INVALID_VERSION(GENERIC_BASE.getCode() + 3),
        UNKNOWN_AUTH_PROVIDER(GENERIC_BASE.getCode() + 4),
        INVALID_PUBLIC_CERT(GENERIC_BASE.getCode() + 5),
        INVALID_RSA_KEY(GENERIC_BASE.getCode() + 6),
        NOT_IMPLEMENTED(GENERIC_BASE.getCode() + 7)
        ;
        public final long code;
        public long getCode() {
            return code;
        }
        private ErrorCode(long code) {
            this.code = code;
        }
    }
}
