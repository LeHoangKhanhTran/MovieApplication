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
import com.example.movieapplication.model.People;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PeopleSearchAdapter extends RecyclerView.Adapter<PeopleSearchAdapter.ViewHolder> {
    private Context context;
    private List<People> peopleList;
    public PeopleSearchAdapter(Context context, List<People> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        People people = peopleList.get(position);
        holder.peopleName.setText(people.getFullName());
        Picasso.get().load(people.getImage()).resize(485, 630).centerCrop().into(holder.peopleImg);
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
        return peopleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView peopleImg;
        private final TextView peopleName;
        public ViewHolder (View itemView)
        {
            super(itemView);
            peopleImg = itemView.findViewById(R.id.people_img);
            peopleName = itemView.findViewById(R.id.people_name);
        }
    }
}
