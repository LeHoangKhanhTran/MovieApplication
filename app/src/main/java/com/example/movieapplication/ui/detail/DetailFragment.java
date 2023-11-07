package com.example.movieapplication.ui.detail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.movieapplication.MainActivity;
import com.example.movieapplication.R;
import com.example.movieapplication.adapter.CastAdapter;
import com.example.movieapplication.databinding.FragmentDetailBinding;
import com.example.movieapplication.databinding.FragmentHomeBinding;
import com.example.movieapplication.model.Cast;
import com.example.movieapplication.model.Movie;
import com.example.movieapplication.model.People;
import com.example.movieapplication.ui.search.SearchFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DetailFragment extends Fragment {

    private DetailViewModel mViewModel;
    private FragmentDetailBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference moviesReference;

    private Movie movie;
    private DatabaseReference usersReference;
    private DatabaseReference peopleReference;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SharedPreferences sh = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        List<String> watchlist = new ArrayList<>(sh.getStringSet("watchlist", new HashSet<>()));
        String id = getArguments().getString("id");
        database = FirebaseDatabase.getInstance();
        moviesReference = database.getReference("movies");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        usersReference = database.getReference("users");
        peopleReference = database.getReference("people");
        moviesReference.orderByChild("movieId").equalTo(id).limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<Movie> type = new GenericTypeIndicator<Movie>() {};
                movie = snapshot.getValue(type);
                if (watchlist.contains(movie.getMovieId()))
                {
                    binding.undoButton.setVisibility(View.VISIBLE);
                }
                else {
                    binding.addButton.setVisibility(View.VISIBLE);
                }
                Picasso.get().load(movie.getBackdropImg()).resize(1560, 700).centerCrop().into(binding.background);
                binding.movieTitle.setText(movie.getMovieTitle() + " (" + movie.getReleaseYear() + ")");
                binding.movieDuration.setText(movie.getDuration());
                binding.releaseDate.setText(movie.getReleaseDate());
                binding.movieRating.setText(String.valueOf(movie.getRating()));
                Picasso.get().load(movie.getPosterImg()).resize(485, 620).centerCrop().into(binding.poster);
                binding.summary.setText(movie.getStoryline());
                String genres = "";
                for (int i = 0; i < movie.getGenres().size(); i++)
                {
                    genres += movie.getGenres().get(i);
                    if (i < movie.getGenres().size() - 1)
                    {
                        genres += ", ";
                    }
                }
                binding.movieGenres.setText(genres);
                binding.director.setText(movie.getDirector());
                binding.writer.setText(movie.getWriter());
                GenericTypeIndicator<List<Cast>> t = new GenericTypeIndicator<List<Cast>>() {};
                List<Cast> casts = snapshot.child("cast").getValue(t);
                List<People> peopleList = new ArrayList<>();
                for (Cast c: casts) {
                    peopleReference.orderByChild("peopleId").equalTo(c.getPeopleId()).limitToFirst(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            GenericTypeIndicator<People> t = new GenericTypeIndicator<People>() {};
                            People people = snapshot.getValue(t);
                            peopleList.add(people);
                            if (peopleList.size() == casts.size())
                            {
                                CastAdapter castAdapter = new CastAdapter(getContext(), casts, peopleList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                                binding.recyclerView.setLayoutManager(layoutManager);
                                binding.recyclerView.setAdapter(castAdapter);
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
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        String name = getArguments().getString("movie");
        View root = binding.getRoot();
        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(name);
        toolbar.collapseActionView();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersReference.child(firebaseAuth.getUid()).child("watchList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        List<String> movieIdList;
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        if (dataSnapshot.getValue(t) == null)
                        {
                            movieIdList = new ArrayList<>();
                            movieIdList.add(movie.getMovieId());
                        }
                        else {
                            movieIdList = new ArrayList<>(dataSnapshot.getValue(t));
                            movieIdList.add(movie.getMovieId());
                        }
                        Map<String, Object> watchListUpdate = new HashMap<>();
                        watchListUpdate.put("watchList", movieIdList);
                        usersReference.child(firebaseAuth.getUid()).updateChildren(watchListUpdate);
                        SharedPreferences sh = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
                        sh.edit().putStringSet("watchlist", new HashSet<>(movieIdList)).commit();
                    }
                });
                v.setVisibility(View.GONE);
                binding.undoButton.setVisibility(View.VISIBLE);
            }
        });
        binding.undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersReference.child(firebaseAuth.getUid()).child("watchList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        List<String> movieIdList;
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        if (dataSnapshot.getValue(t) != null)
                        {
                            movieIdList = new ArrayList<>(dataSnapshot.getValue(t));
                            movieIdList.remove(movie.getMovieId());
                            Map<String, Object> watchListUpdate = new HashMap<>();
                            watchListUpdate.put("watchList", movieIdList);
                            usersReference.child(firebaseAuth.getUid()).updateChildren(watchListUpdate);
                            sh.edit().putStringSet("watchlist", new HashSet<>(movieIdList)).commit();
                        }
                    }
                });
                v.setVisibility(View.GONE);
                binding.addButton.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        // TODO: Use the ViewModel
    }

}