package com.example.movieapplication.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.movieapplication.model.Movie;
import com.example.movieapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Repository {
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    public Repository()
    {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public List<Movie> getTopRatedMovies()
    {
        List<Movie> topRatedList = new ArrayList<>();
        databaseReference.child("movies").orderByChild("rating").limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<Movie> type = new GenericTypeIndicator<Movie>() {};
                Movie result = snapshot.getValue(type);
                topRatedList.add(result);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return topRatedList;
    }

    public List<Movie> getUpcomingMovies()
    {
        List<Movie> upcomingMovies = new ArrayList<>();
        databaseReference.child("movies").orderByChild("releaseDate").limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<Movie> type = new GenericTypeIndicator<Movie>() {};
                Movie result = snapshot.getValue(type);
                upcomingMovies.add(result);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return upcomingMovies;
    }
}
