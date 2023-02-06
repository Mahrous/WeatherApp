package com.graduation.mah.weather.ui.fragmentWeather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fragmentWeather fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}