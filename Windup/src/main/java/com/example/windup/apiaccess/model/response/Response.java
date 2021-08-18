/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model.response;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/*
 * Use default ExtraClass as Object
 */
public class Response {
    /*
     * This is the response
     */
    @SerializedName("extra")
    public HashMap<String , Object> extras;

    @SerializedName("status")
    public int status;

    @SerializedName("msg")
    public String message;

    @SerializedName("apicode")
    public long errorCode;

    @SerializedName("apicode_desc")
    public String errorCodeDescription;

    public int getStatus() {
        return status;
    }

    //This needs to be set by the caller of API.
    public transient String apiName;

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
        NOT_IMPLEMENTED(GENERIC_BASE.getCode() + 7),
        UNKNOWN(GENERIC_BASE.getCode() + 8)
        ;
        public final long code;
        public long getCode() {
            return code;
        }
        public static ErrorCode getCode(long code) {
            for (ErrorCode ec: ErrorCode.values()
                 ) {
                if (ec.code == code)
                    return ec;
            }
            return ErrorCode.UNKNOWN;
        }
        private ErrorCode(long code) {
            this.code = code;
        }
    }
    public interface APIName {
        String API_GET = "GET";
        String API_ADD = "ADD";
        String API_REMOVE = "REMOVE";
        String API_EDIT = "EDIT";
    }

    public static Response fromErrorResponse(String jsonBody) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setPrettyPrinting()
                .create();
        return gson.fromJson(jsonBody, Response.class);
    }
}
