/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import android.util.Log;

import com.example.windup.Application;
import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;
import com.example.windup.apiaccess.WindupAPIService;
import com.example.windup.apiaccess.WindupAPIServiceImpl;
import com.example.windup.apiaccess.WindupAPITodoItemResponseDeserializer;
import com.example.windup.apiaccess.model.response.Response;
import com.example.windup.apiaccess.model.TodoItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TodoItemNetworkDataSource {
    public TodoItemNetworkDataSource() {
    }
    private enum APIName {
        EDIT,
        ADD,
        DELETE,
        GET,
    }

    private Result doGenericAPI(TodoItem item, Map<String, String> headers, APIName apiName,
                                int offset, int count, boolean shared) {
        String requestID = headers.get("Request-id");
        try {
            WindupAPIService apiService = WindupAPIServiceImpl.getInstance(Response.class, new
                    WindupAPITodoItemResponseDeserializer()).getService();
            retrofit2.Response<Response> response = null;
            Response fromServer = null;
            switch (apiName) {
                case GET:
                    Map<String, String> queryParms = new HashMap<>();
                    queryParms.put("offset", String.valueOf(offset));
                    queryParms.put("count", String.valueOf(count));
                    queryParms.put("shared", String.valueOf(shared));
                    if (item != null)
                        queryParms.put("postid", item.getId());
                    response = apiService.postGet(queryParms,
                            Application.getLoggedInUser().getLoggedInUser().getBearerToken(), headers).execute();
                    break;
                case ADD:
                    response = apiService.postAdd(item,
                            Application.getLoggedInUser().getLoggedInUser().getBearerToken(), headers).execute();
                    break;
                case EDIT:
                    apiService.postEdit(item, Application.getLoggedInUser().getLoggedInUser().getBearerToken(),
                            headers).execute();
                    break;
                case DELETE:
                    apiService.postRemove(item, Application.getLoggedInUser().getLoggedInUser().getBearerToken(),
                            headers).execute();
                    break;
            }
            if (!response.isSuccessful()) {
                if (response.errorBody() != null) {
                    fromServer = Response.fromErrorResponse(response.errorBody().string());
                    Response.ErrorCode errorCode = Response.ErrorCode.getCode(fromServer.errorCode);
                    Log.d(ApplicationConstants.LOG_TAG, "Error executing api " + apiName + ":" +
                            errorCode.name());
                } else {
                    fromServer = new Response();
                    fromServer.errorCode = response.code();
                }
                return new Result.Error(new ApplicationException((int)fromServer.errorCode), requestID);
            } else {
                fromServer = response.body();
                switch (apiName) {
                    case GET:
                        fromServer.apiName = Response.APIName.API_GET;
                        break;
                    case DELETE:
                        fromServer.apiName = Response.APIName.API_REMOVE;
                        break;
                    case ADD:
                        fromServer.apiName = Response.APIName.API_ADD;
                        break;
                    case EDIT:
                        fromServer.apiName = Response.APIName.API_EDIT;
                        break;
                }
                return new Result.Success<Response>(fromServer, requestID);
            }
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG, "Error executing edit api: " + e.getMessage());
            return new Result.Error(new ApplicationException(e), requestID);
        }
    }

    public Result updateTodoItem(TodoItem item, Map<String, String>headers) {

        return doGenericAPI(item, headers, APIName.EDIT, -1, -1, false);
    }
    public Result removeTodoItem(TodoItem todoItem, Map<String, String > headers) {
        return doGenericAPI(todoItem, headers, APIName.DELETE, -1, -1, false);
/*        return doAPIOperation(todoItem, headers, TodoItemAPIAccess.TodoItemAPIEndpoint.REMOVE,
                -1, -1, false);*/
    }

    public Result addTodoItem(TodoItem item, Map<String , String> headers) {
        return doGenericAPI(item, headers, APIName.ADD, -1, -1, false);
    }
    public Result getTodoItems(int offset, int count, boolean shared, Map<String, String> headers) {
        return doGenericAPI(null, headers, APIName.GET, offset, count, shared);
    }
    public Result getTodoItem(TodoItem item, Map<String, String> headers) {
        if (item != null)
            return doGenericAPI(item, headers, APIName.GET, -1, -1, false);
        return new Result.Error(new Exception("Item can't be null"));
    }
}
