package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.instagramclone.adapter.CommentAdapter;
import com.app.instagramclone.model.CommentModel;
import com.app.instagramclone.model.NotificationModel;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComentPost extends AppCompatActivity {
    CircleImageView post_img,comment_picture;
    TextView post_username,title,post_description,post;
    EditText comment_add;
    ShimmerRecyclerView commentRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coment_post);
        commentRv = findViewById(R.id.commentRv);
        commentRv.showShimmerAdapter();
        post_img = findViewById(R.id.post_img);
        post_description = findViewById(R.id.post_description);
        post_username = findViewById(R.id.post_username);
        title = findViewById(R.id.title);
        comment_add = findViewById(R.id.comment_add);
        post = findViewById(R.id.textView4);
        comment_picture = findViewById(R.id.comment_picture);


        Intent i = getIntent();
        String post_com = i.getStringExtra("post");
        String username = i.getStringExtra("username");
        String image = i.getStringExtra("image");
        int likes = i.getIntExtra("likes", 0);
        String title_ = i.getStringExtra("titile");
        String description = i.getStringExtra("description");
        String key = i.getStringExtra("postKey");
        String postId = i.getStringExtra("postId");

        Picasso.get().load(image).placeholder(R.drawable.profile_user).into(post_img);
        post_username.setText(username);
        post_description.setText(description);
        title.setText(title_);


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("image").getValue(String.class)).placeholder(R.drawable.profile_user).into(comment_picture);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CommentModel cmd = new CommentModel(comment_add.getText().toString().trim(), id, new Date().getTime());
                FirebaseDatabase.getInstance().getReference().child("PostComments").child(key).setValue(cmd);

                String text = "Comment on your Photo";
                NotificationModel notificationModel = new NotificationModel(id, text, new Date().getTime(),key,post_com);
                notificationModel.setImage(post_com);
                FirebaseDatabase.getInstance().getReference().child("Users").child(postId).child("Notification").child(key).setValue(notificationModel);
            }
        });

        ArrayList<CommentModel> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("PostComments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        CommentModel model = snapshot1.getValue(CommentModel.class);
                        list.add(model);
                    }
                    CommentAdapter adapter = new CommentAdapter(list, ComentPost.this);
                    LinearLayoutManager liner = new LinearLayoutManager(ComentPost.this);
                    commentRv.setLayoutManager(liner);
                    commentRv.setAdapter(adapter);
                    commentRv.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}