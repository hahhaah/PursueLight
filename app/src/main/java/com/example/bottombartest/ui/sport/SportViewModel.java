package com.example.bottombartest.ui.sport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SportViewModel extends ViewModel {

  private MutableLiveData<String> mText;

  public SportViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("This is sport fragment");
  }

  public LiveData<String> getText() {
    return mText;
  }
}