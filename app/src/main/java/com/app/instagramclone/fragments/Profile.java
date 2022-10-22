package com.app.instagramclone.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.instagramclone.EditProfile;
import com.app.instagramclone.R;
import com.app.instagramclone.SearchedUser;
import com.app.instagramclone.adapter.HomeAdapter;
import com.app.instagramclone.adapter.StoryAdapter;
import com.app.instagramclone.authentications.LoginActivity;
import com.app.instagramclone.model.PostsModel;
import com.app.instagramclone.model.Register;
import com.app.instagramclone.model.StoryModel;
import com.app.instagramclone.model.UserStories;
import com.app.instagramclone.profilefrgaments.GridFrgament;
import com.app.instagramclone.profilefrgaments.SavedPosts;
import com.app.instagramclone.profilefrgaments.VideosFragment;
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
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    TextView user_name_here, name_, edit_profile, biotext,followers_count,following_count,post_count;
    ImageView bmenu,playallVieo,playOnlyVideo,savedPosts;
    LinearLayout update_story;
    CircleImageView profile_image;
    View v1, v2,v3;


    ShimmerRecyclerView show_user_story;
    ActivityResultLauncher<String> gallary;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        show_user_story = view.findViewById(R.id.show_user_story);
        show_user_story.showShimmerAdapter();
        v1 = view.findViewById(R.id.view1);
        v2 = view.findViewById(R.id.view2);
        v3 = view.findViewById(R.id.view3);
        post_count = view.findViewById(R.id.post_count);
        savedPosts = view.findViewById(R.id.savedPosts);
        playOnlyVideo = view.findViewById(R.id.playOnlyVideo);
        playallVieo = view.findViewById(R.id.playallVieo);
        update_story = view.findViewById(R.id.update_story);
        followers_count = view.findViewById(R.id.followers_count);
        following_count = view.findViewById(R.id.following_count);
        profile_image = view.findViewById(R.id.profile_image);
        user_name_here = view.findViewById(R.id.user_name_here);
        bmenu = view.findViewById(R.id.bmenu);
        name_ = view.findViewById(R.id.name_);
        biotext = view.findViewById(R.id.biotext);
        edit_profile = view.findViewById(R.id.edit_profile);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("register", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", null);
        String email = sharedPreferences.getString("email_phone", null);
        String dob = sharedPreferences.getString("dob", null);
        String full_name = sharedPreferences.getString("full_name", null);



        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                String follwers = snapshot.child("followerscount").getValue(String.class);
//                String following = snapshot.child("followingcount").getValue(String.class);

//                followers_count.setText(snapshot.child("followerscount").getValue(Integer.class)+"");
                long following  = snapshot.child("Following").getChildrenCount();
                following_count.setText(following+"");
//                if (followers_count.getText().toString().equals("null")){
//                    followers_count.setText(0+"");
//                }
//
                long followers = snapshot.child("Followers").getChildrenCount();
                followers_count.setText(followers+"");
//
                long count = snapshot.child("Posts").getChildrenCount();

                post_count.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        PopupMenu popup = new PopupMenu(getContext(), bmenu);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate(R.menu.log_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.Logout:{
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                    }
//
//
                                    default:
                                        Toast.makeText(getContext(), "Please Select Any Given Point", Toast.LENGTH_SHORT).show();
                                }




                                return true;
                            }

                        });

                        popup.show();
                    }
                });






        update_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallary.launch("image/*");
            }
        });
        gallary = registerForActivityResult(new ActivityResultContracts.GetContent() , new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
//                .setImageURI(result);


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



        user_name_here.setText(userName);
        name_.setText(full_name);



        //Getting data into files from firebase

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Register register = snapshot.getValue(Register.class);

                            Picasso.get().load(register.getImage())
                                    .placeholder(R.drawable.profile_user).into(profile_image);
                            name_.setText(register.getFull_name());
                            user_name_here.setText(register.getUsername());
                            biotext.setText(register.getBio());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //ENding here





        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext() , EditProfile.class);
                i.putExtra("userName", user_name_here.getText().toString());
                i.putExtra("fullName", name_.getText().toString());
                i.putExtra("bio", biotext.getText().toString());
                startActivity(i);
            }
        });




        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double count =0;
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        ArrayList<StoryModel> list = new ArrayList<>();
        StoryAdapter adapter1 = new StoryAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        show_user_story.setLayoutManager(linearLayoutManager1);
        show_user_story.setNestedScrollingEnabled(false);
        FirebaseDatabase.getInstance().getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        list.clear();
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

                    show_user_story.setAdapter(adapter1);
                    show_user_story.hideShimmerAdapter();
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0,0).replace(R.id.frgament_view, new GridFrgament()).commit();

        playallVieo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0,0).replace(R.id.frgament_view, new GridFrgament()).commit();
            }
        });
        playOnlyVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.GONE);
                v2.setVisibility(View.VISIBLE);
                v3.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0,0).replace(R.id.frgament_view, new VideosFragment()).commit();
            }
        });
        savedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.setVisibility(View.GONE);
                v2.setVisibility(View.GONE);
                v3.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0,0).replace(R.id.frgament_view, new SavedPosts()).commit();
            }
        });



        return view;
    }
}