/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.windup.Application;
import com.example.windup.R;
import com.example.windup.apiaccess.model.TodoItem;
import com.example.windup.ui.ActivityConstants;
import com.example.windup.ui.login.LoggedInUserView;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.CollectionUtils;

public class TodoListActivity extends AppCompatActivity {
    private TodoItemViewModel todoItemViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        RecyclerView recyclerView = findViewById(R.id.todolistView);
        //Most important line!!!! WTF
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        final TodoListAdapter listAdapter = new TodoListAdapter();

        todoItemViewModel = ViewModelProviders.of(this, new TodoItemViewModelFactory())
                                .get(TodoItemViewModel.class);
        //recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(listAdapter);
        //todoItemViewModel.pagedListLiveData().observe(this, listAdapter::submitList);
        todoItemViewModel.pagedListLiveData().observe(this, todoItems -> {
            listAdapter.submitList(null);
            listAdapter.submitList(todoItems);
        });
        ImageButton addButton = findViewById(R.id.addItem);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent().setClass(this, TodoListAddActivity.class));
        });
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
                        .setPositiveButton(R.string.yesButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(ActivityConstants.ACTIVITY_TODOLIST_RESULT, null);
                                TodoListActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.noButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing
                            }
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
                .setPositiveButton(R.string.yesButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoListActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.noButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                }).create().show();
    }
}
