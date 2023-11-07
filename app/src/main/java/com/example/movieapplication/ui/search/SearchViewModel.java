package com.example.movieapplication.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        Date x = new Date(Long.parseLong("1081157732"));
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        mText.setValue(fm.format(x));
    }

    public LiveData<String> getText() {
        return mText;
    }
}