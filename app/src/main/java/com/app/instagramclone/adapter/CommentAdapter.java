package com.app.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.R;
import com.app.instagramclone.model.CommentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.myViewHolder> {

    ArrayList<CommentModel> list ;
    Context context;

    public CommentAdapter(ArrayList<CommentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_sample, parent , false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CommentModel model = list.get(position);
        String id = model.getId();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("image").getValue(String.class)).placeholder(R.drawable.profile_user).into(holder.img);
                holder.coment_name.setText(snapshot.child("username").getValue(String.class));
                holder.comment_here.setText(model.getComment());
                holder.com_time.setText(((((new Date().getTime() - model.getTime())/60)/60)/24)+" minutes ago");
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
        CircleImageView img;
        TextView coment_name,comment_here,com_time;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.comment_username);
            coment_name = itemView.findViewById(R.id.coment_name);
            comment_here = itemView.findViewById(R.id.comment_here);
            com_time = itemView.findViewById(R.id.com_time);
        }
    }
}
