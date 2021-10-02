/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.todolist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.windup.R;

public class TodoListViewHolder extends RecyclerView.ViewHolder {
    private String description;
    private String startDate;
    private String endDate;

    public TodoListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void bindView() {

        TextView textView = itemView.findViewById(R.id.description);

        textView.setText(description);

        textView = itemView.findViewById(R.id.start);
        textView.setText(startDate);

        textView = itemView.findViewById(R.id.end);
        textView.setText(endDate);
    }
}
