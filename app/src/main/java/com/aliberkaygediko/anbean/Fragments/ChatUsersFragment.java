package com.aliberkaygediko.anbean.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.aliberkaygediko.anbean.Adapter.ChatUserAdapter;
import com.aliberkaygediko.anbean.Model.User;
import com.aliberkaygediko.anbean.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatUsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatUserAdapter userAdapter;
    private List<User> mUsers;
    private List<String> idList;
    EditText search_users;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_users_chat, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        search_users = view.findViewById(R.id.search_users_chat);
        mUsers=new ArrayList<>();

        recyclerView=view.findViewById(R.id.recycler_view_chat_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new ChatUserAdapter(getContext(), mUsers, false);
        recyclerView.setAdapter(userAdapter);

        idList = new ArrayList<>();

        getFollowing();

        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
    private void searchUsers(String s){





       Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                      for (String id : idList){
                            if (user.getId().equals(id)){
                                mUsers.add(user);

                            }
                        }
                }

               // userAdapter=new ChatUserAdapter(getContext(),mUsers,false);
               // recyclerView.setAdapter(userAdapter);


                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readUsers() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        for (String id : idList){
                            if (user.getId().equals(id)){
                                mUsers.add(user);

                            }
                        }
                    }
                   // userAdapter.notifyDataSetChanged();
                    userAdapter=new ChatUserAdapter(getContext(),mUsers,false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowing() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(firebaseUser.getUid()).child("following");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                readUsers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}