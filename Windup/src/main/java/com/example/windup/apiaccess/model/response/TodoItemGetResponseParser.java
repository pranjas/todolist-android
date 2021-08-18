/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess.model.response;

import android.util.Log;

import com.example.windup.ApplicationConstants;
import com.example.windup.ApplicationException;
import com.example.windup.apiaccess.model.TodoItem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TodoItemGetResponseParser extends ResponseParser {
    private ArrayList<TodoItem> todoItems;
    private enum TodoItemResponseKey {
        NAME,
        CONTENT,
        ACTIONS,
        START_TIME,
        END_TIME,
        ID,
        SHAREDWITH;

        public String getJSONKey() {
            switch (this) {
                case SHAREDWITH:
                    return "sharedWith";
                default:
                    return this.name().toLowerCase();
            }
        }
    }
    public TodoItemGetResponseParser(byte[] inputData) throws UnsupportedEncodingException, ApplicationException {
        super(inputData);
        todoItems = new ArrayList<>();
    }

    public ArrayList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public int getItemCount() {
        return Integer.parseInt(response == null ? "0" : String.valueOf(response.extras.get("count")));
    }

    @Override
    public void parseArray(String currentKey) {
        try {
            jsonReader.beginArray();
            Log.d(ApplicationConstants.LOG_TAG_INFO, "Got json key as " + currentKey);
            if (currentKey.equals("items")) {
                TodoItem item = nextTodoItem();
                if (item != null)
                    todoItems.add(item);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Unable to parse todoItem JSON");
        }
    }

    private TodoItem nextTodoItem() {
        TodoItem item = null;
        try {
            item = new TodoItem();
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                for (TodoItemResponseKey responseKey: TodoItemResponseKey.values()) {
                    if (responseKey.getJSONKey().equals(key)) {
                        switch (responseKey) {
                            case SHAREDWITH:
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    String sharedWith = jsonReader.nextString();
                                    item.setShared(true);
                                    item.addSharedOwner(sharedWith);
                                }
                                jsonReader.endArray();
                                break;
                            case ID:
                                item.setId(jsonReader.nextString());
                                break;
                            case NAME:
                                item.setName(jsonReader.nextString());
                                break;
                            case ACTIONS:
                            case CONTENT:
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()) {
                                    String nextKey = jsonReader.nextName();
                                    String nextValue = jsonReader.nextString();
                                    if (responseKey == TodoItemResponseKey.ACTIONS)
                                        item.addAction(nextKey, nextValue);
                                    else
                                        item.addContent(nextKey, nextValue);
                                }
                                jsonReader.endArray();
                                break;
                            case END_TIME:
                                 item.setEndDate(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")
                                                    .parse(jsonReader.nextString()));
                                break;
                            case START_TIME:
                                item.setStartDate(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")
                                        .parse(jsonReader.nextString()));
                                break;
                        }
                    }
                }
            }
            jsonReader.endObject();
        } catch (IOException | ParseException e) {
            Log.d(ApplicationConstants.LOG_TAG_ERROR, "Unable to parse todoItem JSON"
             + e.getLocalizedMessage());
            item = null;
        }
        return item;
    }

    @Override
    public void parseObject(String currentKey) {

    }
}
