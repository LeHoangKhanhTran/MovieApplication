package com.example.movieapplication.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {
    private String backdropImg;
    private List<Cast> cast;
    private String director;
    private String duration;
    private List<String> genres;
    private String movieId;
    private String movieTitle;
    private String posterImg;
    private float rating;
    private long releaseDate;
    private String releaseYear;
    private String storyline;
    private String writer;

    public Movie()
    {
        this.backdropImg = "";
        this.cast = new ArrayList<>();
        this.director = "";
        this.duration = "";
        this.genres = new ArrayList<>();
        this.movieId = "";
        this.movieTitle = "";
        this.posterImg = "";
        this.rating = 0f;
        this.releaseDate = 0;
        this.releaseYear = "";
        this.storyline = "";
        this.writer = "";
    }

    public Movie(String backdropImg, List<Cast> cast, String director, String duration,
                 List<String> genres, String movieId, String movieTitle, String posterImg, float rating,
                 long releaseDate, String releaseYear, String storyline, String writer)
    {
        this.backdropImg = backdropImg;
        this.cast = cast;
        this.director = director;
        this.duration = duration;
        for (String genre: genres) {
            this.genres.add(genre);
        }
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.posterImg = posterImg;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.releaseYear = releaseYear;
        this.storyline = storyline;
        this.writer = writer;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getDuration() {
        return duration;
    }

    public String getPosterImg() {
        return posterImg;
    }

    public float getRating() {
        return rating;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getReleaseDate() {
        Date x = new Date(releaseDate * 1000);
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = fm.format(x);
        return formattedDate;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getBackdropImg() {
        return backdropImg;
    }

    public String getStoryline() {
        return storyline;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

}
