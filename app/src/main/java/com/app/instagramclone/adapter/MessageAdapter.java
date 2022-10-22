package com.app.instagramclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.MessageActivity;
import com.app.instagramclone.R;
import com.app.instagramclone.SearchedUser;
import com.app.instagramclone.model.MessageModel;
import com.app.instagramclone.model.Register;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myViewHolder> {

    ArrayList<Register> list;
    Context context;

    public MessageAdapter(ArrayList<Register> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_rv, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Register model = list.get(position);

                Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_user).into(holder.image_message);
                holder.message_fullName.setText(model.getFull_name());
                holder.message_username.setText(model.getUsername());

        holder.open_message_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("image", model.getImage());
                i.putExtra("userId", model.getId());
                i.putExtra("username", model.getUsername());
                context.startActivity(i);


            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image_message;
        TextView message_fullName,message_username;
        ConstraintLayout open_message_activity;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            message_fullName = itemView.findViewById(R.id.message_fullName);
            image_message = itemView.findViewById(R.id.profile_message);
            message_username = itemView.findViewById(R.id.message_username);
            open_message_activity = itemView.findViewById(R.id.open_message_activity);
        }
    }
}
