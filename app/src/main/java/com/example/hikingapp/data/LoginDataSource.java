package com.example.hikingapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static final String TAG = "LoginDataSource";

    private static LoginDataSource loginDataSource;
    private static LoginListener listener;
    private FirebaseAuth auth;

    public interface LoginDataSourceListener{

    }

    // private constructor
    private LoginDataSource(){
        auth = FirebaseAuth.getInstance();
    }

    // singleton access
    public static LoginDataSource getInstance(){
        if (loginDataSource == null) {
            loginDataSource = new LoginDataSource();
        }
        return loginDataSource;
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "login:success");
                            Result.Success<FirebaseUser> result = new Result.Success<>(auth.getCurrentUser());
                            // send the result to the LoginRepository
                            listener.onLoginResult(result);
                        }else{
                            Log.w(TAG, "login:failure");
                            Result.Error result = new Result.Error(task.getException());
                            listener.onLoginResult(result);
                        }

                    }
                });
    }

    public void register(String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            Log.i(TAG, "register:success");
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            if(auth.getCurrentUser() != null){
                                auth.getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>(){
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task){
                                                if(task.isSuccessful()){
                                                    Log.i(TAG, "register.updateProfile:success");
                                                    Result.Success<FirebaseUser> result = new Result.Success<>(auth.getCurrentUser());
                                                    listener.onLoginResult(result);
                                                }
                                            }
                                        });
                            }else{
                                Log.w(TAG, "currentUser is null?");
                            }
                        }else{
                            Log.w(TAG, "register:failure");
                            Result.Error result = new Result.Error(task.getException());
                            listener.onLoginResult(result);
                        }
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    public void registerListener(LoginListener listener){
        this.listener = listener;
    }
}