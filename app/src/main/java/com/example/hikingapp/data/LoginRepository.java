package com.example.hikingapp.data;

import com.google.firebase.auth.FirebaseUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository implements LoginListener{

    private static volatile LoginRepository instance;

    private static LoginListener listener;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
//    private LoggedInUser user = null;
    private FirebaseUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public FirebaseUser getUser(){
        return user;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(FirebaseUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String email, String password) {
        // register self for callback
        dataSource.registerListener(this);
        // handle login
        dataSource.login(email, password);
    }

    public void register(String username, String email, String password){
        // register self for callback
        dataSource.registerListener(this);
        // handle registration
        dataSource.register(username, email, password);
    }

    @Override
    public void onLoginResult(Result result){
        if(result instanceof Result.Success){
            user = ((Result.Success<FirebaseUser>) result).getData();
        }
        // propagate the result back to LoginViewModel
        listener.onLoginResult(result);
    }

    public void registerListener(LoginListener listener){
        this.listener = listener;
    }

}