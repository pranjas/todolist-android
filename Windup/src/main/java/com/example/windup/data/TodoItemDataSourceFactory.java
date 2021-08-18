/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.windup.apiaccess.model.TodoItem;

import java.util.Map;

public class TodoItemDataSourceFactory extends DataSource.Factory<Integer, TodoItem> {
    private MutableLiveData<TodoItemPositionalDataSource> sourceLiveData =
            new MutableLiveData<>();
    private Map<String, String> apiHeaders;
    public TodoItemDataSourceFactory(@NonNull Map<String, String> apiHeaders) {
        this.apiHeaders = apiHeaders;
    }
    @NonNull
    @Override
    public DataSource<Integer, TodoItem> create() {
        TodoItemPositionalDataSource dataSource = new TodoItemPositionalDataSource(
                new TodoItemNetworkDataSource()
        );
        dataSource.setApiHeaders(apiHeaders);
        sourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
