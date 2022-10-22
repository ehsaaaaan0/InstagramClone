package com.app.instagramclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.R;
import com.app.instagramclone.SearchedUser;
import com.app.instagramclone.model.Follow;
import com.app.instagramclone.model.Register;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.myViewHodler> {

    ArrayList<Register> list;
    Context context;

    public SuggestedAdapter(ArrayList<Register> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_rv, parent, false);
        return new myViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHodler holder, int position) {
        Register model = list.get(position);
        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_user).into(holder.suggest_profile);
        holder.suggest_name.setText(model.getFull_name());
        holder.usernamehere_suggest.setText(model.getUsername());

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Following").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if (Objects.equals(model.getId(), dataSnapshot.getKey())){
                                holder.startFollow.setText("Following");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.sugested_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchedUser.class);
                intent.putExtra("username", model.getUsername());
                intent.putExtra("name", model.getFull_name());
                intent.putExtra("image", model.getImage());
                intent.putExtra("id", model.getId());
                intent.putExtra("following", model.getFollowingcount());
                intent.putExtra("followers", model.getFollowerscount());
                view.getContext().startActivity(intent);
            }
        });


        holder.startFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String key = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getId()).child("Notification").push().getKey();
                Register register = new Register();
                Follow follow = new Follow();
                follow.setFollowedBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
                follow.setFollowedAt(new Date().getTime());
                follow.setNotification("Follow you");
                follow.setKey(key);

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followingcount")
                        .setValue(register.getFollowingcount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.startFollow.setText("Following");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").child(model.getId()).setValue(model.getId());
                                FirebaseDatabase.getInstance().getReference().child("Users").child(model.getId()).child("followerscount").setValue(register.getFollowerscount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getId()).child("Notification").child(key).setValue(follow);
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getId()).child("Followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    }
                                });
                            }
                        });




            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHodler extends RecyclerView.ViewHolder{
        CircleImageView suggest_profile;
        TextView suggest_name,usernamehere_suggest;
        AppCompatButton startFollow;
        CardView sugested_click;
        public myViewHodler(@NonNull View itemView) {
            super(itemView);
            suggest_profile = itemView.findViewById(R.id.suggest_profile);
            suggest_name = itemView.findViewById(R.id.suggest_name);
            startFollow = itemView.findViewById(R.id.startFollow);
            sugested_click = itemView.findViewById(R.id.sugested_click);
            usernamehere_suggest = itemView.findViewById(R.id.usernamehere_suggest);

        }
    }
}