/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.windup.data.model.LoggedInUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private LoginDataSource dataSource;
    private MutableLiveData<LoggedInUser> repositoryLiveData;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
            instance.repositoryLiveData = new MutableLiveData<>();
        }
        return instance;
    }
    public MutableLiveData<LoggedInUser> getRepositoryLiveData() {
        return instance.repositoryLiveData;
    }

    public void login(final String username, final String password) {
        // handle login
        /*
         * We can't do network ops on the main thread, hence
         * make a separate thread and execute the api there.
         * Note that we do a postValue instead of setValue.
         * Once the value is posted this would be picked up by the ViewModel's
         * Transformations.map and then posted "up" to the UI.
         */
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LoggedInUser result = dataSource.login(username, password);
            repositoryLiveData.postValue(result);
        });
    }

    public void validatTPTLogin(final String tokenID, final String authProvider) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LoggedInUser result = dataSource.validateToken(tokenID, authProvider);
            repositoryLiveData.postValue(result);
        });
    }
    /*
     * TODO : Implement Logout
     */
    public void logout() {

    }
}