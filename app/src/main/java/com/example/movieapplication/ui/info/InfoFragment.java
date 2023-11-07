package com.example.movieapplication.ui.info;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
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
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapplication.MainActivity;
import com.example.movieapplication.R;
import com.example.movieapplication.adapter.MovieAdapter;
import com.example.movieapplication.databinding.FragmentDetailBinding;
import com.example.movieapplication.databinding.FragmentInfoBinding;
import com.example.movieapplication.model.Movie;
import com.example.movieapplication.model.People;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InfoFragment extends Fragment {

    private InfoViewModel mViewModel;
    private FragmentInfoBinding binding;

    private FirebaseDatabase database;

    private DatabaseReference databaseReference;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SharedPreferences sh = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        List<String> watchlist = new ArrayList<>(sh.getStringSet("watchlist", new HashSet<>()));
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        DatabaseReference peopleReference = databaseReference.child("people");
        DatabaseReference moviesReference = databaseReference.child("movies");
        String id = getArguments().getString("id");
        String name = getArguments().getString("name");
        peopleReference.orderByChild("peopleId").equalTo(id).limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<People> t = new GenericTypeIndicator<People>() {};
                People people = snapshot.getValue(t);
                Picasso.get().load(people.getImage()).resize(485, 630).centerCrop().into(binding.img);
                binding.name.setText(people.getFullName());
                binding.birthday.setText(people.getBirthday());
                binding.birthplace.setText(people.getPlaceOfBirth());
                binding.gender.setText(people.getGender());
                binding.bio.setText(people.getBiography());
                List<Movie> movies = new ArrayList<>();
                /*Log.d("x", String.valueOf(people.getMovieIds().size()));*/
                for (String movieId: people.getMovies()) {
                    moviesReference.orderByChild("movieId").equalTo(movieId).limitToFirst(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            GenericTypeIndicator<Movie> type = new GenericTypeIndicator<Movie>() {};
                            Movie movie = snapshot.getValue(type);
                            movies.add(movie);
                            if (movies.size() == people.getMovies().size())
                            {
                                MovieAdapter movieAdapter = new MovieAdapter(getContext(), movies, watchlist);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                                binding.recyclerview.setLayoutManager(layoutManager);
                                binding.recyclerview.setAdapter(movieAdapter);
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
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        // TODO: Use the ViewModel
    }

}