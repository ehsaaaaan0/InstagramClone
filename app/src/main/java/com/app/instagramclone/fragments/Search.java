package com.app.instagramclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.app.instagramclone.R;
import com.app.instagramclone.adapter.SearchPostAdapter;
import com.app.instagramclone.adapter.SearchUserAdapter;
import com.app.instagramclone.model.PostsModel;
import com.app.instagramclone.model.Register;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    ShimmerRecyclerView post_rv,search_rv;
    EditText search_user;
    ArrayList<PostsModel> postList;
    DatabaseReference ref;
    ArrayList<Register>list = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        post_rv = view.findViewById(R.id.post_rv);
        post_rv.showShimmerAdapter();
        search_rv = view.findViewById(R.id.search_rv);
        search_rv.showShimmerAdapter();
        search_user = view.findViewById(R.id.search_user);



        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String getText = search_user.getText().toString();
                searchh(getText);
                if (!getText.isEmpty()){
                    post_rv.setVisibility(View.GONE);
                    search_rv.setVisibility(View.VISIBLE);
                }
                else{
                    post_rv.setVisibility(View.VISIBLE);
                    search_rv.setVisibility(View.GONE);
                }
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





        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Register register = snapshot1.getValue(Register.class);
                        list.add(register);
                    }
                    SearchUserAdapter adapter = new SearchUserAdapter(list, getContext());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    search_rv.setLayoutManager(linearLayoutManager);
                    search_rv.setAdapter(adapter);

                    search_rv.setAdapter(adapter);
                    search_rv.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        postList = new ArrayList<>();
        SearchPostAdapter adapter1 = new SearchPostAdapter(postList, getContext());
        post_rv.setLayoutManager(new GridLayoutManager(getContext(), 3));


        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    PostsModel postsModel = snapshot1.getValue(PostsModel.class);
                    postList.add(postsModel);
                }
                post_rv.setAdapter(adapter1);
                post_rv.hideShimmerAdapter();
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }
    private void searchh(String s) {


//        Query query = ref.orderByChild("username").startAt(s).endAt(s +"\uf8ff");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    list.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren())
                    {

                        Register register = dss.getValue(Register.class);
                        if (register.getUsername().toLowerCase().contains(s.toLowerCase())){
                            list.add(register);
                        }
                        if (Objects.equals(register.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            list.remove(register);
                        }

                    }
                    SearchUserAdapter adapter = new SearchUserAdapter(list, getContext());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    search_rv.setLayoutManager(linearLayoutManager);
                    search_rv.setAdapter(adapter);

                    search_rv.hideShimmerAdapter();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}