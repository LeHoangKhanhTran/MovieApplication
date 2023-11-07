package com.example.movieapplication.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class People {
    private String biography;
    private String birthday;
    private String fullName;
    private String gender;
    private String image;
    private List<String> movies;
    private String peopleId;
    private String placeOfBirth;

    public People()
    {
        this.biography = "";
        this.birthday = "";
        this.fullName = "";
        this.gender = "";
        this.image = "";
        this.movies = new ArrayList<>();
        this.peopleId = "";
        this.placeOfBirth = "";
    }

    public People(String biography, String birthday, String fullName, String gender,
                  String image, List<String> movies, String peopleId, String placeOfBirth)
    {
        this.biography = biography;
        this.birthday = birthday;
        this.fullName = fullName;
        this.gender = gender;
        this.image = image;
        for (String movieId: movies) {
            this.movies.add(movieId);
        }
        this.peopleId = peopleId;
        this.placeOfBirth = placeOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPeopleId() {
        return peopleId;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getImage() {
        return image;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getMovies() {
        return movies;
    }
}
