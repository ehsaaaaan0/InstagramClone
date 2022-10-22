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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SavedPostAdapter extends RecyclerView.Adapter<SavedPostAdapter.myViewHolder> {

    ArrayList<PostsModel> list;
    Context context;

    public SavedPostAdapter(ArrayList<PostsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_adpater, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        PostsModel model = list.get(position);

        String key = model.getSavedPost();
        FirebaseDatabase.getInstance().getReference().child("Posts").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("image").getValue(String.class)).into(holder.all_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView all_data;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            all_data = itemView.findViewById(R.id.all_data);
        }
    }
}
