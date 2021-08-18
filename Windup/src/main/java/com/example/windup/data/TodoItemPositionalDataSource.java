/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.example.windup.apiaccess.model.TodoItem;
import com.example.windup.apiaccess.model.response.Response;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodoItemPositionalDataSource extends PositionalDataSource<TodoItem> {
    private TodoItemNetworkDataSource dataSource;
    private Map<String , String >apiHeaders;

    public TodoItemPositionalDataSource(@NonNull TodoItemNetworkDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setApiHeaders(Map<String, String> apiHeaders) {
        this.apiHeaders = apiHeaders;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<TodoItem> callback) {
        int offset = params.requestedStartPosition / params.pageSize;
        int toSkip = params.requestedStartPosition % params.pageSize;
        int count = params.pageSize;
        Result result = dataSource.getTodoItems(offset, count, true, this.apiHeaders);
        if (result != null) {
            if (result instanceof Result.Success) {
                Response response = (Response)
                                        ((Result.Success) result).getData();
                if (((Double)response.extras.get("count")).intValue() == 0) {
                    return;
                }
                ArrayList<TodoItem> itemList = (ArrayList<TodoItem>) response.extras.get("items");
                if (itemList.size() > 0) {
                    List<TodoItem> finalList = itemList.subList(toSkip,
                            itemList.size());
                    callback.onResult(finalList, params.requestedStartPosition, finalList.size());
                }
            }
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<TodoItem> callback) {
        int offset = params.startPosition / params.loadSize;
        int toSkip = params.startPosition % params.loadSize;
        int count = params.loadSize;
        Result result = dataSource.getTodoItems(offset, count, true, this.apiHeaders);
        if (result != null) {
            if (result instanceof Result.Success) {
                Response response = (Response)((Result.Success) result).getData();
                if ((int)response.extras.get("count") == 0) {
                    return;
                }
                ArrayList<TodoItem> itemList = (ArrayList<TodoItem>) response.extras.get("items");
                if (itemList.size() > 0) {
                    List<TodoItem> finalList = itemList.subList(toSkip,
                            itemList.size());
                    callback.onResult(finalList);
                }
            }
        }
    }
}
