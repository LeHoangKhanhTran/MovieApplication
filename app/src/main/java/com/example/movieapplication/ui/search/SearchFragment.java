package com.example.movieapplication.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapplication.MainActivity;
import com.example.movieapplication.R;
import com.example.movieapplication.adapter.MovieSearchAdapter;
import com.example.movieapplication.adapter.PeopleSearchAdapter;
import com.example.movieapplication.databinding.FragmentSearchBinding;
import com.example.movieapplication.model.Movie;
import com.example.movieapplication.model.People;
import com.example.movieapplication.ui.detail.DetailFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private androidx.appcompat.widget.SearchView searchView = null;
    private List<Movie> movies;

    private List<People> peopleList;

    private String searchCategory = "Movie";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("movies");
        DatabaseReference databaseReference1 = database.getReference("people");
        movies = new ArrayList<>();
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    GenericTypeIndicator<Movie> t = new GenericTypeIndicator<Movie>() {};
                    Movie movie = child.getValue(t);
                    movies.add(movie);
                }
            }
        });
        peopleList = new ArrayList<>();
       databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    GenericTypeIndicator<People> t = new GenericTypeIndicator<People>() {};
                    People people = child.getValue(t);
                    peopleList.add(people);
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(getContext().getDrawable(R.drawable.divider));
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchCategory.equals("Movie"))
                {
                    if (searchView.getQuery().length() > 0)
                    {
                        setMoviesResults(searchView.getQuery().toString());
                    }
                    binding.movieLine.setVisibility(View.VISIBLE);
                    binding.peopleLine.setVisibility(View.INVISIBLE);
                    searchCategory = "Movie";
                }
            }
        });
        binding.people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchCategory.equals("People"))
                {
                    if (searchView.getQuery().length() > 0)
                    {
                        setPeopleResults(searchView.getQuery().toString());
                    }
                    binding.peopleLine.setVisibility(View.VISIBLE);
                    binding.movieLine.setVisibility(View.INVISIBLE);
                    searchCategory = "People";
                }
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        if (searchItem != null)
        {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    switch (searchCategory)
                    {
                        case "Movie":
                        {
                            setMoviesResults(newText);
                        }
                        break;
                        case "People":
                        {
                            setPeopleResults(newText);
                        }
                        break;
                    }
                    return true;
                }

            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void setMoviesResults(String text)
    {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie: movies) {
            if (movie.getMovieTitle().toLowerCase().contains(text.toLowerCase()) && text.length() > 0)
            {
                filteredMovies.add(movie);
            }
        }
        MovieSearchAdapter adapter = new MovieSearchAdapter(getContext(), filteredMovies, searchView);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setPeopleResults(String text)
    {
        List<People> filteredPeople = new ArrayList<>();
        for (People people: peopleList) {
            if (people.getFullName().toLowerCase().contains(text.toLowerCase()) && text.length() > 0)
            {
                filteredPeople.add(people);
            }
        }
        PeopleSearchAdapter adapter = new PeopleSearchAdapter(getContext(), filteredPeople);
        binding.recyclerView.setAdapter(adapter);
    }
}