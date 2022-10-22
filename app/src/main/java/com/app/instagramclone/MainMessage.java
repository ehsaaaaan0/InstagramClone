package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.instagramclone.adapter.MessageAdapter;
import com.app.instagramclone.adapter.SearchUserAdapter;
import com.app.instagramclone.model.MessageModel;
import com.app.instagramclone.model.Register;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainMessage extends AppCompatActivity {
    ShimmerRecyclerView message_rv;
    ArrayList<Register> list;
    ImageView message_back;
    TextView username;
    EditText search_to_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);
        message_rv = findViewById(R.id.message_rv);
        message_back = findViewById(R.id.message_back);
        username  =findViewById(R.id.textView6);
        search_to_message = findViewById(R.id.search_to_message);
        message_rv.showShimmerAdapter();





        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        list = new ArrayList<>();


        search_to_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String getText = search_to_message.getText().toString();
                searchh(getText);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {


                    searchh(editable.toString());
//                    seachA(editable.toString());
                } else {
                    searchh("");
                }
            }
        });





        MessageAdapter adapter = new MessageAdapter(list, this);
        LinearLayoutManager linearLayoutManager_1 = new LinearLayoutManager(this);
        message_rv.setLayoutManager(linearLayoutManager_1);
        message_rv.setNestedScrollingEnabled(false);
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()) {
                       for (DataSnapshot snapshot1: snapshot.getChildren()) {
                           Register messageModel = snapshot1.getValue(Register.class);
                           if (!Objects.equals(messageModel.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                               list.add(messageModel);
                           }
                       }
                       message_rv.setAdapter(adapter);
                       message_rv.hideShimmerAdapter();
                       adapter.notifyDataSetChanged();
                   }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        message_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void searchh(String getText) {
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    list.clear();
                    for (DataSnapshot dss : snapshot.getChildren())
                    {

                        Register register = dss.getValue(Register.class);
                        if (register.getUsername().toLowerCase().contains(getText.toLowerCase())){
                            list.add(register);
                        }
                        if (Objects.equals(register.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            list.remove(register);
                        }
//                        else if (!Objects.equals(register.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                            list.add(register);
//                        }


                    }
                    SearchUserAdapter adapter = new SearchUserAdapter(list, MainMessage.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainMessage.this);
                    message_rv.setLayoutManager(linearLayoutManager);
                    message_rv.setAdapter(adapter);

                    message_rv.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}