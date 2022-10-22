package com.app.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.R;
import com.app.instagramclone.model.PostsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.myViewHolder> {

    ArrayList<PostsModel>list;
    Context context;

    public SearchPostAdapter(ArrayList<PostsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_post_rv, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        PostsModel model = list.get(position);
        Picasso.get().load(model.getImage()).into(holder.show_search_post);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView show_search_post;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            show_search_post = itemView.findViewById(R.id.show_search_post);
        }
    }
}
