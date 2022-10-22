package com.app.instagramclone.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.instagramclone.MainMessage;
import com.app.instagramclone.R;
import com.app.instagramclone.adapter.PostAdapter;
import com.app.instagramclone.adapter.StoryAdapter;
import com.app.instagramclone.model.PostModel;
import com.app.instagramclone.model.PostsModel;
import com.app.instagramclone.model.StoryModel;
import com.app.instagramclone.model.UserStories;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends Fragment {

    ShimmerRecyclerView postRv, storyRv;
    ArrayList<StoryModel> list;
    ArrayList<PostsModel> postList;
    PostAdapter adapter;
    TextView user_name_story;
    ImageView message_user;
    CircleImageView circleImageView;
    ActivityResultLauncher<String> gallary;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }



    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        storyRv = view.findViewById(R.id.recycler_story);
        storyRv.showShimmerAdapter();
        postRv = view.findViewById(R.id.post_recyclerView);
        postRv.showShimmerAdapter();
        circleImageView = view.findViewById(R.id.circleImageView_);
        message_user = view.findViewById(R.id.message_user);
        user_name_story = view.findViewById(R.id.user_name_story);


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Picasso.get().load(snapshot.child("Image").getValue(String.class)).placeholder(R.drawable.profile_user).into(circleImageView);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });









        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallary.launch("image/*");
            }
        });

        gallary = registerForActivityResult(new ActivityResultContracts.GetContent() , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        circleImageView.setImageURI(result);


                        StorageReference reference = FirebaseStorage.getInstance().getReference().child("stories").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(new Date().getTime()+"");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        StoryModel story = new StoryModel();
                                        story.setStoryAt(new Date().getTime());
                                        FirebaseDatabase.getInstance().getReference().child("stories").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("postedBy").setValue(story.getStoryAt());
                                        UserStories stories = new UserStories(uri.toString(), story.getStoryAt());
                                        FirebaseDatabase.getInstance().getReference().child("stories").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("userstories").push().setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });
                                    }
                                });
                            }
                        });

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name_story.setText(snapshot.child("username").getValue(String.class));
                Picasso.get().load(snapshot.child("image").getValue(String.class)).placeholder(R.drawable.profile_user).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        list = new ArrayList<>();
        StoryAdapter adapter1 = new StoryAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);


        FirebaseDatabase.getInstance().getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        StoryModel story = new StoryModel();
                        story.setStoryBy(dataSnapshot.getKey());
                        story.setStoryAt(dataSnapshot.child("postedBy").getValue(Long.class));
                        ArrayList<UserStories> stories = new ArrayList<>();

                        for (DataSnapshot snapshot1 : dataSnapshot.child("userstories").getChildren()){
                            UserStories userStories = snapshot1.getValue(UserStories.class);
                            stories.add(userStories);
                        }
                        story.setStories(stories);
                        list.add(story);
                    }
                    storyRv.setAdapter(adapter1);
                    storyRv.hideShimmerAdapter();
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        postList = new ArrayList<>();
        Collections.reverse(postList);
        int newIndex = 0;
        adapter = new PostAdapter(postList, getContext());
        postRv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_1 = new LinearLayoutManager(getContext());
        postRv.setLayoutManager(linearLayoutManager_1);
        postRv.setNestedScrollingEnabled(false);

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostsModel postsModel = dataSnapshot.getValue(PostsModel.class);
                    postList.add(newIndex,postsModel);
//                    notifyItemInserted(newIndex);

                }
                adapter.notifyItemInserted(newIndex);
                postRv.smoothScrollToPosition(newIndex);
                postRv.setAdapter(adapter);
                postRv.hideShimmerAdapter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        message_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainMessage.class));
            }
        });


        return view;
    }
}