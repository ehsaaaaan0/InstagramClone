package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.instagramclone.model.PostsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Random;

public class  UploadPost extends AppCompatActivity {
    ImageView imageView4,tickErrow,backarrow;
    Uri uri;
    EditText title, description;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        imageView4 = findViewById(R.id.imageView4);
        tickErrow = findViewById(R.id.tickErrow);
        backarrow = findViewById(R.id.backarrow);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        storage = FirebaseStorage.getInstance();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Your Image...!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Intent i = getIntent();
        uri = i.getParcelableExtra("post");
        imageView4.setImageURI(uri);

        tickErrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                long postat = new Date().getTime();

                final StorageReference reference = storage.getReference().child("post").child(String.valueOf(postat));
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                int likes = 0;
                                String reference1 = FirebaseDatabase.getInstance().getReference().child("Posts").push().getKey();
                                PostsModel model = new PostsModel(id, title.getText().toString(), description.getText().toString(), uri.toString() ,postat, likes, reference1);
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(reference1).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Posts").child(reference1).setValue(model);
                                        dialog.dismiss();
                                        startActivity(new Intent(UploadPost.this, MainActivity.class));
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(UploadPost.this, "Error please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}