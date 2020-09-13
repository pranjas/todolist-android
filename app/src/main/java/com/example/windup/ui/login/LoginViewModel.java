/*
 * Copyright (c) 2020.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.login;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.windup.data.LoginRepository;
import com.example.windup.data.Result;
import com.example.windup.data.model.LoggedInUser;
import com.example.windup.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LiveData<LoginResult> loginResult;
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        /*
         * Bind the resulting LoginResult to the values emmitted by the
         * repository.
         */
        loginResult = Transformations.map(loginRepository.getRepositoryLiveData(),
                new Function<LoggedInUser, LoginResult>() {
            @Override
            public LoginResult apply(LoggedInUser input) {
                if (input != null) {
                    return new LoginResult(new LoggedInUserView(input));
                }
                return new LoginResult((R.string.login_failed));
            }
        });
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password);
    }

    public void validateTPTLogin(@NonNull String tokenID, @NonNull String authProvider) {
        loginRepository.validatTPTLogin(tokenID, authProvider);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}