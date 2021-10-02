/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.windup.apiaccess.model.TodoItem;

import java.util.Map;

public class TodoItemDataSourceFactory extends DataSource.Factory<TodoItemKey, TodoItem> {
    private Map<String, String> apiHeaders;
    private MutableLiveData<DataSource<TodoItemKey, TodoItem>>
            currentDataSource = new MutableLiveData<>();
    public TodoItemDataSourceFactory(@NonNull Map<String, String> apiHeaders) {
        this.apiHeaders = apiHeaders;
    }
    public LiveData getCurrentDataSourceObservable() {
        return currentDataSource;
    }
    @NonNull
    @Override
    public DataSource<TodoItemKey, TodoItem> create() {
        TodoItemKeyedDataSource dataSource = new TodoItemKeyedDataSource(
                new TodoItemNetworkDataSource()
        );
        dataSource.setApiHeaders(apiHeaders);
        currentDataSource.postValue(dataSource);
        return dataSource;
    }
}
