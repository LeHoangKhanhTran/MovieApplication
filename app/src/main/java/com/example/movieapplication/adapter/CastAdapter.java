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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.R;
import com.example.movieapplication.model.Cast;
import com.example.movieapplication.model.People;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    private Context context;
    private List<Cast> castList;
    private List<People> peopleList;
    public CastAdapter(Context context, List<Cast> casts, List<People> people)
    {
        this.context = context;
        this.castList = casts;
        this.peopleList = people;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_layout, parent, false);
        return new CastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = castList.get(position);
        People people = peopleList.get(position);
        Picasso.get().load(people.getImage()).resize(485, 630).centerCrop().into(holder.imageView);
        holder.castName.setText(people.getFullName());
        holder.characterName.setText(cast.getCharacter());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", people.getFullName());
                bundle.putString("id", people.getPeopleId());
                NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_info, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView castName;

        private final TextView characterName;
        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            castName = itemView.findViewById(R.id.cast_name);
            characterName = itemView.findViewById(R.id.character_name);
        }
    }
}
