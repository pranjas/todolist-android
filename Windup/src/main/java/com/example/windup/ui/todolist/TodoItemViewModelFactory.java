/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.windup.data.TodoItemRepository;

public class TodoItemViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoItemViewModel.class)) {
            return (T) new TodoItemViewModel(TodoItemRepository.
                    getInstance(TodoItemRepository.DataSourceType.POSITIONAL));
        }
        throw new IllegalArgumentException("Required " + TodoItemViewModel.class.getName() +
                " Given " + modelClass.getName());
    }
}
