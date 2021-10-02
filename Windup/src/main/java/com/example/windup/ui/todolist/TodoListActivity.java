/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.windup.Application;
import com.example.windup.ApplicationConstants;
import com.example.windup.R;
import com.example.windup.apiaccess.model.TodoItem;
import com.example.windup.ui.ActivityConstants;
import com.example.windup.ui.login.LoggedInUserView;

public class TodoListActivity extends AppCompatActivity {
    private TodoItemViewModel todoItemViewModel;
    private static final int MAX_ITEMS_TO_LOAD = 20;
    private DataSource<Integer, TodoItem> currentDataSource;
    private PagedList<TodoItem> pagedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        final TodoListAdapter listAdapter = new TodoListAdapter();

        RecyclerView recyclerView = findViewById(R.id.todolistView);
        //Most important line!!!! WTF
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        todoItemViewModel = ViewModelProviders.of(this, new TodoItemViewModelFactory())
                                .get(TodoItemViewModel.class);
        recyclerView.setAdapter(listAdapter);
        todoItemViewModel.pagedListLiveData().observe(this, todoItems -> {
//            listAdapter.submitList(null);
            listAdapter.submitList(todoItems);
            pagedList = todoItems;
        });
        todoItemViewModel.getTodoItemDataSourceLiveData().observe(this, new Observer<DataSource<Integer, TodoItem>>() {
            @Override
            public void onChanged(DataSource<Integer, TodoItem> integerTodoItemDataSource) {
                TodoListActivity.this.currentDataSource = integerTodoItemDataSource;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean movingDown;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    LinearLayoutManager linearLayoutManager =
                            (LinearLayoutManager) recyclerView.getLayoutManager();
                    int offset = pagedList.getPositionOffset();
                    int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastItem = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d(ApplicationConstants.LOG_TAG, "onScrollStateChanged...");
                    if(movingDown)
                        pagedList.loadAround(lastItem + offset);
                    else {
                       pagedList.loadAround(firstItem + offset);
                    }
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                movingDown = dy > 0;
            }
        });
        ImageButton addButton = findViewById(R.id.addItem);
        addButton.setOnClickListener(v -> startActivity(new Intent().setClass(this, TodoListAddActivity.class)));
        displayLoggedInUser();
    }
    private void displayLoggedInUser() {
        LoggedInUserView loggedInUserView = Application.getLoggedInUser();

        if (loggedInUserView != null) {
            setTitle("Welcome " + loggedInUserView.getDisplayName());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Handle Up button.
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.logoutDialog)
                        .setMessage(R.string.logoutMessage)
                        .setPositiveButton(R.string.yesButton, (dialog, which) -> {
                            setResult(ActivityConstants.ACTIVITY_TODOLIST_RESULT, null);
                            TodoListActivity.super.onBackPressed();
                        })
                        .setNegativeButton(R.string.noButton, (dialog, which) -> {
                            //Do nothing
                        }).create().show();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logoutDialog)
                .setMessage(R.string.logoutMessage)
                .setPositiveButton(R.string.yesButton, (dialog, which) -> TodoListActivity.super.onBackPressed())
                .setNegativeButton(R.string.noButton, (dialog, which) -> {
                    //Do nothing
                }).create().show();
    }
}
