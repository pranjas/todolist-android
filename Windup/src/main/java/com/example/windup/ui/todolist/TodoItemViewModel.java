/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.windup.Application;
import com.example.windup.ApplicationConstants;
import com.example.windup.apiaccess.APIAccess;
import com.example.windup.data.Result;
import com.example.windup.data.TodoItemDataSourceFactory;
import com.example.windup.data.TodoItemKey;
import com.example.windup.data.TodoItemKeyedDataSource;
import com.example.windup.data.TodoItemRepository;
import com.example.windup.data.model.LoggedInUser;
import com.example.windup.apiaccess.model.TodoItem;

import java.util.HashMap;
import java.util.Map;

public class TodoItemViewModel extends ViewModel {
    private TodoItemRepository repository;
    private LiveData<Result> resultLiveData;
    private LiveData<PagedList<TodoItem>> todoItemListData;
    private LiveData<DataSource<Integer, TodoItem>> todoItemDataSourceLiveData;
    private DataSource.Factory<TodoItemKey, TodoItem> todoItemDataFactory;
    private final String REQUEST_ID = Integer.toHexString(this.hashCode());
    public static final String REQUEST_ID_HEADER = "Request-id";
    private final int PAGE_SIZE = 5;

    public TodoItemViewModel(TodoItemRepository repository) {
        this.repository = repository;
        //Create a 1:1 mapping.
        /*
         * Since the underlying repositoryLiveData
         * which is posting the values of the API calls is same
         * for all such resultLiveData we'll need to add an
         * additional identifier to figure out if this
         * repositoryLiveData posted is actually meant for
         * this instance of resultLiveData.
         * Thus we just add a unique identifier, a timestamp
         * will do here or this resultLiveData object itself.
         */
        resultLiveData = Transformations.map(repository.getRepositoryLiveData(), input -> {
           if (input.getRequestID().equals(REQUEST_ID))
               return input;
           return null;
        });
        LoggedInUser currentUser = Application.getLoggedInUser().getLoggedInUser();
        HashMap<String, String> apiHeaders = new HashMap<>();
        apiHeaders.put(APIAccess.HeaderFieldAuthorizationType, currentUser.getAuthProvider());
        TodoItemDataSourceFactory todoItemDataSourceFactory =
                    new TodoItemDataSourceFactory(apiHeaders);
        todoItemDataSourceLiveData = Transformations.map(
                todoItemDataSourceFactory.getCurrentDataSourceObservable(),
                (Function<DataSource<TodoItemKey, TodoItem>, DataSource<TodoItemKey, TodoItem>>)
                        (input)-> {
                    if (input instanceof TodoItemKeyedDataSource) {
                        TodoItemKeyedDataSource dataSource = (TodoItemKeyedDataSource) input;
                        dataSource.setPageSize(PAGE_SIZE);
                    }
                    return input;
        });
        todoItemDataFactory = todoItemDataSourceFactory;
        todoItemDataFactory.create();

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(true)
                .setPrefetchDistance(2*PAGE_SIZE)
                .build();
        todoItemListData = new LivePagedListBuilder<>(todoItemDataFactory, config)
                .setBoundaryCallback(new PagedList.BoundaryCallback<TodoItem>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                    }

                    @Override
                    public void onItemAtFrontLoaded(@NonNull TodoItem itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull TodoItem itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                        PagedList<TodoItem> pagedList = todoItemListData.getValue();
                        if (pagedList != null) {
                            pagedList.loadAround(pagedList.getPositionOffset() +
                                    pagedList.getLoadedCount() - 1);
                        }
                        Log.d(ApplicationConstants.LOG_TAG, "Reached end");
                    }
                })
                .build();
    }
    public LiveData<DataSource<Integer, TodoItem>> getTodoItemDataSourceLiveData(){
        return todoItemDataSourceLiveData;
    }
    public LiveData<Result> getTodoItemAPIResult() {
        return resultLiveData;
    }
    public LiveData<PagedList<TodoItem>> pagedListLiveData() {
        return todoItemListData;
    }
    private Map<String ,String > doSetupRequestAuthHeaders(LoggedInUser user) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(REQUEST_ID_HEADER, REQUEST_ID);
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
