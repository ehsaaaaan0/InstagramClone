package com.app.instagramclone.profilefrgaments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.instagramclone.R;
import com.app.instagramclone.adapter.HomeAdapter;
import com.app.instagramclone.adapter.SavedPostAdapter;
import com.app.instagramclone.model.PostsModel;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedPosts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedPosts extends Fragment {
    ShimmerRecyclerView rv;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedPosts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedPosts.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedPosts newInstance(String param1, String param2) {
        SavedPosts fragment = new SavedPosts();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_saved_posts, container, false);
        rv = view.findViewById(R.id._saved_profile);
        rv.showShimmerAdapter();
        ArrayList<PostsModel> list = new ArrayList<>();
        LinearLayoutManager liner  = new LinearLayoutManager(getContext());
        rv.setLayoutManager(liner);
        SavedPostAdapter adapter = new SavedPostAdapter(list, getContext());
        rv.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("saved").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                PostsModel model = snapshot1.getValue(PostsModel.class);
                                list.add(model);
                            }
                            rv.hideShimmerAdapter();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}