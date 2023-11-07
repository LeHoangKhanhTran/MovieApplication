package com.example.movieapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.R;
import com.example.movieapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movieList;
    private SearchView searchView;
    public MovieSearchAdapter(Context context, List<Movie> movies, SearchView searchView)
    {
        this.context = context;
        this.movieList = movies;
        this.searchView = searchView;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        String genres = "";
        for (int i = 0; i < movie.getGenres().size(); i++)
        {
            genres += movie.getGenres().get(i);
            if (i < movie.getGenres().size() - 1)
            {
                genres += ", ";
            }
        }
        if (genres.length() > 28)
        {
            genres = genres.substring(0, 26) + "...";
        }
        holder.title.setText(movie.getMovieTitle());
        holder.releaseYear.setText(movie.getReleaseYear());
        Picasso.get().load(movie.getPosterImg()).resize(485, 630).centerCrop().into(holder.posterImg);
        holder.genres.setText(genres);
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
        private final TextView title;
        private final TextView releaseYear;
        private final ImageView posterImg;
        private final TextView genres;

        public ViewHolder(View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            releaseYear = itemView.findViewById(R.id.character_name);
            posterImg = itemView.findViewById(R.id.poster);
            genres = itemView.findViewById(R.id.movie_genres);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
