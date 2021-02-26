package com.example.hikingapp.ui.plans;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlansViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PlansViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("TODO: plans fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}