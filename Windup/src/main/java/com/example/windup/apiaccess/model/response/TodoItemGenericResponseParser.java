/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model.response;


import com.example.windup.ApplicationException;

import java.io.UnsupportedEncodingException;

public class TodoItemGenericResponseParser extends ResponseParser {
    public TodoItemGenericResponseParser(byte[] inputData) throws UnsupportedEncodingException, ApplicationException {
        super(inputData);
    }

    /*
     * Nothing special is required for generic TodoItem response.
     * Everything is embedded in the response field of the base class.
     */
    @Override
    public void parseArray(String currentKey) {

    }

    @Override
    public void parseObject(String currentKey) {

    }
}
