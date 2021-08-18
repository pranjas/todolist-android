
/*
 * Copyright (c) 2021.
 * Author: Pranay Kr. Srivastava <pranjas@gmail.com>
 * Released Under GNU-GPLv3
 */

package com.example.windup.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windup.Application;
import com.example.windup.ApplicationConstants;
import com.example.windup.R;
import com.example.windup.ui.ActivityConstants;
import com.example.windup.ui.ActivityResultHandler;
import com.example.windup.ui.todolist.TodoListActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private GoogleSignInClient googleSignInClient;
    private ArrayList<ActivityResultHandler> activityResultHandlers;
    private boolean loggedIn;
    private static final String InstanceKeyLoggedIn = "logged_in";
    private GoogleSignInAccount googleSignInAccount;

    /*
     * We need the client's ID so we can send it to our REST server
     * and validate the token generated.
     */
    private void createGoogleSignInClient(boolean forceNew) {
        if (googleSignInClient != null && forceNew)
            googleSignInClient = null;

        if (googleSignInClient != null)
            return;
        /*
         * requestIdToken is what makes the token available to us. We'll send
         * this token to get it verified from Google but via our REST API.
         */
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestIdToken(getString(R.string.web_client_id))
                                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
    }

    private boolean doSignInForProvider(String authProvider) {
        switch (authProvider) {
            case ApplicationConstants.AuthProviderGoogle:
                return doGoogleSignIn();
            default:
                return false;
        }
    }
    /*
     * Start google signin Activity.
     * TODO: Make it more simpler.
     * So we could be here
     * 1) When the user is first signing-in to our app for the very first time.
     * 2) She/he has signed in earlier so instead of opening a "pop-up" try a silentSignIn.
     * 3) In case silentSignIn fails for any reason other than "token-expired" use the "pop-up"
     *    flow.
     */
    private boolean doGoogleSignIn() {
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            Task<GoogleSignInAccount> task = googleSignInClient.silentSignIn();

            if (task.isSuccessful()) {
                setGoogleSignInAccount(task.getResult());
            } else {
                task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        try {
                            GoogleSignInAccount signInAccount = task.getResult(ApiException.class);
                            setGoogleSignInAccount(signInAccount);
                            loginViewModel.validateTPTLogin(googleSignInAccount.getIdToken(),
                                        ApplicationConstants.AuthProviderGoogle);
                        } catch (ApiException e) {
                            Log.d(ApplicationConstants.LOG_TAG_FATAL, "Couldn't access google account" +
                                    e.getMessage());
                            final ProgressBar loadingProgressBar = findViewById(R.id.loading);
                            loadingProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
                /*
                 * We don't know if the signon was a success or not at this time.
                 */
                return false;
            }
            return true;
        }
        Intent gsignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(gsignInIntent, ActivityConstants.ACTIVITY_GOOGLE_SIGNIN_REQUEST);
        return !(googleSignInAccount == null);
    }

    /*
     * We'll be coming here from multiple screens at the end.
     * Currently we only handle the GoogleSignIn activity result however
     * we could be in one of the settings or TODO item's screen as well.
     * We'll add more handlers without modifying this piece of code again.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        for (ActivityResultHandler handler : activityResultHandlers) {
            boolean requestCodeMatched = false;
            boolean resultCodeMatched = false;

            if (handler.getRequestCode() == ActivityResultHandler.REQUEST_CODE_ANY)
                requestCodeMatched = true;
            if (handler.getResultCode() == ActivityResultHandler.RESULT_CODE_ANY)
                resultCodeMatched = true;
            if (!requestCodeMatched && handler.getRequestCode() == requestCode)
                    requestCodeMatched = true;
            if (!resultCodeMatched && handler.getResultCode() == resultCode)
                    resultCodeMatched = true;

            if (requestCodeMatched && resultCodeMatched) {
                handler.handleActivityResult();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * TODO: We still need to save state for orientation changes,
     * viz, after clicking on googleSignIn if we rotate the device while
     * waiting for the token verification.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final SignInButton googleSignInButton = findViewById(R.id.sign_in_button);
        /*
         * These are registered onActivityForResult handlers.
         */
        activityResultHandlers = new ArrayList<>();
        createGoogleSignInClient(true);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    return;
                }
                if (loginResult.getSuccess() != null) {
                    LoggedInUserView loggedInUserView = loginResult.getSuccess();
                    if (loggedInUserView.isExpired()) {
                        doSignInForProvider(loggedInUserView.getProvider());
                        return;
                    }
                    updateUiWithUser(loginResult.getSuccess());
                    Application.setLoggedInUserView(loggedInUserView);
                }
                loggedIn = true;
                startActivity(new Intent().setClass(LoginActivity.this,
                        TodoListActivity.class));
                //setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!doGoogleSignIn()) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                }
                if (googleSignInAccount != null) {
                    //Validate googleSignin.
                   loadingProgressBar.setVisibility(View.VISIBLE);
                   loginViewModel.validateTPTLogin(googleSignInAccount.getIdToken(),
                           ApplicationConstants.AuthProviderGoogle);
                }
            }
        });
        /*
         * Add our onActivityForResult Handlers here.
         */
        addActivityResultHandler(new GoogleSigninResultHandler(ActivityConstants.ACTIVITY_GOOGLE_SIGNIN_REQUEST,
                                ActivityResultHandler.RESULT_CODE_ANY, this));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
         * We need to know if we were already logged in earlier when
         * the app state was saved. This isn't really useful currently.
         */
        outState.putBoolean(InstanceKeyLoggedIn, loggedIn);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void addActivityResultHandler(ActivityResultHandler handler) {
        this.activityResultHandlers.add(handler);
    }
}

class GoogleSigninResultHandler extends ActivityResultHandler {
    private LoginActivity loginActivity;
    private GoogleSigninResultHandler(int requestCode, int resultCode) {
        super(requestCode, resultCode);
    }
    public GoogleSigninResultHandler(int requestCode, int resultCode, LoginActivity loginActivity) {
        this(requestCode, resultCode);
        this.loginActivity = loginActivity;
    }
    @Override
    public void handleActivityResult() {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            this.loginActivity.setGoogleSignInAccount(googleSignInAccount);
        } catch (ApiException e) {
            Log.d(ApplicationConstants.LOG_TAG_INFO, "Failed to signin through google." +
                    e.getMessage());
            final ProgressBar loadingProgressBar = this.loginActivity.findViewById(R.id.loading);
            loadingProgressBar.setVisibility(View.GONE);
        }
    }
}