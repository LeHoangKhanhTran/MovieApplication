package com.example.movieapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.LoginActivity;
import com.example.movieapplication.MainActivity;
import com.example.movieapplication.R;
import com.example.movieapplication.SplashActivity;
import com.example.movieapplication.adapter.MovieAdapter;
import com.example.movieapplication.data.Repository;
import com.example.movieapplication.databinding.FragmentHomeBinding;
import com.example.movieapplication.model.Movie;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private  HomeViewModel homeViewModel;

    private FirebaseDatabase database;

    private DatabaseReference databaseReference;

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Home");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //loadDataOnStart();
        /*Observer<List<Movie>> movieObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                MovieAdapter movieAdapter = new MovieAdapter(getContext(), movies);
                recyclerView.setAdapter(movieAdapter);
            }

        };*/
        //homeViewModel.getTopRatedMovies().observe(getViewLifecycleOwner(), movieObserver);
        RecyclerView recyclerView2 = binding.recyclerView2;
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        databaseReference.child("users").child(FirebaseAuth.getInstance().getUid()).child("watchList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> ids = new ArrayList<>();
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                if (dataSnapshot.getValue(t) != null)
                {
                    ids = dataSnapshot.getValue(t);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("watchlist", new HashSet<>(ids));
                    editor.commit();
                }
                loadDataOnStart(ids);
            }
        });
        /*Observer<List<Movie>> movieObserver2 = new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movieList) {
                MovieAdapter movieAdapter = new MovieAdapter(getContext(), movieList);
                recyclerView2.setAdapter(movieAdapter);
            }

        };*/
        //homeViewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), movieObserver2);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadDataOnStart(List<String> watchListIds)
    {
        List<Movie> topRatedList = new ArrayList<>();
        databaseReference.child("movies").orderByChild("rating").limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<Movie> type = new GenericTypeIndicator<Movie>() {};
                Movie result = snapshot.getValue(type);
                topRatedList.add(result);
                if (topRatedList.size() == 7)
                {
                    MovieAdapter movieAdapter = new MovieAdapter(getContext(), topRatedList, watchListIds);
                    binding.recyclerView.setAdapter(movieAdapter);
                }
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
        List<Movie> upcomingMovies = new ArrayList<>();
        databaseReference.child("movies").orderByChild("releaseDate").limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<Movie> type = new GenericTypeIndicator<Movie>() {};
                Movie result = snapshot.getValue(type);
                upcomingMovies.add(result);
                if (upcomingMovies.size() == 7)
                {
                    MovieAdapter movieAdapter = new MovieAdapter(getContext(), upcomingMovies, watchListIds);
                    binding.recyclerView2.setAdapter(movieAdapter);
                }
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
    }

}