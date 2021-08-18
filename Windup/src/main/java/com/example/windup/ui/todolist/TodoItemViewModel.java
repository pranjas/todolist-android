/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.windup.Application;
import com.example.windup.apiaccess.APIAccess;
import com.example.windup.data.Result;
import com.example.windup.data.TodoItemDataSourceFactory;
import com.example.windup.data.TodoItemRepository;
import com.example.windup.data.model.LoggedInUser;
import com.example.windup.apiaccess.model.TodoItem;

import java.util.HashMap;
import java.util.Map;

public class TodoItemViewModel extends ViewModel {
    private TodoItemRepository repository;
    private LiveData<Result> resultLiveData;
    private LiveData<PagedList<TodoItem>> todoItemListData;
    private DataSource<Integer, TodoItem> dataSource;
    private DataSource.Factory<Integer, TodoItem> todoItemDataFactory;


    public TodoItemViewModel(TodoItemRepository repository) {
        this.repository = repository;
        //Create a 1:1 mapping.
        resultLiveData = Transformations.map(repository.getRepositoryLiveData(), new Function<Result, Result>() {
            @Override
            public Result apply(Result input) {
                return input;
            }
        });
        LoggedInUser currentUser = Application.getLoggedInUser().getLoggedInUser();
        HashMap<String, String> apiHeaders = new HashMap<>();
        apiHeaders.put(APIAccess.HeaderFieldAuthorizationType, currentUser.getAuthProvider());

        todoItemDataFactory = new TodoItemDataSourceFactory(apiHeaders);
        dataSource = todoItemDataFactory.create();
        todoItemListData = new LivePagedListBuilder<>(todoItemDataFactory, 20)
                .build();
    }
    public void invalidateDataSource() {
        dataSource.invalidate();
    }
    public LiveData<Result> getTodoItemAPIResult() {
        return resultLiveData;
    }
    public LiveData<PagedList<TodoItem>> pagedListLiveData() {
        return todoItemListData;
    }
    private Map<String ,String > doSetupRequestAuthHeaders(LoggedInUser user) {
        HashMap<String, String> headers = new HashMap<>();
        if (user != null) {
            headers.put(APIAccess.HeaderFieldAuthorizationType, user.getAuthProvider());
        }
        return headers;
    }
    public void addTodoItem(TodoItem item, LoggedInUser user) {
        repository.addTodoItem(item, doSetupRequestAuthHeaders(user));
    }
    public void modifyTodoItem(TodoItem item, LoggedInUser user) {
        repository.addTodoItem(item, doSetupRequestAuthHeaders(user));
    }
    public void removeTodoItem(TodoItem item, LoggedInUser user) {
        repository.deleteTodoItem(item, doSetupRequestAuthHeaders(user));
    }
    public void getTodoItems(int offset, int count, boolean shared, LoggedInUser user) {
        repository.refreshTodoItems(offset, count, shared, doSetupRequestAuthHeaders(user));
    }
    public void getTodoItem(TodoItem item, LoggedInUser user) {
        repository.getTodoItem(item, doSetupRequestAuthHeaders(user));
    }
}
