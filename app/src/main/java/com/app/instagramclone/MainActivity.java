package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.app.instagramclone.fragments.Add;
import com.app.instagramclone.fragments.Home;
import com.app.instagramclone.fragments.Notification;
import com.app.instagramclone.fragments.Profile;
import com.app.instagramclone.fragments.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    CircleImageView profile_img;
    BottomNavigationView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.layoutHere);
        menu  =findViewById(R.id.nav_view);
        profile_img = findViewById(R.id.profile_img);
        Home home = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.layoutHere, home).commit();
        menu.setItemIconTintList(null);

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutHere, new Profile()).commit();
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.home){

                    Home home = new Home();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layoutHere, home).commit();
                }
                else if (item.getItemId()==R.id.search){

                    Fragment fragment = new Search();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.layoutHere , fragment).commit();
                }
                else if (item.getItemId()==R.id.plus){
                    Add add = new Add();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layoutHere, add).commit();
                }
                else if (item.getItemId()==R.id.heart){
                    Fragment fragment = new Notification();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.layoutHere ,fragment).commit();
                }
                else if (item.getItemId()==R.id.user){
                    Fragment fragment = new Profile();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.layoutHere , fragment).commit();
                }
                return true;
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("Image").getValue(String.class)).placeholder(R.drawable.profile_user).into(profile_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}