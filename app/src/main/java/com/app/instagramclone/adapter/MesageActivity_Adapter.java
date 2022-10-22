package com.app.instagramclone.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.R;
import com.app.instagramclone.model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesageActivity_Adapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> list;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MesageActivity_Adapter(ArrayList<MessageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_messagedesign, parent, false);
            return new senderMessageviewholder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_messagedesign, parent, false);
            return new receiverMessageviewholder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel model  = list.get(position);

        if (holder.getClass()== senderMessageviewholder.class){
            ((senderMessageviewholder)holder).sendermessage.setText(model.getMessage());
        }else{
            ((receiverMessageviewholder)holder).receivermessage.setText(model.getMessage());
//            FirebaseDatabase.getInstance().getReference().child("Users").child(model.getId()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_user).into(((receiverMessageviewholder)holder).receivermsg_profile);

//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            Picasso.get().load(((receiverMessageviewholder)holder).receivermsg_profile).placeholder(R.drawable.profile_user).into(model.getImage());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class senderMessageviewholder extends RecyclerView.ViewHolder{
        TextView sendermessage;
        public senderMessageviewholder(@NonNull View itemView) {
            super(itemView);
            sendermessage = itemView.findViewById(R.id.sendermessage_);
        }
    }

    public class receiverMessageviewholder extends RecyclerView.ViewHolder{
        CircleImageView receivermsg_profile;
        TextView receivermessage;
        public receiverMessageviewholder(@NonNull View itemView) {
            super(itemView);
            receivermsg_profile = itemView.findViewById(R.id.receivermsg_profile);
            receivermessage = itemView.findViewById(R.id.receivermessage_);
        }
    }
}
