package com.app.instagramclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.ImageClick;
import com.app.instagramclone.R;
import com.app.instagramclone.fragments.Home;
import com.app.instagramclone.model.PostsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.onViewHolder> {

    ArrayList<PostsModel> list;
    Context context;

    public HomeAdapter(ArrayList<PostsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public onViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_adpater, parent, false);
        return new onViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull onViewHolder holder, int position) {
        PostsModel model = list.get(position);

        Picasso.get().load(model.getImage()).into(holder.all_data);

        holder.all_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ImageClick.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class onViewHolder extends RecyclerView.ViewHolder{
        ImageView all_data;
        public onViewHolder(@NonNull View itemView) {
            super(itemView);
            all_data = itemView.findViewById(R.id.all_data);
        }
    }
}
