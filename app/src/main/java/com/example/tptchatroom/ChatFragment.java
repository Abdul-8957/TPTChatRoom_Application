package com.example.tptchatroom;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tptchatroom.adapter.UserListAdapter;
import com.example.tptchatroom.modal.usermodal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_chat, container, false);
       //lets bind a adapter in recylerview
        ArrayList<usermodal> list=new ArrayList<usermodal>();

        RecyclerView recycle=v.findViewById(R.id.userrecyle);
        UserListAdapter adapter=new UserListAdapter(getContext(),list);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        //
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String email=auth.getCurrentUser().getEmail();
        //let's get all records from firebasedatabase user table
        FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot user:snapshot.getChildren())
                {
                    usermodal u=new usermodal();
                    u.name=user.child("Name").getValue(String.class);
                    u.email=user.child("Email").getValue(String.class);
                    u.uid=user.getKey();
                    u.profilepic=user.child("profilepic").getValue(String.class);
                  //  if((u.email.equals(email)))
                    if(!(u.email.equals(email)))
                    list.add(u);
                   // Toast.makeText(getContext(), snapshot+"", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();

               // Log.e("Data",snapshot+"");
               // Toast.makeText(getContext(), snapshot+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();
            }
        });
       return v;
    }


}