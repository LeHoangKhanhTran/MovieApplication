package com.example.movieapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapplication.data.Repository;
import com.example.movieapplication.model.Movie;
import com.google.firebase.database.core.Context;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Movie>> topRatedMovies;
    private final MutableLiveData<List<Movie>> upcomingMovies;

    public HomeViewModel() {
        topRatedMovies = new MutableLiveData<>();
        upcomingMovies = new MutableLiveData<>();
       /* Repository mRepo = new Repository();
        topRatedMovies.setValue(mRepo.getTopRatedMovies());
        upcomingMovies.setValue(mRepo.getUpcomingMovies());*/

    }
    /*public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }
    public LiveData<List<Movie>> getUpcomingMovies() { return upcomingMovies; }*/
}