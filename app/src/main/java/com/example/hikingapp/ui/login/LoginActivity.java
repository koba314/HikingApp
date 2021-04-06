package com.example.hikingapp.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikingapp.R;
import com.example.hikingapp.ui.login.LoginViewModel;
import com.example.hikingapp.ui.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    public static final String NEXT = "LoginActivity.nextActivity";

    private LoginViewModel loginViewModel;

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    Button registerQButton;
    Button registerBackButton;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        registerQButton = findViewById(R.id.register_q);
        registerBackButton = findViewById(R.id.register_back);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                registerButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null){
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getEmailError() != null) {
                    emailEditText.setError(getString(loginFormState.getEmailError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                    finish();
                }

            }
        });

        loginViewModel.getLoginViewState().observe(this, new Observer<LoginViewState>() {
            @Override
            public void onChanged(@Nullable LoginViewState loginViewState) {
                if(loginViewState.getState().equals(LoginViewState.STATE_LOGIN)){
                    usernameEditText.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.GONE);
                    registerBackButton.setVisibility(View.GONE);
                    registerQButton.setVisibility(View.VISIBLE);
                }else if(loginViewState.getState().equals(LoginViewState.STATE_REGISTER)) {
                    usernameEditText.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    registerButton.setVisibility(View.VISIBLE);
                    registerBackButton.setVisibility(View.VISIBLE);
                    registerQButton.setVisibility(View.GONE);
                }
            }
        });

        // set initial state of the layout
        loginViewModel.loginViewStateChanged(LoginViewState.STATE_LOGIN);

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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(), emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(loginViewModel.getLoginViewState().getValue().getState().equals(LoginViewState.STATE_REGISTER)){
                        loginViewModel.register(usernameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
                    }else{
                        loginViewModel.login(emailEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.register(usernameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        registerQButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginViewModel.loginViewStateChanged(LoginViewState.STATE_REGISTER);
            }
        });

        registerBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginViewModel.loginViewStateChanged(LoginViewState.STATE_LOGIN);
            }
        });

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + " " + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        usernameEditText.clearFocus();
        emailEditText.clearFocus();
        passwordEditText.clearFocus();
//        hideKeyboard();
        usernameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        loginViewModel.loginDataChanged("","","");
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}