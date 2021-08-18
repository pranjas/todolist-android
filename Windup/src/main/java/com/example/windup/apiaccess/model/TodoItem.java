/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model;

import com.example.windup.ApplicationConstants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TodoItem {
    private transient String description;
    @SerializedName("start_time")
    private Date startDate;

    @SerializedName("end_time")
    private Date endDate;
    private String id;
    private Map<String, Object> content;
    private Map<String, Object> actions;
    private transient boolean isOwner;
    private transient boolean isShared;
    private ArrayList<String> sharedWith;
    private String name;

    public TodoItem() {
        description = ApplicationConstants.EMPTY_STRING;
        startDate = Calendar.getInstance().getTime();
        endDate = startDate;
        id = ApplicationConstants.EMPTY_STRING;
        content = new ConcurrentHashMap<>();
        actions = new ConcurrentHashMap<>();
        isOwner = false;
        isShared = false;
        sharedWith = new ArrayList<>();
        name = ApplicationConstants.EMPTY_STRING;
    }

    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }
    public ArrayList<String> getSharedWith() {
        return sharedWith;
    }

    public void addSharedOwner(String sharedOwner) {
        sharedWith.add(sharedOwner);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public void addContent (String key, Object value) {
        content.put(key, value);
    }
    public void addAction (String key, Object value) {
        actions.put(key, value);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (! (obj instanceof TodoItem))
            return false;
        TodoItem item = (TodoItem)obj;
        return item.id.equals(this.id);
    }
}
