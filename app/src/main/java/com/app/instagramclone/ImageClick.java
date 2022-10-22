package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.instagramclone.adapter.HomeAdapter;
import com.app.instagramclone.adapter.PostAdapter;
import com.app.instagramclone.model.PostsModel;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageClick extends AppCompatActivity {
    ShimmerRecyclerView home_post_recyclerView;
    ArrayList<PostsModel> arrayList;
    ImageView imageView5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_click);
        home_post_recyclerView = findViewById(R.id.home_post_recyclerView);
        home_post_recyclerView.showShimmerAdapter();
        imageView5 = findViewById(R.id.imageView5);
        arrayList = new ArrayList<>();
        LinearLayoutManager liner  = new LinearLayoutManager(this);
        home_post_recyclerView.setLayoutManager(liner);
        PostAdapter adpater = new PostAdapter(arrayList, this);
        home_post_recyclerView.setNestedScrollingEnabled(false);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    arrayList.clear();
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        PostsModel model = snapshot1.getValue(PostsModel.class);
                        arrayList.add(model);
                    }
                    home_post_recyclerView.setAdapter(adpater);
                    home_post_recyclerView.hideShimmerAdapter();
                    adpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}