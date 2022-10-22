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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.ComentPost;
import com.app.instagramclone.R;
import com.app.instagramclone.SearchedUser;
import com.app.instagramclone.model.NotificationModel;
import com.app.instagramclone.model.PostModel;
import com.app.instagramclone.model.PostsModel;
import com.app.instagramclone.model.Register;
import com.app.instagramclone.model.StoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myViewHolder> {

    ArrayList<PostsModel> list;
    Context context;
    ArrayList<Register>list2;

    public PostAdapter(ArrayList<PostsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_post_rv, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        PostsModel model = list.get(position);
        list2 = new ArrayList<>();
        SuggestedAdapter adapter = new SuggestedAdapter(list2, context);
        LinearLayoutManager liner = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.suggested_rv.setLayoutManager(liner);
        holder.suggested_rv.setNestedScrollingEnabled(false);
        int newIndex = 0;
        if (holder.getAbsoluteAdapterPosition()==1 || holder.getAbsoluteAdapterPosition()==5 || holder.getAbsoluteAdapterPosition()==10){
            FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Register register = snapshot1.getValue(Register.class);
                            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(register.getId())) {
                                if (!Objects.equals(register.getId(), snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Followers").getKey())) {
                                    list2.add(newIndex, register);
                                }
                            }

                        }
                        adapter.notifyItemInserted(newIndex);
                        holder.suggested_rv.smoothScrollToPosition(newIndex);
                        holder.suggested_rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            holder.suggested_text.setVisibility(View.GONE);
        }
//        int ii = Integer.valueOf(list.get(position).toString());
//        if (ii==1){
//            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
//        }
//        for (int i=0; i<list.size(); i++){
//            if (i==1){
//
//        }

        String id = model.getId();
        Picasso.get().load(model.getImage()).into(holder.post);
        holder.post_desc.setText(model.getTitle());
        holder.postLikes.setText(model.getLikes() + " likes");
        holder.posttitle.setText(model.getDesciption());
        String title = holder.posttitle.getText().toString();
        String description = holder.post_desc.getText().toString();
        if (title.isEmpty()){
            holder.posttitle.setVisibility(View.GONE);
        }



        FirebaseDatabase.getInstance().getReference().child("PostLike").child(model.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(dataSnapshot.getKey())){
                            holder.like_post.setVisibility(View.GONE);
                            holder.read_like.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        long postat = new Date().getTime();
        long postdate = model.getPostAt();

        long finaltime = postat-postdate;
        long last = ((finaltime/60)/60)/24;
        if (last<=60){
        holder.post_time.setText(last + " minutes ago");
        }else if (last>60){
            long hours = last/60/12;
            holder.post_time.setText(hours+" Hours");
        }else if (last>=1440){
            long day = (last/60)/24;
            holder.post_time.setText(day +" Day");
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("Image").getValue(String.class)).placeholder(R.drawable.profile_user).into(holder.post_profile);
                holder.user_name_here.setText(snapshot.child("username").getValue(String.class));
                holder.user_name_post.setText(snapshot.child("username").getValue(String.class));
                holder.comment_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, ComentPost.class);
                        i.putExtra("post" , model.getImage());
                        i.putExtra("username", snapshot.child("username").getValue(String.class));
                        i.putExtra("image", snapshot.child("image").getValue(String.class));
                        i.putExtra("titile", model.getTitle());
                        i.putExtra("description", model.getDesciption());
                        i.putExtra("likes", model.getLikes());
                        i.putExtra("postKey", model.getKey());
                        i.putExtra("postId", model.getId());
                        context.startActivity(i);
                    }
                });

                holder.comments__post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, ComentPost.class);
                        i.putExtra("post" , model.getImage());
                        i.putExtra("username", snapshot.child("username").getValue(String.class));
                        i.putExtra("image", snapshot.child("image").getValue(String.class));
                        i.putExtra("titile", model.getTitle());
                        i.putExtra("description", model.getDesciption());
                        i.putExtra("likes", model.getLikes());
                        i.putExtra("postKey", model.getKey());
                        i.putExtra("postId", model.getId());
                        context.startActivity(i);
                    }
                });

                holder.user_name_here.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, SearchedUser.class);
                        intent.putExtra("username", snapshot.child("username").getValue(String.class));
                        intent.putExtra("name", snapshot.child("full_name").getValue(String.class));
                        intent.putExtra("image", snapshot.child("image").getValue(String.class));
                        intent.putExtra("id", id);
                        intent.putExtra("following", snapshot.child("followingcount").getValue(Integer.class));
                        intent.putExtra("followers", snapshot.child("followerscount").getValue(Integer.class));
                        context.startActivity(intent);
                    }
                });

                holder.post_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, SearchedUser.class);
                        intent.putExtra("username", snapshot.child("username").getValue(String.class));
                        intent.putExtra("name", snapshot.child("full_name").getValue(String.class));
                        intent.putExtra("image", snapshot.child("image").getValue(String.class));
                        intent.putExtra("id", id);
                        intent.putExtra("following", snapshot.child("followingcount").getValue(Integer.class));
                        intent.putExtra("followers", snapshot.child("followerscount").getValue(Integer.class));
                        context.startActivity(intent);
                    }
                });
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.like_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.read_like.setVisibility(View.VISIBLE);
                holder.like_post.setVisibility(View.GONE);

                String text = "Like Your Post";
                long time = new Date().getTime();
                String key = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Notification").push().getKey();
                NotificationModel notificationModel = new NotificationModel(id, text, time,key, model.getImage());
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Notification").child(key).setValue(notificationModel);
                FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getKey()).child("likes").setValue(model.getLikes()+1);
                FirebaseDatabase.getInstance().getReference().child("PostLike").child(model.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

//                FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getKey()).child("likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });
        holder.read_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.like_post.setVisibility(View.VISIBLE);
                holder.read_like.setVisibility(View.GONE);
                FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getKey()).child("likes").setValue(model.getLikes()-1);
                FirebaseDatabase.getInstance().getReference().child("PostLike").child(model.getKey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("saved").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (Objects.equals(model.getKey(), snapshot.getKey())){
                            holder.save.setVisibility(View.GONE);
                            holder.saved.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.save.setVisibility(View.GONE);
                holder.saved.setVisibility(View.VISIBLE);
//                Toast.makeText(context, "save click", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("saved").child(model.getKey()).child("savedPost").setValue(model.getKey());
            }
        });
        holder.saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.saved.setVisibility(View.GONE);
                holder.save.setVisibility(View.VISIBLE);
//                Toast.makeText(context, "saved click", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("saved").child(model.getKey()).removeValue();
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView post_profile;
        TextView user_name_here,user_name_post,post_desc,postLikes,posttitle,post_time,comments__post,suggested_text;
        ImageView post,like_post,read_like,comment_post,save,saved;
        RecyclerView suggested_rv;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            save = itemView.findViewById(R.id.save);
            saved = itemView.findViewById(R.id.saved);
            suggested_text = itemView.findViewById(R.id.suggested_text);
            comments__post = itemView.findViewById(R.id.comments__post);
            comment_post = itemView.findViewById(R.id.comment_post);
            post_profile = itemView.findViewById(R.id.post_profile);
            user_name_here = itemView.findViewById(R.id.user_name_here);
            post = itemView.findViewById(R.id.post);
            user_name_post = itemView.findViewById(R.id.user_name_post);
            post_desc = itemView.findViewById(R.id.post_desc);
            postLikes = itemView.findViewById(R.id.postLikes);
            posttitle = itemView.findViewById(R.id.posttitle);
            post_time = itemView.findViewById(R.id.post_time);
            like_post = itemView.findViewById(R.id.like_post_);
            read_like = itemView.findViewById(R.id.read_like);
            suggested_rv = itemView.findViewById(R.id.suggested_rv);
        }
    }

}
