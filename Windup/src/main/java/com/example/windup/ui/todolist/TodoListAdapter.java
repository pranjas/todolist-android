/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.windup.ApplicationConstants;
import com.example.windup.R;
import com.example.windup.apiaccess.model.TodoItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class TodoListAdapter extends PagedListAdapter<TodoItem, TodoListViewHolder> {
    public TodoListAdapter() {
        super(TodoItemDiffCallback);
    }
    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.todoitem_desc, parent, false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        TodoItem todoItem = getItem(position);
        if (todoItem != null) {
            holder.setDescription(todoItem.getDescription());
            holder.setEndDate(todoItem.getEndDate().toString());
            holder.setStartDate(todoItem.getStartDate().toString());

        } else {
            final String holderText = "XXXXXXX";
            holder.setDescription(holderText);
            holder.setEndDate(holderText);
            holder.setStartDate(holderText);
        }
        holder.bindView();
    }

    private static DiffUtil.ItemCallback<TodoItem> TodoItemDiffCallback =
            new DiffUtil.ItemCallback<TodoItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull TodoItem oldItem, @NonNull TodoItem newItem) {
                    return false;
                    //return oldItem.equals(newItem);
                }

                //We always assume contents are never same.
                @Override
                public boolean areContentsTheSame(@NonNull TodoItem oldItem, @NonNull TodoItem newItem) {
                    return false;
                    //return oldItem.equals(newItem);
                }
            };

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<TodoItem> previousList, @Nullable PagedList<TodoItem> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        Log.d(ApplicationConstants.LOG_TAG, "Changed List");
    }
}
