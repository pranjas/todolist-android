/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.windup.Application;
import com.example.windup.ApplicationConstants;
import com.example.windup.R;
import com.example.windup.apiaccess.model.TodoItem;
import com.example.windup.apiaccess.model.response.Response;
import com.example.windup.data.Result;

import java.util.Calendar;
import java.util.GregorianCalendar;

/*
 * This activity handles exactly one todoItem.
 */
public class TodoListAddActivity extends AppCompatActivity {

    private TodoItem todoItem;
    private TodoItemViewModel todoItemViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoItem = new TodoItem();
        if (savedInstanceState != null) {
            String itemId = savedInstanceState.getString(TodoItemConstants.id);
            if (itemId != null)
                todoItem.setId(itemId);
        }
        setContentView(R.layout.activity_todo_list_add);
        todoItemViewModel = ViewModelProviders.of(this, new TodoItemViewModelFactory())
                            .get(TodoItemViewModel.class);
        todoItemViewModel.getTodoItemAPIResult().observe(this, result ->
        {
            if (result instanceof Result.Success) {
                Object data= ((Result.Success) result).getData();
                if (data instanceof Response) {
                    Response response = (Response) data;
                    switch (response.apiName) {
                        //Set this todoItem from the response we got.
                        case Response.APIName.API_GET:
                            Log.d(ApplicationConstants.LOG_TAG, response.extras.toString());
                            break;
                        case Response.APIName.API_ADD:
                        case Response.APIName.API_EDIT:
                        case Response.APIName.API_REMOVE:
                            Log.d(ApplicationConstants.LOG_TAG, response.toString());
                    }
                }
            }
        });
        setUpListeners();
        setupTodoItem();
    }
    private void setupTodoItem() {
        if (todoItem.getId().length() != 0) {
            todoItemViewModel.getTodoItem(todoItem, Application.getLoggedInUser().getLoggedInUser());
        }
    }
    private void setUpListeners() {
        Button btnStartDate = findViewById(R.id.datePickerButton);
        Button btnEndDate = findViewById(R.id.enddatePickerButton);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        ImageButton btnDelete = findViewById(R.id.buttonDelete);

        if (todoItem.getId().length() == 0)
            btnDelete.setEnabled(false);

        btnSave.setOnClickListener(v -> {
            //Save to backend.
            if (todoItem.getId().length() == 0)
                todoItem.setId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            todoItemViewModel.addTodoItem(todoItem,
                    Application.getLoggedInUser().getLoggedInUser());
        });

        //When cancelled do nothing. User can navigate via
        //back button. Or maybe finish this activity?
        btnCancel.setOnClickListener(v -> {
        });

        //When asking for remove.
        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(TodoListAddActivity.this)
                .setTitle(R.string.deleteTodoItem)
                .setNegativeButton(R.string.noButton, (dialog, which) -> {

                })
                .setPositiveButton(R.string.yesButton, (dialog, which) ->
                        todoItemViewModel.removeTodoItem(todoItem,
                        Application.getLoggedInUser().getLoggedInUser()))
                .create().show());
        btnStartDate.setOnClickListener(v -> {
            Calendar todayCalendar = Calendar.getInstance();
            if (todoItem.getStartDate() != null) {
                todayCalendar.setTime(todoItem.getStartDate());
            }
            DatePickerDialog datePickerDialog = new
                    DatePickerDialog(TodoListAddActivity.this,
                    (view, year, month, dayOfMonth) ->
                            todoItem.setStartDate(new GregorianCalendar(year, month, dayOfMonth).getTime()),
                    todayCalendar.get(Calendar.YEAR),
                    todayCalendar.get(Calendar.MONTH),
                    todayCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnEndDate.setOnClickListener(v -> {
            Calendar todayCalendar = Calendar.getInstance();
            if (todoItem.getEndDate() != null) {
                todayCalendar.setTime(todoItem.getStartDate());
            }
            DatePickerDialog datePickerDialog = new
                    DatePickerDialog(TodoListAddActivity.this,
                    (view, year, month, dayOfMonth) ->
                            todoItem.setEndDate(new GregorianCalendar(year, month, dayOfMonth).getTime()),
                    todayCalendar.get(Calendar.YEAR),
                    todayCalendar.get(Calendar.MONTH),
                    todayCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }
}