package com.example.movieapplication.ui.you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.example.movieapplication.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.MainActivity;
import com.example.movieapplication.R;
import com.example.movieapplication.adapter.MovieAdapter;
import com.example.movieapplication.databinding.FragmentYouBinding;
import com.example.movieapplication.model.Movie;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class YouFragment extends Fragment {

    private FragmentYouBinding binding;
    private List<String> moviesId;

    private Fragment self = this;

    public FragmentYouBinding getBinding() {
        return binding;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sh = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
        List<String> watchlist = new ArrayList<>(sh.getStringSet("watchlist", new HashSet<>()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        YouViewModel youViewModel =
                new ViewModelProvider(this).get(YouViewModel.class);

        binding = FragmentYouBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        reference.child(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("username").getValue();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(username);
            }
        });
        reference.child(firebaseAuth.getUid()).child("watchList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List<String> ids = dataSnapshot.getValue(t);
                if (ids == null)
                {
                    binding.notice.setVisibility(View.VISIBLE);
                }
                else if (ids != null)
                {
                    moviesId = ids;
                    List<Movie> movies = new ArrayList<>();
                    for (String id : moviesId) {
                        database.getReference("movies").orderByChild("movieId").equalTo(id).limitToFirst(1).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                GenericTypeIndicator<Movie> t = new GenericTypeIndicator<Movie>() {};
                                Movie movie = snapshot.getValue(t);
                                movies.add(movie);
                                if (movies.size() == moviesId.size())
                                {
                                    MovieAdapter adapter = new MovieAdapter(getContext(), movies, ids, self);
                                    recyclerView.setAdapter(adapter);
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
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.you_menu, menu);
        MenuItem signOutItem = menu.findItem(R.id.sign_out);
        SpannableString s = new SpannableString("Sign Out");
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#0D253F")), 0, s.length(), 0);
        signOutItem.setTitle(s);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out)
        {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}