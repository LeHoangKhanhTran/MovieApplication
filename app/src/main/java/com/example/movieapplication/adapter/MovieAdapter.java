package com.example.movieapplication.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.R;
import com.example.movieapplication.model.Movie;
import com.example.movieapplication.ui.you.YouFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Context context;
    private final List<Movie> movieList;

    private final List<String> watchListIds;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersReference;

    Fragment fragment;
    public MovieAdapter(Context context, List<Movie> movieList, List<String> ids)
    {
        this.context = context;
        Collections.reverse(movieList);
        this.movieList = movieList;
        this.watchListIds = ids;
    }
    public MovieAdapter(Context context, List<Movie> movieList, List<String> ids, Fragment frag)
    {
        this.context = context;
        Collections.reverse(movieList);
        this.movieList = movieList;
        this.watchListIds = ids;
        this.fragment = frag;
    }
    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        Movie movie = movieList.get(position);
        Picasso.get().load(movie.getPosterImg()).resize(485, 615).centerCrop().into(holder.movieImg);
        holder.ratingScore.setText(String.valueOf(movie.getRating()));
        holder.movieName.setText(movie.getMovieTitle());
        holder.releaseYear.setText(movie.getReleaseYear());
        holder.duration.setText(movie.getDuration());
        if (watchListIds.contains(movie.getMovieId()))
        {
            holder.bookmarkCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.bookmarkAdd.setVisibility(View.VISIBLE);
        }
        holder.bookmarkCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromWatchList(movie.getMovieId());
                v.setVisibility(View.GONE);
                if (fragment != null && fragment.getClass().getSimpleName().equals("YouFragment"))
                {
                    movieList.remove(movie);
                    watchListIds.remove(movie.getMovieId());
                    MovieAdapter adapter = new MovieAdapter(fragment.getContext(), movieList, watchListIds, fragment);
                    ((YouFragment) fragment).getBinding().recyclerView.setAdapter(adapter);
                }
                holder.bookmarkAdd.setVisibility(View.VISIBLE);
            }
        });
        holder.bookmarkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWatchList(movie.getMovieId());
                v.setVisibility(View.GONE);
                holder.bookmarkCheck.setVisibility(View.VISIBLE);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("movie", movie.getMovieTitle());
                bundle.putString("id", movie.getMovieId());
                NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_detail, bundle);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView movieImg;
        private final TextView ratingScore;
        private final TextView movieName;
        private final TextView releaseYear;
        private final TextView duration;

        private final ImageView bookmarkAdd;
        private final ImageView bookmarkCheck;
        public ViewHolder(View itemView)
        {
            super(itemView);
            movieImg = itemView.findViewById(R.id.movie_img);
            ratingScore = itemView.findViewById(R.id.rating_score);
            movieName = itemView.findViewById(R.id.movie_name);
            releaseYear = itemView.findViewById(R.id.character_name);
            duration = itemView.findViewById(R.id.duration);
            bookmarkAdd = itemView.findViewById(R.id.bookmark_add);
            bookmarkCheck = itemView.findViewById(R.id.bookmark_check);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
    public void removeFromWatchList(String movieId)
    {

        usersReference.child(firebaseAuth.getUid()).child("watchList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> movieIdList;
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                if (dataSnapshot.getValue(t) != null)
                {
                    movieIdList = new ArrayList<>(dataSnapshot.getValue(t));
                    movieIdList.remove(movieId);
                    Map<String, Object> watchListUpdate = new HashMap<>();
                    watchListUpdate.put("watchList", movieIdList);
                    usersReference.child(firebaseAuth.getUid()).updateChildren(watchListUpdate);
                    SharedPreferences.Editor editor = ((Activity)context).getSharedPreferences("mySharedPref", Context.MODE_PRIVATE).edit();
                    editor.putStringSet("watchlist", new HashSet<>(movieIdList));
                    editor.commit();
                }
            }
        });
    }

    public void addToWatchList(String movieId)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference usersReference = database.getReference("users");
        usersReference.child(firebaseAuth.getUid()).child("watchList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<String> movieIdList;
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                if (dataSnapshot.getValue(t) == null)
                {
                    movieIdList = new ArrayList<>();
                    movieIdList.add(movieId);
                }
                else {
                    movieIdList = new ArrayList<>(dataSnapshot.getValue(t));
                    movieIdList.add(movieId);
                }
                Map<String, Object> watchListUpdate = new HashMap<>();
                watchListUpdate.put("watchList", movieIdList);
                usersReference.child(firebaseAuth.getUid()).updateChildren(watchListUpdate);
                SharedPreferences.Editor editor = ((Activity)context).getSharedPreferences("mySharedPref", Context.MODE_PRIVATE).edit();
                editor.putStringSet("watchlist", new HashSet<>(movieIdList));
                editor.commit();
            }
        });
    }
}
