package com.example.hikingapp.ui.login;

public class LoginViewState {
    public static final String STATE_LOGIN = "LoginViewState.STATE_LOGIN";
    public static final String STATE_REGISTER = "LoginViewState.STATE_REGISTER";

    private String state;

    LoginViewState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }
}
