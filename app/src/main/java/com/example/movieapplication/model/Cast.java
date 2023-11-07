package com.example.movieapplication.model;

public class Cast {
    private String character;
    private String peopleId;

    public Cast()
    {
        this.character = "";
        this.peopleId = "";
    }

    public Cast(String character, String peopleId)
    {
        this.character = character;
        this.peopleId = peopleId;
    }

    public String getCharacter() {
        return character;
    }

    public String getPeopleId() {
        return peopleId;
    }
}
