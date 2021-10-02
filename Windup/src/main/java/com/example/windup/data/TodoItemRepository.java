/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import com.example.windup.apiaccess.model.TodoItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TodoItemRepository {
    private static volatile TodoItemRepository instance;
    private TodoItemNetworkDataSource dataSource;
    private TodoItemKeyedDataSource positionalDataSource;
    private MutableLiveData<Result> repositoryLiveData;
    private DataSourceType type;
    public enum DataSourceType {
        NETWORK,
        POSITIONAL
    }

    private TodoItemRepository(DataSourceType type) {
        this.dataSource = new TodoItemNetworkDataSource();
        this.repositoryLiveData = new MutableLiveData<>();
        this.type = type;

        switch (type) {
            case NETWORK:
                break;
            case POSITIONAL:
                positionalDataSource = new TodoItemKeyedDataSource(dataSource);
                break;
            default:
                break;
        }
    }

    public DataSourceType getType() {
        return  type;
    }
    public TodoItemKeyedDataSource getPositionalDataSource() {
        return positionalDataSource;
    }
    public static TodoItemRepository getInstance(DataSourceType type) {
        if (instance == null)
            instance = new TodoItemRepository(type);
        return instance;
    }
    public LiveData<Result> getRepositoryLiveData() {
        return repositoryLiveData;
    }
    //This is a GET operation
    public void refreshTodoItems(final int offset, final int count,
                                 final boolean shared, final Map<String, String>headers) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result result = dataSource.getTodoItems(offset, count, shared, headers);
                repositoryLiveData.postValue(result);
            }
        });
    }
    public void getTodoItem(final TodoItem item, final Map<String, String> headers) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Result result = dataSource.getTodoItem(item, headers);
            repositoryLiveData.postValue(result);
        });
    }
    public void updateTodoItem(final TodoItem item,  final Map<String, String>headers) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Result result = dataSource.updateTodoItem(item, headers);
            repositoryLiveData.postValue(result);
        });
    }
    public void deleteTodoItem(final TodoItem item, final Map<String, String>headers) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Result result = dataSource.removeTodoItem(item, headers);
            repositoryLiveData.postValue(result);
        });
    }
    public void addTodoItem(final TodoItem item, final Map<String, String> headers) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Result result = dataSource.addTodoItem(item, headers);
            repositoryLiveData.postValue(result);
        });
    }
}
