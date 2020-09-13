/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.windup.Application;
import com.example.windup.ApplicationConstants;
import com.example.windup.R;
import com.example.windup.ui.login.LoggedInUserView;

public class TodoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            case android.R.id.home:
                setResult(ActivityConstants.ACTIVITY_TODOLIST_RESULT, null);
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}