package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.instagramclone.adapter.HomeAdapter;
import com.app.instagramclone.adapter.StoryAdapter;
import com.app.instagramclone.model.Follow;
import com.app.instagramclone.model.PostsModel;
import com.app.instagramclone.model.Register;
import com.app.instagramclone.model.StoryModel;
import com.app.instagramclone.model.UserStories;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
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

public class SearchedUser extends AppCompatActivity {
    CircleImageView profile_image;
    TextView name_, user_name_here,biotext,followb, followers_count,following_count,post_count;
    ShimmerRecyclerView story_recycler,videoRecycler;
    ArrayList<PostsModel> postList;
    HomeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_user);
        videoRecycler = findViewById(R.id.videoRecycler);
        videoRecycler.showShimmerAdapter();
        story_recycler = findViewById(R.id.story_recycler);
        story_recycler.showShimmerAdapter();
        post_count = findViewById(R.id.post_count);
        profile_image  =findViewById(R.id.profile_image);
        name_ = findViewById(R.id.name_);
        followb = findViewById(R.id.follow);
        followers_count = findViewById(R.id.followers_count);
        user_name_here = findViewById(R.id.user_name_here);
        biotext = findViewById(R.id.biotext);
        following_count = findViewById(R.id.following_count);
        Intent i= getIntent();
        String img =  i.getStringExtra("image");
        String name = i.getStringExtra("name");
        String userName = i.getStringExtra("username");
        String id = i.getStringExtra("id");




        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long following  = snapshot.child("Following").getChildrenCount();
                following_count.setText(following+"");
                long followers = snapshot.child("Followers").getChildrenCount();
                followers_count.setText(followers+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Picasso.get().load(img).placeholder(R.drawable.profile_user).into(profile_image);
        name_.setText(name);
        user_name_here.setText(userName);


        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.child("Posts").getChildrenCount();
                post_count.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Following").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if (Objects.equals(id, dataSnapshot.getKey())){
                                followb.setText("Following");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        followb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Notification").push().getKey();
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
                           followb.setText("Following");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").child(id).setValue(id);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("followerscount").setValue(register.getFollowerscount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Notification").child(key).setValue(follow);
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    }
                                });
                            }
                        });
            }
        });
        ArrayList<StoryModel> list = new ArrayList<>();
        StoryAdapter adapter1 = new StoryAdapter(list, SearchedUser.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        story_recycler.setLayoutManager(linearLayoutManager);
        story_recycler.setNestedScrollingEnabled(false);
        FirebaseDatabase.getInstance().getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (id.equals(dataSnapshot.getKey())) {
                            list.clear();

                            StoryModel story = new StoryModel();
                            story.setStoryBy(dataSnapshot.getKey());
                            story.setStoryAt(dataSnapshot.child("postedBy").getValue(Long.class));
                            ArrayList<UserStories> stories = new ArrayList<>();

                            for (DataSnapshot snapshot1 : dataSnapshot.child("userstories").getChildren()) {
                                UserStories userStories = snapshot1.getValue(UserStories.class);
                                stories.add(userStories);
                            }
                            story.setStories(stories);
                            list.add(story);
                                story_recycler.setAdapter(adapter1);
                                story_recycler.hideShimmerAdapter();
                                adapter1.notifyDataSetChanged();
                        }else{
                                story_recycler.hideShimmerAdapter();
                                story_recycler.setVisibility(View.GONE);
                            }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        postList = new ArrayList<>();
        adapter = new HomeAdapter(postList, SearchedUser.this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        videoRecycler.setLayoutManager(new GridLayoutManager(SearchedUser.this, 3));
        videoRecycler.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostsModel postsModel = dataSnapshot.getValue(PostsModel.class);
                    postList.add(postsModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}