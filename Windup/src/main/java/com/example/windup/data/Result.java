/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private Result() {
    }

    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }
    public String getRequestID() {
        if (this instanceof  Result.Success) {
            Result.Success success = (Result.Success) this;
            return success.requestID == null ? "" : success.requestID;
        } else  if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return error.requestID == null ? "" : error.requestID;
        }
        return "";
    }
    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;
        private String requestID;

        public Success(T data) {
            this.data = data;
        }

        public Success(T data, String requestID) {
            this(data);
            this.requestID = requestID;
        }

        public T getData() {
            return this.data;
        }

        public void setRequestID(String requestID) {
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private Exception error;
        private String requestID;
        public Error(Exception error) {
            this.error = error;
        }
        public Error(Exception error, String requestID) {
            this(error);
            this.requestID = requestID;
        }

        public Exception getError() {
            return this.error;
        }
    }
}