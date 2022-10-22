package com.app.instagramclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.instagramclone.R;
import com.app.instagramclone.SearchedUser;
import com.app.instagramclone.model.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.myViewHolder> {
    public SearchUserAdapter(ArrayList<Register> list, Context context) {
        this.list = list;
        this.context = context;
    }

    ArrayList<Register>list;
    Context context;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchuser_rv, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Register register = list.get(position);
            holder.search_user_name.setText(register.getFull_name());
            holder.search_user_username.setText(register.getUsername());
            Picasso.get().load(register.getImage()).placeholder(R.drawable.profile_user).into(holder.search_user_proile);
//        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//        if (Objects.equals(register.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
//            holder.disbalethis.setVisibility(View.GONE);
//        }

        holder.disbalethis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchedUser.class);
                intent.putExtra("username", holder.search_user_username.getText().toString());
                intent.putExtra("name", holder.search_user_name.getText().toString());
                intent.putExtra("image", register.getImage());
                intent.putExtra("id", register.getId());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView search_user_proile;
        TextView search_user_username,search_user_name;
        ConstraintLayout disbalethis;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            search_user_proile = itemView.findViewById(R.id.search_user_proile);
            search_user_username = itemView.findViewById(R.id.search_user_username);
            search_user_name = itemView.findViewById(R.id.search_user_name);
            disbalethis = itemView.findViewById(R.id.disbalethis);
        }
    }
}
