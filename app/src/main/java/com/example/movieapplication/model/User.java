package com.example.movieapplication.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    String email;
    String username;
    List<Movie> watchList;
    List<People> favouriteCelebs;

    public User()
    {
        email = "";
        username = "";
        watchList = new ArrayList<Movie>();
        favouriteCelebs = new ArrayList<People>();
    }
    public User(String email, String username)
    {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<Movie> getWatchList() {
        return watchList;
    }
    public void setWatchList(List<Movie> watchList) {
        this.watchList = watchList;
    }
    public List<People> getFavouriteCelebs() {
        return favouriteCelebs;
    }
    public void setFavouriteCelebs(List<People> favouriteCelebs) {
        this.favouriteCelebs = favouriteCelebs;
    }
}
