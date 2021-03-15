package com.example.hikingapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.hikingapp.data.LoginListener;
import com.example.hikingapp.data.LoginRepository;
import com.example.hikingapp.data.Result;
import com.example.hikingapp.R;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel implements LoginListener {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginViewState> loginViewState = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) { this.loginRepository = loginRepository; }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    LiveData<LoginViewState> getLoginViewState(){ return loginViewState; }

    public void login(String email, String password) {
        // register self for callback
        loginRepository.registerListener(this);
        // start log in
        loginRepository.login(email, password);
    }

    public void register(String username, String email, String password){
        // register self for callback
        loginRepository.registerListener(this);
        // start registration
        loginRepository.register(username, email, password);
    }

    public void loginDataChanged(String username, String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_email, null));
        } else if (!isUsernameValid(username)){
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void loginViewStateChanged(String newState){
        if(newState.equals(LoginViewState.STATE_LOGIN)){
            loginViewState.setValue(new LoginViewState(LoginViewState.STATE_LOGIN));
        }else{
            loginViewState.setValue(new LoginViewState(LoginViewState.STATE_REGISTER));
        }

    }

    // TODO: update username validation
    private boolean isUsernameValid(String username){
        if(username == null ||
                (username.length() == 0 && loginViewState.getValue().getState().equals(LoginViewState.STATE_REGISTER))){
            return false;
        }else{
            return true;
        }
    }

    // TODO: update email validation
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // TODO: update password validation
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onLoginResult(Result result){
        if(result instanceof Result.Success){
            loginResult.setValue(new LoginResult(new LoggedInUserView(((Result.Success<FirebaseUser>) result).getData().getDisplayName())));
        }else if(result instanceof Result.Error){
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }
}