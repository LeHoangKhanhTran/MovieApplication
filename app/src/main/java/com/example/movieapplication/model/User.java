package com.example.movieapplication.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    String email;
    String username;
    List<String> watchList;

    public User()
    {
        email = "";
        username = "";
        watchList = new ArrayList<String>();
    }
    public User(String email, String username)
    {
        this.email = email;
        this.username = username;
    }
    public User(String email, String username, List<String> watchList)
    {
        this.email = email;
        this.username = username;
        for (String id : watchList) {
            this.watchList.add(id);
        }
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
    public List<String> getWatchList() {
        return watchList;
    }

}
