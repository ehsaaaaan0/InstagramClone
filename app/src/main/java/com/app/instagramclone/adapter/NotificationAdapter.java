package com.app.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.R;
import com.app.instagramclone.model.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.myViewHolder> {

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_rv, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        NotificationModel model = list.get(position);
        holder.message.setText(model.getNotitext());
        holder.message.setText(model.getNotitext());
        long time = model.getTime();
        long postat = new Date().getTime();
        long finaltime = ((((postat-time)/60)/60)/24);

        Picasso.get().load(model.getImage()).into(holder.postimage, new Callback() {
            @Override
            public void onSuccess() {
                holder.postimage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        holder.notitime.setText(finaltime+" minutes ago");
        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("Image").getValue(String.class)).placeholder(R.drawable.profile_user).into(holder.profile);
                holder.name.setText(snapshot.child("username").getValue(String.class));
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
        TextView name, message,notitime;
        ImageView postimage;
        CircleImageView profile;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            postimage = itemView.findViewById(R.id.postimage);
            name = itemView.findViewById(R.id.notUserName);
            message = itemView.findViewById(R.id.nottext);
            profile = itemView.findViewById(R.id.notiProfile);
            notitime = itemView.findViewById(R.id.notitime);
        }
    }
}
