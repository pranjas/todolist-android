/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model.response;

import android.util.JsonReader;
import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public abstract class ResponseParser {
    protected JsonReader jsonReader;
    public Response response;

    public abstract void parseArray(String currentKey);
    public abstract void parseObject(String currentKey);

    public ResponseParser(byte[] inputData) throws UnsupportedEncodingException, ApplicationException {
        if (inputData == null)
            throw new ApplicationException(new Exception("Input data is null"));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        jsonReader = new JsonReader(new InputStreamReader(byteArrayInputStream, "utf-8"));
        response = new Response();
    }

    public void parseResponse() {
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String currKey = jsonReader.nextName();
                switch (currKey) {
                    case "apicode":
                        response.errorCode = jsonReader.nextLong();
                        break;
                    case "apicode_desc":
                        response.errorCodeDescription = jsonReader.nextString();
                        break;
                    case "status":
                        response.status = jsonReader.nextInt();
                        break;
                    case "msg":
                        response.message = jsonReader.nextString();
                        break;
                    case "extra":
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            currKey = jsonReader.nextName();
                            switch (jsonReader.peek()) {
                                case STRING:
                                case NAME:
                                    response.extras.put(currKey, jsonReader.nextString());
                                    break;
                                case BOOLEAN:
                                    response.extras.put(currKey, "" + jsonReader.nextBoolean());
                                    break;
                                case BEGIN_ARRAY:
                                    parseArray(currKey);
                                    break;
                                case BEGIN_OBJECT:
                                    parseObject(currKey);
                                    break;
                                case NULL:
                                    response.extras.put(currKey, "");
                                    jsonReader.nextNull();//eat the null.
                                    break;
                                case NUMBER:
                                    response.extras.put(currKey, String.valueOf(jsonReader.nextInt()));
                                default:
                                    //nothing.
                            }
                        }
                        jsonReader.endObject();
                        break;
                    default:
                        Log.d(ApplicationConstants.LOG_TAG_ERROR, "Unexpected json property "
                                    + currKey);
                        jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG, "no more json tokens" + e.getMessage());
        }
    }
}

