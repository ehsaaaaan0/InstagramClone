package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.instagramclone.fragments.Profile;
import com.app.instagramclone.model.Register;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    EditText edit_fullName, edit_userName, editBio;
    LinearLayout imageselect;
    ImageView imageView3;
    CircleImageView selected_image;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Uri uri;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edit_fullName = findViewById(R.id.edit_fullName);
        edit_userName = findViewById(R.id.edit_userName);
        editBio = findViewById(R.id.editBio);
        imageselect = findViewById(R.id.imageselect);
        selected_image = findViewById(R.id.selected_image);
        imageView3  =findViewById(R.id.imageView3);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updaing Profile..!");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Register register = snapshot.getValue(Register.class);

                            Picasso.get().load(register.getImage())
                                    .placeholder(R.drawable.profile_user).into(selected_image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Intent i = getIntent();
        String name = i.getStringExtra("fullName");
        String UserName = i.getStringExtra("userName");
        String bio = i.getStringExtra("bio");


        edit_fullName.setText(name);
        edit_userName.setText(UserName);
        editBio.setText(bio);

        imageselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(EditProfile.this)
                        //Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);

                                intent.setType("image/*");

//                                startActivityForResult(Intent.createChooser( intent,"Select Image from here..."), PICK_IMAGE_REQUEST);
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
//                                startActivityForResult(Intent.createChooser(intent, "Select Image", 1));
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uplaodToDatabase();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (data.getData()!=null){
                uri = data.getData();
                selected_image.setImageURI(uri);


            }
        }

    }
    private void uplaodToDatabase() {
        dialog.show();
        final StorageReference reference = storage.getReference().child("profile").child(auth.getCurrentUser().getUid());
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Image").setValue(uri.toString());
                        FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("full_name").setValue(edit_fullName.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("username").setValue(edit_userName.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("bio").setValue(editBio.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                onBackPressed();
                                dialog.dismiss();
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfile.this, MainActivity.class));
                            }
                        });

                    }
                });
            }
        });
    }
}