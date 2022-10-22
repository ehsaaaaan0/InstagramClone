package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.instagramclone.adapter.MesageActivity_Adapter;
import com.app.instagramclone.model.MessageModel;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView receiver_profile;
    ImageView backarrow_;
    TextView message_send,send_message;
    EditText write_message;
    ShimmerRecyclerView message_receive;
    LinearLayout liner_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message_receive = findViewById(R.id.message_receive);
        message_receive.showShimmerAdapter();
        liner_message = findViewById(R.id.liner_message);
        backarrow_ = findViewById(R.id.backarrow_);
        receiver_profile = findViewById(R.id.receiver_profile);
        message_send = findViewById(R.id.message_send);
        write_message = findViewById(R.id.write_message);
        send_message = findViewById(R.id.send_message);
        Intent i  = getIntent();
        String image = i.getStringExtra("image");
        String username = i.getStringExtra("username");
        String receiverId = i.getStringExtra("userId");
        final String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        message_send.setText(username);
        Picasso.get().load(image).placeholder(R.drawable.profile_user).into(receiver_profile);

        FirebaseDatabase.getInstance().getReference().child("Users").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                message_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), SearchedUser.class);
                        intent.putExtra("username", username);
                        intent.putExtra("name", snapshot.child("full_name").getValue(String.class));
                        intent.putExtra("image", image);
                        intent.putExtra("id", receiverId);
                        intent.putExtra("following", snapshot.child("followingcount").getValue(Integer.class));
                        intent.putExtra("followers", snapshot.child("followercount").getValue(Integer.class));
                        view.getContext().startActivity(intent);
                    }

                });
                receiver_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), SearchedUser.class);
                        intent.putExtra("username", username);
                        intent.putExtra("name", snapshot.child("full_name").getValue(String.class));
                        intent.putExtra("image", image);
                        intent.putExtra("id", receiverId);
                        intent.putExtra("following", snapshot.child("followingcount").getValue(Integer.class));
                        intent.putExtra("followers", snapshot.child("followercount").getValue(Integer.class));
                        view.getContext().startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        write_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                liner_message.setVisibility(View.GONE);
                send_message.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                liner_message.setVisibility(View.GONE);
                send_message.setVisibility(View.VISIBLE);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final MesageActivity_Adapter adapter = new MesageActivity_Adapter(messageModels , this);
        message_receive.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        message_receive.setLayoutManager(linearLayoutManager);
        message_receive.smoothScrollToPosition(adapter.getItemCount());


        final String senderRoom = senderId+receiverId;
        final String receiverRoom = receiverId+senderId;



        FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    messageModels.clear();
                    message_receive.smoothScrollToPosition(adapter.getItemCount());
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        MessageModel model = snapshot1.getValue(MessageModel.class);
                        messageModels.add(model);
                    }
                    message_receive.smoothScrollToPosition(adapter.getItemCount());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = write_message.getText().toString().trim();
                final MessageModel model = new MessageModel(senderId, message, image);
                write_message.setText("");
                FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                liner_message.setVisibility(View.VISIBLE);
                                send_message.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });





        backarrow_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}