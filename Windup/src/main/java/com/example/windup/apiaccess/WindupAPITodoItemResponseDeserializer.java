/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.apiaccess;

import com.example.windup.apiaccess.model.TodoItem;
import com.example.windup.apiaccess.model.response.Response;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WindupAPITodoItemResponseDeserializer implements JsonDeserializer<Response> {

    @Override
    public Response deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
       Response response = new Gson().fromJson(json.getAsJsonObject(), Response.class);
       if (response.extras != null) {
           Object object = response.extras.get("items");
           if (object != null) {
               Gson gson = new Gson();
               String todoItemsJSON = gson.toJson(object);
               ArrayList<TodoItem> listItems = gson.fromJson(todoItemsJSON,
                                                new TypeToken<ArrayList<TodoItem>>(){}.getType());
               response.extras.remove("items");
               response.extras.put("items", listItems);
           }
       }
       return response;
    }
}
